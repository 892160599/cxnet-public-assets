package com.cxnet.asset.empchange.domain;

import java.util.Date;

import com.cxnet.framework.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 资产使用人变动明细表
 *
 * @author zhaoyi
 * @since 2021-04-16 14:28:31
 */
@Data
@ApiModel(description = "资产使用人变动明细表")
public class AstEmpchangeList implements Serializable {
    private static final long serialVersionUID = -14609306910426426L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("单据号")
    private String billId;

    @ApiModelProperty("资产卡片id")
    private String astId;

    @ApiModelProperty("卡片编号")
    private String astCode;

    @ApiModelProperty("资产类别编码")
    private String categoryCode;

    @ApiModelProperty("资产类别名称")
    private String categoryName;

    @ApiModelProperty("资产名称")
    private String assetName;

    @ApiModelProperty("价值")
    private Integer cost;

    @ApiModelProperty("数量")
    private Integer qty;

    @ApiModelProperty("存放地点代码")
    private String placeCode;

    @ApiModelProperty("存放地点")
    private String placeName;

    @ApiModelProperty("购置日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date acquisitionDate;

    @ApiModelProperty("品牌")
    private String brand;

    @ApiModelProperty("规格型号")
    private String spec;

    @ApiModelProperty("变更前使用部门id")
    private String departmentBeforeId;

    @ApiModelProperty("变更前使用部门代码")
    private String departmentBeforeCode;

    @ApiModelProperty("变更前使用部门名称")
    private String departmentBeforeName;

    @ApiModelProperty("变更后使用部门id")
    private String departmentAfterId;

    @ApiModelProperty("变更后使用部门代码")
    private String departmentAfterCode;

    @ApiModelProperty("变更后使用部门名称")
    private String departmentAfterName;

    @ApiModelProperty("变更前使用人id")
    private String empBeforeId;

    @ApiModelProperty("变更前使用人代码")
    private String empBeforeCode;

    @ApiModelProperty("变更前使用人名称")
    private String empBeforeName;

    @ApiModelProperty("变更后使用人id")
    private String empAfterId;

    @ApiModelProperty("变更后使用人代码")
    private String empAfterCode;

    @ApiModelProperty("变更后使用人名称")
    private String empAfterName;

}

