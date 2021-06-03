package com.cxnet.asset.astchange.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 卡片明细信息
 *
 * @author zhaoyi
 * @since 2021-04-23 10:06:14
 */
@Data
@ApiModel("卡片明细信息")
public class AstChangeListVo {

    @ApiModelProperty("单据Id")
    private String astchangeId;

    @ApiModelProperty("资产类别编码")
    private String categoryCode;

    @ApiModelProperty("单位信息")
    private String unitId;

    @ApiModelProperty("部门信息")
    private String deptId;

    @ApiModelProperty("模糊查询")
    private String param;

    @ApiModelProperty("列表里的卡片主键")
    private List<String> cardIds;
}
