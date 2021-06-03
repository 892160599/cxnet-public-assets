package com.cxnet.common.utils.poi.jxls;


import cn.hutool.core.date.DateUtil;
import com.cxnet.common.exception.CustomException;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.command.EachCommand;
import org.jxls.command.ImageCommand;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JxlsUtils
 *
 * @author
 */
public class JxlsUtils {

    static {
        //添加自定义指令（可覆盖jxls原指令）
        XlsCommentAreaBuilder.addCommandMapping("image", ImageCommand.class);
        XlsCommentAreaBuilder.addCommandMapping("each", EachCommand.class);
        XlsCommentAreaBuilder.addCommandMapping("merge", MergeCommand.class);
//        XlsCommentAreaBuilder.addCommandMapping("link", LinkCommand.class);
    }

    public static void exportExcel(InputStream is, OutputStream os, Map<String, Object> model) throws IOException {
        Context context = PoiTransformer.createInitialContext();
        if (model != null) {
            for (String key : model.keySet()) {
                context.putVar(key, model.get(key));
            }
        }
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer = jxlsHelper.createTransformer(is, os);
        //获得配置
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
        //设置静默模式，不报警告
        evaluator.getJexlEngine().setSilent(true);
        //函数强制，自定义功能
        Map<String, Object> funcs = new HashMap<String, Object>();
        //添加自定义功能
        funcs.put("utils", new JxlsUtils());
        evaluator.getJexlEngine().setFunctions(funcs);
        jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);
    }

    public static void exportExcel(File xls, File out, Map<String, Object> model) throws FileNotFoundException, IOException {
        exportExcel(new FileInputStream(xls), new FileOutputStream(out), model);
    }

    public static void exportExcel(String templatePath, OutputStream os, Map<String, Object> model) throws Exception {
        File template = getTemplate(templatePath);
        if (template != null) {
            try {
                exportExcel(new FileInputStream(template), os, model);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                os.close();
            }
        } else {
            throw new CustomException("Excel 模板未找到");
        }
    }

    /**
     * 获取jxls模版文件
     *
     * @param path
     * @return
     */
    public static File getTemplate(String path) {
        File template = new File(path);
        if (template.exists()) {
            return template;
        }
        return null;
    }

    /**
     * 日期格式化
     *
     * @param date
     * @param fmt
     * @return
     */
    public String dateFmt(Date date, String fmt) {
        if (date == null) {
            return "";
        }
        try {
            return DateUtil.format(date, fmt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * if判断
     *
     * @param b
     * @param o1
     * @param o2
     * @return
     */
    public Object ifelse(boolean b, Object o1, Object o2) {
        return b ? o1 : o2;
    }

}