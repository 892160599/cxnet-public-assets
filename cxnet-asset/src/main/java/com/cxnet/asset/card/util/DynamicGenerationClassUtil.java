package com.cxnet.asset.card.util;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotBlank;

import com.cxnet.asset.astfield.domain.AstField;
import com.cxnet.common.constant.Constants;
import com.cxnet.common.utils.StringUtils;
import com.google.zxing.NotFoundException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.DoubleMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DynamicGenerationClassUtil {
    public static final String CLASS_NAME_PREFIX = "com.cxnet.asset.card.domain.AstCard@";
    public static final String ANNOTATION_PACKAGE_NAME = "com.cxnet.common.utils.poi.annotation.Excel";
    public static final String STRING_PACKAGE_NAME = "java.lang.String";
    public static final String NOTBLANK_NAME = "javax.validation.constraints.NotBlank";
    public static final String CONVERT_NAME = "com.cxnet.common.constant.Convert";
    public static final String LENGTH_NAME = "org.hibernate.validator.constraints.Length";

    /**
     * 动态生成实体
     *
     * @param list
     * @return
     * @throws javassist.NotFoundException
     * @throws CannotCompileException
     * @throws javassist.NotFoundException
     */
    public static Class<?> generatePrototypeClass(List<AstField> list) {

        String className = CLASS_NAME_PREFIX + UUID.randomUUID().toString();
        ClassPool pool = ClassPool.getDefault();
        CtClass clazz = pool.makeClass(className);
        ClassFile ccFile = clazz.getClassFile();
        ConstPool constpool = ccFile.getConstPool();
        //添加fields
        addExpressField(pool, clazz, constpool, list);

        Class<?> entityClass = null;
        try {
            entityClass = clazz.toClass();
        } catch (CannotCompileException e) {
            log.error("错误原因:{}", e.getMessage(), e);
        }

        return entityClass;
    }

    /**
     * 设置属性
     *
     * @param pool
     * @param clazz
     * @param constpool
     * @param list
     * @throws javassist.NotFoundException
     * @throws CannotCompileException
     * @throws javassist.NotFoundException
     */
    @NotBlank
    private static void addExpressField(ClassPool pool, CtClass clazz, ConstPool constpool, List<AstField> list) {
        // 将数据库查出动态附上property 属性
        for (AstField astField : list) {
            addFieldAndAnnotation(pool, clazz, constpool, astField);
        }
    }


    /**
     * 设置注解
     *
     * @param pool
     * @param clazz
     * @param constpool
     * @param titleName
     * @param fieldName
     * @throws javassist.NotFoundException
     * @throws NotFoundException
     * @throws CannotCompileException
     * @throws javassist.NotFoundException
     */
    private static void addFieldAndAnnotation(ClassPool pool, CtClass clazz, ConstPool constpool, AstField astField) {

        //生成field
        CtField field = null;
        try {
            field = new CtField(pool.get(STRING_PACKAGE_NAME), astField.getFieldCode(), clazz);
        } catch (javassist.NotFoundException e) {
            log.error("错误原因:{}", e.getMessage(), e);
        } catch (CannotCompileException e) {
            log.error("错误原因:{}", e.getMessage(), e);
        }
        field.setModifiers(Modifier.PUBLIC);

        //添加easypoi的注解
        AnnotationsAttribute fieldAttr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);


        //添加excel注解
        fieldAttr.addAnnotation(setExcelAnnotation(astField, constpool));

        //如果字段必填的话，添加非空验证
        if (Constants.YES.equals(astField.getIsRequired())) {
            fieldAttr.addAnnotation(setNullAnnotation(astField, constpool));
        }

        //添加转换注解
        if (StringUtils.isNotBlank(astField.getDictKey())) {
            fieldAttr.addAnnotation(setConvertAnnotation(astField, constpool));
        }

        //添加最大长度注解
        if (StringUtils.isNotBlank(astField.getMaxLen())) {
            fieldAttr.addAnnotation(setLengthtAnnotation(astField, constpool));
        }


        field.getFieldInfo().addAttribute(fieldAttr);

        //生成get,set方法
        try {
            clazz.addMethod(CtNewMethod.getter("get" + upperFirstLatter(astField.getFieldCode()), field));
            clazz.addMethod(CtNewMethod.setter("set" + upperFirstLatter(astField.getFieldCode()), field));
            clazz.addField(field);
        } catch (CannotCompileException e) {
            log.error("错误原因:{}", e.getMessage(), e);
        }

    }

    /**
     * 将属性换成get，set方法
     *
     * @param letter
     * @return
     */
    private static String upperFirstLatter(String letter) {
        return letter.substring(0, 1).toUpperCase() + letter.substring(1);
    }

    /**
     * 设置excel注解
     *
     * @param astField
     * @param constpool
     * @return
     */
    private static Annotation setExcelAnnotation(AstField astField, ConstPool constpool) {
        Annotation excelAnnotation = new Annotation(ANNOTATION_PACKAGE_NAME, constpool);
        //设置导出excel中的名称
        excelAnnotation.addMemberValue("name", new StringMemberValue(astField.getExportTitle(), constpool));

        //设置宽度
        if (null != astField.getWidth()) {
            excelAnnotation.addMemberValue("width", new DoubleMemberValue(astField.getWidth().doubleValue(), constpool));
        }

        //设置高度
        if (null != astField.getHeight()) {
            excelAnnotation.addMemberValue("height", new DoubleMemberValue(astField.getHeight().doubleValue(), constpool));
        }

        //设置日期格式
        if (StringUtils.isNotBlank(astField.getDateFormat())) {
            excelAnnotation.addMemberValue("dateFormat", new StringMemberValue(astField.getDateFormat(), constpool));
        }

        //设置默认值
        if (StringUtils.isNotBlank(astField.getDefaultValue())) {
            excelAnnotation.addMemberValue("defaultValue", new StringMemberValue(astField.getDefaultValue(), constpool));
        }

        return excelAnnotation;
    }


    /**
     * 设置非空注解
     *
     * @param astField
     * @param constpool
     * @return
     */
    private static Annotation setNullAnnotation(AstField astField, ConstPool constpool) {
        //非空验证
        Annotation nullAnnotation = new Annotation(NOTBLANK_NAME, constpool);
        nullAnnotation.addMemberValue("message", new StringMemberValue(StringUtils.isBlank(astField.getRequiredInformation()) ? "请输入合法参数" : astField.getRequiredInformation(), constpool));
        return nullAnnotation;
    }


    /**
     * 设置转换注解
     *
     * @param astField
     * @param constpool
     * @return
     */
    private static Annotation setConvertAnnotation(AstField astField, ConstPool constpool) {
        //转换注解
        Annotation convertAnnotation = new Annotation(CONVERT_NAME, constpool);
        convertAnnotation.addMemberValue("key", new StringMemberValue(astField.getDictKey(), constpool));
        return convertAnnotation;
    }


    /**
     * 设置最大长度
     *
     * @param astField
     * @param constpool
     * @return
     */
    private static Annotation setLengthtAnnotation(AstField astField, ConstPool constpool) {
        //转换注解
        Annotation convertAnnotation = new Annotation(LENGTH_NAME, constpool);
        convertAnnotation.addMemberValue("max", new StringMemberValue(astField.getMaxLen(), constpool));
        return convertAnnotation;
    }

}
