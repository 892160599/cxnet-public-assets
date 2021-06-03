package com.cxnet.asset.astchange.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 资产变更操作记录
 *
 * @author zhaoyi
 * @since 2021-04-25 13:56:33
 */
@Data
@ApiModel(description = "资产变更操作记录")
public class SelectAstAstChangeListVo {

    @ApiModelProperty("变更单号")
    private String astchangeCode;

    @ApiModelProperty("变更类型")
    private String astchangeType;

    @ApiModelProperty("变更前内容")
    private String astchangeBefore;

    @ApiModelProperty("变更后内容")
    private String astchangeAfter;

    @ApiModelProperty("变更日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date verificationDate;

    @ApiModelProperty("变更原因")
    private String verificationReason;

    @ApiModelProperty("创建者")
    public String createBy;
}
