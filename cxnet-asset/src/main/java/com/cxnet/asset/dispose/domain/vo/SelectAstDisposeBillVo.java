package com.cxnet.asset.dispose.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 资产操作记录
 *
 * @author zhaoyi
 * @since 2021-03-25 13:56:33
 */
@Data
@ApiModel(description = "资产操作记录")
public class SelectAstDisposeBillVo {

    @ApiModelProperty("处置单号")
    private String disposeCode;

    @ApiModelProperty("处置方式")
    private String disposetypeCode;

    @ApiModelProperty("处置日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date verificationDate;

    @ApiModelProperty("创建者")
    public String createBy;
}
