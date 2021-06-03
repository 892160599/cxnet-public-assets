package com.cxnet.asset.assetcardstyle.controller;

import static com.cxnet.common.constant.AjaxResult.success;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.asset.assetcardstyle.service.AstCardStyleService;
import com.cxnet.asset.astcardstyletype.domain.AstCardStyleType;
import com.cxnet.asset.astcardstyletype.service.AstCardStyleTypeService;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.constant.Constants;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.DateUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.common.utils.tree.ZoneUtils;
import com.cxnet.flow.model.domain.SysModel;
import com.cxnet.flow.model.service.SysModelServiceI;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.rpc.domain.asset.AstCardStyle;
import com.cxnet.rpc.service.expense.ExpBasicConfigServiceRpc;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 单据样式配置表(AstCardStyle)表控制层
 *
 * @author guks
 * @since 2021-03-24 18:28:43
 */
@Api(tags = "单据样式配置")
@RestController
@RequestMapping("/asset/cardstyle")
public class AstCardStyleController extends BaseController {

    /**
     * 资产卡片样式服务类
     */
    @Resource
    private AstCardStyleService astCardStyleService;

    /**
     * 系统模块服务类
     */
    @Autowired(required = false)
    private SysModelServiceI sysModelService;

    /**
     * 单据配置服务类
     */
    @Autowired(required = false)
    private ExpBasicConfigServiceRpc expBasicConfigServiceRpc;

    /**
     * 样式管理资产类别服务类
     */
    @Autowired(required = false)
    private AstCardStyleTypeService astCardStyleTypeService;


    /**
     * 单据类型
     */
    public static final String BILL_TYPE = "BILL_TYPE";

    /**
     * 卡片编码
     */
    public static final String CARD_STYLE_CODE = "CARD_STYLE_CODE";


    /**
     * 固定资产模板id
     */
    public static final String GD_TEMPALTE_ID = "10001";


    /**
     * 通过单位id和资产编码查询
     *
     * @param unitId    单位id
     * @param assetCode 资产编码
     * @return 资产卡片列表
     */
    @ApiOperation("通过单位id和资产编码查询")
    @GetMapping("/getUnitid")
    public AjaxResult getUnitid(@RequestParam("unitId") String unitId, @RequestParam("assetCode") String assetCode) {
        QueryWrapper<AstCardStyleType> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AstCardStyleType::getUnitId, unitId).eq(AstCardStyleType::getAssetCode, assetCode);

        //获取卡片样式管理类
        AstCardStyleType astCardStyleType = astCardStyleTypeService.getOne(queryWrapper);

        AstCardStyle astCardStyle = null;
        if (null != astCardStyleType) {
            QueryWrapper<AstCardStyle> qw = new QueryWrapper<>();
            qw.lambda().eq(AstCardStyle::getId, astCardStyleType.getCardStyleId()).eq(AstCardStyle::getDelFlag, Constants.YES)
                    .eq(AstCardStyle::getStatus, Constants.YES);
            astCardStyle = astCardStyleService.getOne(qw);
        }
        return success(astCardStyle);
    }


    /**
     * 查询资产管理tree
     *
     * @param modelId 模块id
     * @return 资产管理树
     */
    @ApiOperation("查询资产管理tree")
    @GetMapping("/tree")
    public AjaxResult treeSysModel() {
        List<Zone> list = sysModelService.selectSysModelTree(new SysModel());
        return success(ZoneUtils.buildTreeById(list, "10"));
    }

    /**
     * 分页查询所有数据
     *
     * @param astCardStyle 查询实体
     * @return 资产卡片列表
     */
    @ApiOperation("分页查询所有数据")
    @GetMapping()
    public AjaxResult selectAll(AstCardStyle astCardStyle) {
        startPage();
        QueryWrapper<AstCardStyle> qw = new QueryWrapper<>(astCardStyle);
        //判断单位id是否存在，不存在的话，查询默认单位
        if (StringUtils.isEmpty(astCardStyle.getUnitId())) {
            astCardStyle.setUnitId(Constants.DEFAULT_UNIT_ID);
        }
        astCardStyle.setStatus(Constants.YES);
        astCardStyle.setDelFlag(Constants.YES);
        //根据单据类型、卡片编码排序
        qw.orderByAsc(BILL_TYPE, CARD_STYLE_CODE);
        List<AstCardStyle> list = astCardStyleService.list(qw);
        return success(getDataTable(list));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("/{id}")
    public AjaxResult selectOne(@PathVariable Serializable id) {
        return success(astCardStyleService.getById(id));
    }

    /**
     * 新增卡片样式数据
     *
     * @param astCardStyle 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @Log(title = "单据样式配置表", businessType = BusinessType.INSERT)
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult insert(@RequestBody AstCardStyle astCardStyle) {
        //验证合法性
        check(astCardStyle);
        astCardStyle.setId(null);
        astCardStyle.setDelFlag(Constants.YES);
        astCardStyle.setStatus(Constants.YES);
        astCardStyle.setIsPreset(Constants.NO);
        astCardStyleService.save(astCardStyle);

        //获取模板id
        String templateAstCardStyleId = getTemplateCardStyleId(astCardStyle);

        //设置单据样式
        expBasicConfigServiceRpc.copyByCardStyleIdCodeAndUnitId(astCardStyle.getId(), astCardStyle.getUnitId(), "0", templateAstCardStyleId);
        return success();
    }

    /**
     * 修改数据
     *
     * @param astCardStyle 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @Log(title = "单据样式配置表", businessType = BusinessType.UPDATE)
    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult update(@RequestBody AstCardStyle astCardStyle) {
        check(astCardStyle);
        astCardStyleService.updateBathbyBillType(astCardStyle.getBillType(), astCardStyle.getUnitId());
        return success(astCardStyleService.updateById(astCardStyle));
    }

    /**
     * 删除数据
     *
     * @param ids 主键集合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @Log(title = "单据样式配置表", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@NotEmpty(message = "至少选中一条要删除的数据！") @RequestBody List<String> ids) {
        return success(astCardStyleService.removeByIds(ids));
    }

    /**
     * 删除单条数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @Log(title = "单据样式配置表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult deleteById(@NotEmpty(message = "请选择单据") @PathVariable("id") String id) {
        AstCardStyle astCardStyle = astCardStyleService.getById(id);
        astCardStyle.setDelFlag("2");
        return success(astCardStyleService.updateById(astCardStyle));
    }


    /**
     * 复制系统级单据到当前单位下
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("复制系统级单据到当前单位下")
    @Log(title = "单据样式配置表", businessType = BusinessType.INSERT)
    @PostMapping("copy/{unitId}")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult copy(@PathVariable("unitId") String unitId, @NotEmpty(message = "至少选中一条要复制的数据！") @RequestBody List<String> ids) {
        List<AstCardStyle> astCardStyleList = astCardStyleService.listByIds(ids);
        if (CollectionUtil.isNotEmpty(astCardStyleList)) {
            Map<String, Object> columnMap = new HashMap<>(4);
            columnMap.put("UNIT_ID", unitId);

            astCardStyleList.forEach(entity -> {
                columnMap.put("CARD_STYLE_CODE", entity.getCardStyleCode());
                columnMap.put("BILL_TYPE", entity.getBillType());
                columnMap.put("DEL_FLAG", "0");

                if (CollectionUtil.isNotEmpty(astCardStyleService.listByMap(columnMap))) {
                    throw new CustomException("当前单位下已经存在要复制的单据样式");
                }
            });
            astCardStyleList.forEach(entity -> {
                entity.setId(null);
                entity.setUnitId(unitId);
                entity.setIsDefault("2");
                entity.setIsPreset("2");
                entity.setUpdateBy(SecurityUtils.getUsername());
                entity.setUpdateTime(DateUtils.getNowDate());
            });

            astCardStyleService.saveBatch(astCardStyleList);
            //拷贝资产类别
            List<AstCardStyleType> astCardStyleTypeList = null;
            for (String astCardStyleId : ids) {
                //获取资产类别
                QueryWrapper<AstCardStyleType> qw = new QueryWrapper<>();
                qw.lambda().eq(AstCardStyleType::getCardStyleId, astCardStyleId);
                astCardStyleTypeList = astCardStyleTypeService.list(qw);

                //设置资产类别
                if (CollectionUtil.isNotEmpty(astCardStyleTypeList)) {
                    astCardStyleTypeList.forEach(entity -> {
                        entity.setId(null);
                        entity.setUnitId(unitId);
                        entity.setUpdateBy(SecurityUtils.getUsername());
                        entity.setUpdateTime(DateUtils.getNowDate());
                    });
                    astCardStyleTypeService.saveBatch(astCardStyleTypeList);
                }
            }

            //获取模板id
            String templateAstCardStyleId = getTemplateCardStyleId(astCardStyleList.get(0));
            astCardStyleList.forEach(entity -> {
                expBasicConfigServiceRpc.copyByCardStyleIdCodeAndUnitId(entity.getId(), unitId, "0", templateAstCardStyleId);
            });

        }
        return success();
    }


    /**
     * 获取单据下的默认样式
     *
     * @param modelCode 单据code
     * @param unitId    单位编码
     * @return
     */
    @ApiOperation("获取单据下的默认样式")
    @GetMapping("/{modelCode}/{unitId}")
    public AjaxResult getDefaultStyleInModel(@PathVariable("modelCode") String modelCode, @PathVariable("unitId") String unitId) {
        return success(astCardStyleService.getDefaultStyleInModel(modelCode, unitId));
    }


    /**
     * 检查卡片样式对象是否符合要求
     *
     * @param astCardStyle 卡片样式对象
     * @return
     */
    private void check(AstCardStyle astCardStyle) {
        if (StringUtils.isEmpty(astCardStyle.getCardStyleCode())) {
            throw new CustomException("样式编码不可以为空");
        }

        if (checkCardStyleCode(astCardStyle)) {
            throw new CustomException("样式编码不可以重复");
        }


        if (StringUtils.isEmpty(astCardStyle.getCardStyleName())) {
            throw new CustomException("样式名称不可以为空");
        }

        if (StringUtils.isEmpty(astCardStyle.getFiscal())) {
            throw new CustomException("缺少业务年度！");
        }

        if (StringUtils.isEmpty(astCardStyle.getBillType())) {
            throw new CustomException("单据类型不可以为空");
        }

        if (StringUtils.isEmpty(astCardStyle.getUnitId())) {
            astCardStyle.setUnitId(Constants.DEFAULT_UNIT_ID);
        }
    }


    /**
     * 检查单据样式编码是否重复
     *
     * @param astCardStyle 卡片样式对象
     * @return
     */
    private boolean checkCardStyleCode(AstCardStyle astCardStyle) {
        QueryWrapper<AstCardStyle> qw = new QueryWrapper<>();
        qw.lambda().eq(AstCardStyle::getCardStyleCode, astCardStyle.getCardStyleCode())
                .eq(AstCardStyle::getBillType, astCardStyle.getBillType())
                .eq(AstCardStyle::getDelFlag, Constants.YES)
                .eq(AstCardStyle::getUnitId, astCardStyle.getUnitId())
                .ne(!StringUtils.isEmpty(astCardStyle.getId()), AstCardStyle::getId, astCardStyle.getId());
        return !CollectionUtil.isEmpty(astCardStyleService.list(qw));
    }

    /**
     * 获取模板id
     *
     * @param astCardStyle 卡片样式对象
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private String getTemplateCardStyleId(AstCardStyle astCardStyle) {
        String defaultCardStyleCode = GD_TEMPALTE_ID;
        QueryWrapper<AstCardStyle> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(AstCardStyle::getBillType, astCardStyle.getBillType())
                .eq(AstCardStyle::getCardStyleCode, defaultCardStyleCode)
                .eq(AstCardStyle::getDelFlag, Constants.YES)
                .eq(AstCardStyle::getUnitId, Constants.YES)
                .eq(AstCardStyle::getIsPreset, Constants.YES);
        AstCardStyle templateAstCardStyle = astCardStyleService.getOne(queryWrapper);
        return templateAstCardStyle == null ? "" : templateAstCardStyle.getId();
    }


}