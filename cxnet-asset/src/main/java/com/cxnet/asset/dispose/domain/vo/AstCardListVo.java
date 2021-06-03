package com.cxnet.asset.dispose.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 卡片列表信息
 *
 * @author zhaoyi
 * @since 2021-03-25 13:56:33
 */
@Data
@ApiModel("卡片列表信息")
public class AstCardListVo {

    @ApiModelProperty("单据Id")
    private String disposeId;

    @ApiModelProperty("资产类别")
    private String disposetypeCode;

    @ApiModelProperty("单位信息")
    private String unitId;

    @ApiModelProperty("部门信息")
    private String deptId;

    @ApiModelProperty("列表里的卡片主键")
    private List<String> cardIds;

    @ApiModelProperty("模糊查询")
    private String param;
}
