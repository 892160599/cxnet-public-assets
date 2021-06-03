package com.cxnet.asset.card.controller;

import static com.cxnet.common.constant.AjaxResult.success;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cxnet.asset.astfield.service.AstFieldService;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.domain.dto.AstCardDTO;
import com.cxnet.asset.card.domain.vo.AstCardVO;
import com.cxnet.asset.card.service.AstCardService;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.constant.BillTypeCodeConstant;
import com.cxnet.common.constant.Convert;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.common.enums.AssertType;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.DateUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.poi.ExcelUtil;
import com.cxnet.framework.aspectj.lang.annotation.BusinessLog;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.service.SysDeptServiceI;
import com.cxnet.project.system.dict.service.SysDictDataServiceI;
import com.cxnet.project.system.rule.service.RuleServiceI;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 资产主表(AstCard)表控制层
 *
 * @author guks
 * @since 2021-03-29 10:23:02
 */
@Api(tags = "资产主表")
@RestController
@RequestMapping("/astCard")
@Slf4j
public class AstCardController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstCardService astCardService;


    @Autowired(required = false)
    private AstFieldService astFieldService;

    @Autowired(required = false)
    private SysDictDataServiceI sysDictDataServiceI;

    /**
     * 注入编号器服务
     */
    @Autowired(required = false)
    private RuleServiceI ruleServiceI;

    @Autowired(required = false)
    private SysDeptServiceI sysDeptServiceI;


    /**
     * 分页查询所有数据
     *
     * @param astCard 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @PreAuthorize("@ss.hasPermi('astCard:astCard:query')")
    @GetMapping
    public AjaxResult selectAll(AstCardDTO astCardDTO) {
        List<AstCard> astCardList = astCardService.getAstCardList(astCardDTO);
        return success(getDataTable(astCardList));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @PreAuthorize("@ss.hasPermi('astCard:astCard:query')")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable String id) {
        return success(astCardService.getAstCardVO(id));
    }

    /**
     * 新增数据
     *
     * @param astCard 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PreAuthorize("@ss.hasPermi('astCard:astCard:insert')")
    @Log(title = "资产主表", businessType = BusinessType.INSERT)
    @PostMapping
    @BusinessLog(module = "资产模块", value = "新增资产卡片", businessType = BusinessType.INSERT,
            operatorTable = "astCard", isRecordKey = true, isBatch = false)
    public AjaxResult insert(@RequestBody AstCardVO astCardVO) {
        return success(astCardService.saveAstCardVo(astCardVO));
    }

    /**
     * 修改数据
     *
     * @param astCard 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PreAuthorize("@ss.hasPermi('astCard:astCard:update')")
    @Log(title = "资产主表", businessType = BusinessType.UPDATE)
    @PutMapping
    @BusinessLog(module = "资产模块", value = "修改资产卡片", businessType = BusinessType.UPDATE,
            operatorTable = "astCard", isRecordKey = true, isBatch = false)
    public AjaxResult update(@RequestBody AstCardVO astCardVO) {
        return success(astCardService.updateAstCardVo(astCardVO));
    }

    /**
     * 删除数据
     *
     * @param ids 主键集合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('astCard:astCard:delete')")
    @Log(title = "资产主表", businessType = BusinessType.DELETE)
    @DeleteMapping
    @BusinessLog(module = "资产模块", value = "删除资产卡片", businessType = BusinessType.DELETE,
            operatorTable = "astCard", isRecordKey = true, isBatch = true)
    public AjaxResult delete(@NotEmpty(message = "至少选中一条要删除的数据！") @RequestBody List<String> ids) {
        List<AstCard> astCardList = astCardService.listByIds(ids);
        if (CollectionUtil.isNotEmpty(astCardList)) {
            astCardList.forEach(entity -> {
                entity.setDelFlag("2");
                entity.setUpdateBy(SecurityUtils.getUsername());
                entity.setUpdateTime(DateUtils.getNowDate());
            });

            astCardService.updateBatchById(astCardList);
        }

        return success(ids);
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('astCard:astCard:print')")
    @GetMapping("/print")
    public AjaxResult print(CommonPrint commonPrint) {
        // 获取帆软路径
        return success(basePrint(commonPrint));
    }

    /**
     * 修改资产主表状态
     *
     * @param ids 主键集合
     * @return
     */
    @ApiOperation("修改资产主表状态")
    @PreAuthorize("@ss.hasPermi('astCard:astCard:status')")
    @PutMapping("/status")
    @BusinessLog(module = "资产模块", value = "资产确认", businessType = BusinessType.CONFIRM,
            operatorTable = "astCard", isRecordKey = true, isBatch = true)
    public AjaxResult changStatus(@RequestBody List<String> ids) {
        List<AstCard> astCards = astCardService.listByIds(ids);
        //获取当前时间
        Date nowDate = DateUtils.getNowDate();
        if (CollectionUtil.isNotEmpty(astCards)) {
            astCards.forEach(entity -> {
                entity.setAstStatus("1");
                entity.setUpdateBy(SecurityUtils.getUsername());
                entity.setUpdateTime(nowDate);
            });
            astCardService.updateBatchById(astCards);
        }
        return success(ids);
    }

    /**
     * 获取使用部门列表
     *
     * @param unitId 单位id
     * @return
     */
    @ApiOperation("获取使用部门列表")
    @GetMapping("/dept")
    public AjaxResult getDepartmentList() {
        return success(astCardService.getDepartmentList());
    }


    /**
     * 资产卡片导出
     *
     * @param unitId 单位id
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @PreAuthorize("@ss.hasPermi('astCard:astCard:export')")
    @ApiOperation("资产卡片导出")
    @GetMapping("/export/excel")
    public AjaxResult export(AstCardDTO astCardDTO) {
        //根据数据库字段动态生成实体
        Class classz = astFieldService.getDynamicClass("2", astCardDTO.getUnitId());
        //获取类中所有属性集合
        Field[] fields = ReflectUtil.getFields(classz);

        //获取id数组
        List<String> ids = astCardDTO.getIds();
        //获取资产列表
        List<AstCard> astCards = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(ids)) {
            astCards = astCardService.listByIds(ids);
        } else {
            astCards = astCardService.getAstCardList(astCardDTO);
        }

        //获取要导出的列表
        List<Object> astCardList = new ArrayList<>(astCards.size());
        for (AstCard astCard : astCards) {
            Object object = ReflectUtil.newInstance(classz);
            BeanUtils.copyProperties(astCard, object);
            convert(object, fields);
            astCardList.add(object);
        }
        ExcelUtil util = new ExcelUtil<>(classz);
        return util.exportExcel(astCardList, "资产卡片导出");
    }


    /**
     * 下载资产卡片模板
     *
     * @param unitId 单位id
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ApiOperation("下载资产卡片模板")
    @PreAuthorize("@ss.hasPermi('astCard:astCard:template')")
    @PostMapping("{unitId}/template")
    public AjaxResult getTemplate(@PathVariable String unitId) {
        //根据数据库字段动态生成实体
        Class classz = astFieldService.getDynamicClass("1", unitId);
        ExcelUtil util = new ExcelUtil<>(classz);
        return util.exportExcel(null, "资产卡片导入模板");
    }

    /**
     * 导入卡片信息
     *
     * @return
     * @throws Exception
     * @throws IOException
     */
    @PreAuthorize("@ss.hasPermi('astCard:astCard:import')")
    @ApiOperation("导入卡片信息")
    @PostMapping("/{unitId}/importExcel")
    public AjaxResult importExcel(MultipartFile file, @PathVariable("unitId") String unitId) throws Exception {
        Class classz = astFieldService.getDynamicClass("1", unitId);
        ExcelUtil<Object> util = new ExcelUtil<Object>(classz);
        List<Object> astCardList = util.importExcel(file.getInputStream());

        //获取单位信息
        SysDept unit = sysDeptServiceI.selectDeptById(unitId);


        //获取类中所有属性集合
        Field[] fields = ReflectUtil.getFields(classz);
        //判断导入数据是否为空
        if (CollectionUtil.isNotEmpty(astCardList)) {
            int length = astCardList.size();
            List<AstCard> astCards = new ArrayList<>(length);

            AstCard astCard = null;
            for (int index = 0; index < length; index++) {
                astCard = new AstCard();
                BeanUtils.copyProperties(astCardList.get(index), astCard);
                check(astCardList.get(index), index, fields);
                reConvert(astCardList.get(index), fields);

                astCard.setId(null);
                //设置卡片状态
                astCard.setAstStatus("0");
                astCard.setTypeName(AssertType.getName(astCard.getTypeCode()));
                //设置初始化状态
                astCard.setInitialState("1");
                astCard.setIsInitImport("2");
                astCard.setDelFlag("0");
                astCard.setCreateTime(DateUtil.date());
                astCard.setUnitId(unitId);
                astCard.setUnitCode(unit.getDeptCode());
                astCard.setUnitName(unit.getDeptName());

                // 设置单据号
                astCard.setAstCode(ruleServiceI.nextNumberByModelCode(BillTypeCodeConstant.AST_REGISTER, astCard));

                astCards.add(astCard);
            }
            astCardService.saveBatch(astCards);
        }
        return success();
    }

    /**
     * 检查导入字段合法性
     *
     * @param obj
     */
    private void check(Object obj, int index, Field[] fields) {
        int line = index + 1;
        try {
            for (Field field : fields) {
                if (null == field) {
                    return;
                }
                field.setAccessible(true);
                //验证属性是否为空
                if (field.getAnnotation(NotBlank.class) != null) {
                    if (null == field.get(obj) || "".equalsIgnoreCase(field.get(obj).toString().trim())) {
                        throw new CustomException("第" + line + "行报错,错误信息是：" + field.getAnnotation(NotBlank.class).message());
                    }
                }

                //验证最大值
                if (field.getAnnotation(Length.class) != null) {
                    if (null != field.get(obj) && field.get(obj).toString().length() > field.getAnnotation(Length.class).max()) {
                        throw new CustomException("第" + line + "行报错,错误信息是字符长度超限制");
                    }
                }

                //验证资产类型是否合法
                if ("typeCode".equalsIgnoreCase(field.getName())) {
                    //获取资产类型的值
                    String typeCode = field.get(obj).toString().trim();
                    if (!StringUtils.equalsAnyIgnoreCase(typeCode, "1", "2", "9")) {
                        throw new CustomException("第" + line + "行报错,错误信息是资产类型不合法");
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将字典值转换成对应的内容
     *
     * @param obj
     */
    private void convert(Object obj, Field[] fields) {
        Map<String, String> sysDictDataMap = null;
        try {
            for (Field field : fields) {
                if (null == field) {
                    continue;
                }
                field.setAccessible(true);

                //获取转换注解
                if (null != field.getAnnotation(Convert.class)) {
                    //获取字典值
                    String key = field.getAnnotation(Convert.class).key();
                    if (StringUtils.isNotBlank(key)) {
                        sysDictDataMap = sysDictDataServiceI.selectDictDataMapByType(key);
                        field.set(obj, sysDictDataMap.get(field.get(obj)));
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将内容转化成对应的字典值
     *
     * @param obj
     */
    private void reConvert(Object obj, Field[] fields) {
        Map<String, String> sysDictDataMap = null;
        try {
            for (Field field : fields) {
                if (null == field) {
                    continue;
                }
                field.setAccessible(true);

                //获取转换注解
                if (null != field.getAnnotation(Convert.class)) {
                    //获取字典值
                    String key = field.getAnnotation(Convert.class).key();
                    if (StringUtils.isNotBlank(key)) {
                        sysDictDataMap = sysDictDataServiceI.selectMapByType(key);
                        if (StringUtils.isNotBlank(sysDictDataMap.get(field.get(obj)))) {
                            field.set(obj, sysDictDataMap.get(field.get(obj)));
                        }
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
