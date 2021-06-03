package com.cxnet.basic.expBasicConfig.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.baseData.assetType.domain.BdAssetType;
import com.cxnet.baseData.assetType.service.BdAssetTypeService;
import com.cxnet.basic.expBasicConfig.domain.ExpBasicConfig;
import com.cxnet.basic.expBasicConfig.domain.ExpBasicConfigVo;
import com.cxnet.basic.expBasicConfig.service.ExpBasicConfigService;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.rpc.domain.asset.AstCardStyle;
import com.cxnet.rpc.service.asset.AstCardStyleServiceRpc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 报销单据配置(ExpBasicConfig)表控制层
 *
 * @author caixx
 * @since 2020-09-11 15:13:54
 */
@Slf4j
@Api(tags = "报销单据配置")
@RestController
@RequestMapping("/expBasicConfig")
public class ExpBasicConfigController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private ExpBasicConfigService expBasicConfigService;

    @Autowired
    private AstCardStyleServiceRpc astCardStyleServiceRpc;

    @Autowired
    private BdAssetTypeService bdAssetTypeService;

    /**
     * 查询报销单据tree
     *
     * @param expBasicConfig 查询实体
     * @return tree
     */
    @ApiOperation("查询报销单据tree")
    @PreAuthorize("@ss.hasPermi('expBasicConfig:expBasicConfig:query')")
    @GetMapping("/tree")
    public AjaxResult selectTree(ExpBasicConfig expBasicConfig) {
        List<Zone> list = expBasicConfigService.selectTree(expBasicConfig);
        return success(list);
    }

    /**
     * 通过单据modeCode查询所有配置信息
     *
     * @param modelCode 单据id
     * @return 单条数据
     */
    @ApiOperation("通过单据modeCode查询所有配置信息")
    @GetMapping("/{modelCode}")
    public AjaxResult selectOne(@PathVariable String modelCode, @RequestParam("unitId") String unitId) {
        String defaultUnitId = "0".equals(unitId.trim()) ? "*" : "0";
        return success(expBasicConfigService.getExpBasicConfigByModelCode(modelCode, null, unitId, defaultUnitId));
    }

    /**
     * 通过单据modeCode和单据配置code查询所有配置信息
     *
     * @param modelCode   单据id
     * @param cardStyleId 单据配置id
     * @return 单条数据
     */
    @ApiOperation("通过单据modeCode和单据配置code查询所有配置信息")
    @GetMapping("/{modelCode}/{cardStyleId}")
    public AjaxResult getConfigByModelCodeAndCardStyleId(@PathVariable("modelCode") String modelCode,
                                                         @PathVariable("cardStyleId") String cardStyleId, @RequestParam("unitId") String unitId) {
        return success(expBasicConfigService.getExpBasicConfigByModelCode(modelCode, cardStyleId, unitId, "*"));
    }

    /**
     * 根据单位id、单据配置code以及资产类别Id查询所有配置信息
     *
     * @param modelCode 单据id
     * @param unitId    单位id
     * @param assetCode 资产编码
     * @param assetType 资产类型
     * @return
     */
    @ApiOperation("根据条件查询所有资产配置信息")
    @GetMapping("/{modelCode}/{unitId}/asset")
    public AjaxResult getConfigByUnitIdAndModelCodeAndAssetCode(@PathVariable("modelCode") String modelCode,
                                                                @PathVariable("unitId") String unitId, @RequestParam("assetCode") String assetCode, @RequestParam("assetType") String assetType) {
        ExpBasicConfigVo expBasicConfigVo = null;
        AstCardStyle astCardStyle = null;
        BdAssetType bdAssetType = null;

        while (null == astCardStyle && StringUtils.isNotBlank(assetCode)) {
            //根据资产类别编码获取样式
            astCardStyle = astCardStyleServiceRpc.getAstCardStyleByUnitIdAndAssetCode(unitId, assetCode, assetType);

            //查询资产类别信息
            QueryWrapper<BdAssetType> qw = new QueryWrapper<>();
            qw.lambda().eq(BdAssetType::getUnitId, unitId).eq(BdAssetType::getAssetCode, assetCode);
            bdAssetType = bdAssetTypeService.getOne(qw);
            assetCode = (null == bdAssetType) ? null : bdAssetType.getParentCode();
        }

        // 如果当前单位下不存在模板样式配置的话，查找全部单位下的模板样式配置
        if (null == astCardStyle) {
            unitId = "0";
            astCardStyle = astCardStyleServiceRpc.getAstCardStyleByUnitIdAndAssetCode("0", null, assetType);
        }


        expBasicConfigVo = expBasicConfigService.getExpBasicConfigByModelCode(modelCode, astCardStyle.getId(), unitId, "*");
        return success(expBasicConfigVo);
    }

    /**
     * 通过单据modeCode恢复默认配置
     *
     * @param modelCode 单据code
     */
    @ApiOperation("通过单据modeCode恢复默认配置")
    @GetMapping("reSet/{modelCode}")
    public AjaxResult reSet(@PathVariable String modelCode, @RequestParam("unitId") String unitId) {
        String defaultUnitId = "0".equals(unitId.trim()) ? "*" : "0";
        return success(expBasicConfigService.reSet(modelCode, unitId, defaultUnitId));
    }

    /**
     * 将一个单位的单据配置，同步到其他多个单位
     *
     * @param modelCode     单据code
     * @param basicConfigVo 参数
     * @return 结果
     */
    @ApiOperation("通过单据modeCode恢复默认配置")
    @PutMapping("syncBill/{modelCode}")
    public AjaxResult syncBill(@PathVariable String modelCode, ExpBasicConfigVo basicConfigVo) {
        String defaultUnitId = "0".equals(basicConfigVo.getUnitId().trim()) ? "*" : "0";
        return success(expBasicConfigService.syncBill(modelCode, basicConfigVo.getUnitId(), defaultUnitId, basicConfigVo.getUnitIds()));
    }

    /**
     * 修改数据
     *
     * @param expBasicConfigVo 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PreAuthorize("@ss.hasPermi('expBasicConfig:expBasicConfig:update')")
    @Log(title = "报销单据配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody ExpBasicConfigVo expBasicConfigVo) {
        if (ObjectUtil.isNull(expBasicConfigVo.getSysModel())
                || StringUtils.isEmpty(expBasicConfigVo.getSysModel().getModelId())) {
            throw new CustomException("请选择单据！");
        }
        return success(expBasicConfigService.updateByExpBasicConfigVo(expBasicConfigVo));
    }

}