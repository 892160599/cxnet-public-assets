package com.cxnet.asset.card.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.cxnet.asset.astfield.service.AstFieldService;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.domain.AstOpeningJournal;
import com.cxnet.asset.card.service.AstCardService;
import com.cxnet.asset.card.service.AstOpeningJournalService;
import com.cxnet.common.constant.BillTypeCodeConstant;
import com.cxnet.common.constant.Convert;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.ServletUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.poi.ExcelUtil;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.service.SysDeptServiceI;
import com.cxnet.project.system.dict.service.SysDictDataServiceI;
import com.cxnet.project.system.rule.service.RuleServiceI;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.asset.card.domain.AstOpeningCard;
import com.cxnet.asset.card.service.AstOpeningCardService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 期初卡片导入记录表(AstOpeningCard)表控制层
 *
 * @author zhangyl
 * @since 2021-04-22 16:29:00
 */
@Slf4j
@Api(tags = "期初卡片导入记录表")
@RestController
@RequestMapping("/astOpeningCard")
public class AstOpeningCardController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstOpeningCardService astOpeningCardService;
    @Resource
    private AstCardService astCardService;
    @Resource
    private AstOpeningJournalService astOpeningJournalService;
    @Autowired(required = false)
    private AstFieldService astFieldService;
    @Autowired(required = false)
    private SysDeptServiceI sysDeptServiceI;
    @Autowired(required = false)
    private SysDictDataServiceI sysDictDataServiceI;
    @Autowired(required = false)
    private RuleServiceI ruleServiceI;

    /**
     * 分页查询所有数据
     *
     * @param astOpeningCard 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @PreAuthorize("@ss.hasPermi('astOpeningCard:astOpeningCard:query')")
    @GetMapping
    public AjaxResult selectAll(AstOpeningCard astOpeningCard) {
        startPage();
        QueryWrapper<AstOpeningCard> qw = new QueryWrapper<>(astOpeningCard);
        qw.lambda().eq(AstOpeningCard::getDelFlag, "0");
        List<AstOpeningCard> list = astOpeningCardService.list(qw);
        return success(getDataTable(list));
    }

    /**
     * 通过主键查询日志信息
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询日志信息")
    @PreAuthorize("@ss.hasPermi('astOpeningCard:astOpeningCard:query')")
    @GetMapping("/selectOne")
    public AjaxResult selectOne(String id) {
        List<AstOpeningJournal> list = astOpeningCardService.getList(id);
        return success(list);
    }

    /**
     * 导入
     *
     * @param bugProjectLibraries
     * @return
     */
    @ApiOperation("导入")
    @PreAuthorize("@ss.hasPermi('astOpeningCard:astOpeningCard:import')")
    @PostMapping("/{unitId}/import")
    public AjaxResult projectLibraryImp(MultipartFile file, @PathVariable("unitId") String unitId) throws Exception {
        AstOpeningCard card = new AstOpeningCard();
        Class classz = astFieldService.getDynamicClass("1", unitId);
        ExcelUtil<Object> util = new ExcelUtil<Object>(classz);
        List<Object> astCardList = util.importExcel(file.getInputStream());
        //获取单位信息
        SysDept unit = sysDeptServiceI.selectDeptById(unitId);
        //获取类中所有属性集合
        Field[] fields = ReflectUtil.getFields(classz.getClass());
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
            card = astOpeningCardService.insertCardList(astCards);
        }
        return success(card);
    }

    /**
     * 导出
     *
     * @param bugProjectLibraries
     * @return
     */
    @ApiOperation("导出")
    @PreAuthorize("@ss.hasPermi('astOpeningCard:astOpeningCard:export')")
    @GetMapping("/export")
    public AjaxResult projectLibraryExport(AstOpeningCard astOpeningCard) {
        //根据数据库字段动态生成实体
        Class classz = astFieldService.getDynamicClass("2", astOpeningCard.getUnitId());
        QueryWrapper<AstOpeningJournal> wrapper = new QueryWrapper<>();
        //根据操作记录id查询操作记录表
        wrapper.lambda().eq(AstOpeningJournal::getOpeningId, astOpeningCard.getId());
        List<AstOpeningJournal> list = astOpeningJournalService.list(wrapper);
        //获取要导出的列表
        List<Object> cardList = new ArrayList<>();
        for (AstOpeningJournal v : list) {
            QueryWrapper<AstCard> cardWrapper = new QueryWrapper<>();
            cardWrapper.lambda().eq(AstCard::getId, v.getAstId());
            AstCard one = astCardService.getOne(cardWrapper);
            if (ObjectUtil.isNotNull(one)) {
                Object object = ReflectUtil.newInstance(classz);
                BeanUtils.copyProperties(one, object);
                cardList.add(object);
            }
        }
        ExcelUtil util = new ExcelUtil<>(classz);
        return util.exportExcel(cardList, "资产卡片导出");
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('astOpeningCard:astOpeningCard:delete')")
    @Log(title = "期初卡片导入记录表", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody List<String> ids) {
        ids.forEach(v -> {
            QueryWrapper<AstOpeningCard> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(AstOpeningCard::getId, v);
            AstOpeningCard one = astOpeningCardService.getOne(wrapper);
            one.setDelFlag("2");
            astOpeningCardService.updateById(one);
        });
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

