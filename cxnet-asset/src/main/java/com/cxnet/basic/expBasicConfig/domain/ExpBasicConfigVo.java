package com.cxnet.basic.expBasicConfig.domain;

import com.cxnet.basic.expModuleConfig.domain.ExpModuleConfig;
import com.cxnet.flow.model.domain.SysModel;
import com.cxnet.rpc.domain.per.PerConfigIndex;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 报销单据配置vo
 *
 * @author caixx
 * @since 2020-09-11 15:13:55
 */
@Data
@ApiModel(description = "报销单据配置vo")
public class ExpBasicConfigVo implements Serializable {
    private static final long serialVersionUID = -88844330610187379L;

    @ApiModelProperty("单据详情")
    private SysModel sysModel;
    @ApiModelProperty("基本信息")
    private List<ExpBasicConfig> expBasicConfigs;
    @ApiModelProperty("费用明细")
    private List<ExpBasicConfigCost> expBasicConfigCosts;
    @ApiModelProperty("附件值集编码")
    private String dictTypeCode;
    @ApiModelProperty("费用标准")
    private List<ExpBasicConfigCost> costStandard;
    @ApiModelProperty("单据模块")
    private List<ExpModuleConfig> blocks;
    @ApiModelProperty("固化指标")
    private List<PerConfigIndex> perConfigIndices;

    @ApiModelProperty("源单位id")
    private String unitId;
    @ApiModelProperty("目标单位id集合")
    private List<String> unitIds;

}