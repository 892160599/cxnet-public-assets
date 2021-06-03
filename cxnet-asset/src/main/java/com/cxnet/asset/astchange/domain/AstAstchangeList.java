package com.cxnet.asset.astchange.domain;

import java.math.BigDecimal;
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
 * 资产变动明细表
 *
 * @author zhaoyi
 * @since 2021-04-23 17:10:40
 */
@Data
@ApiModel(description = "资产变动明细表")
public class AstAstchangeList implements Serializable {
    private static final long serialVersionUID = -86528186314246731L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("单据号")
    private String billId;

    @ApiModelProperty("资产卡片id")
    private String astId;

    @ApiModelProperty("卡片编号")
    private String astCode;

    @ApiModelProperty("资产名称")
    private String assetName;

    @ApiModelProperty("数量")
    private Integer qty;

    @ApiModelProperty("购置日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date acquisitionDate;

    @ApiModelProperty("品牌")
    private String brand;

    @ApiModelProperty("规格型号")
    private String spec;

    @ApiModelProperty("变更前原值")
    private BigDecimal costBefore;

    @ApiModelProperty("变更后原值")
    private BigDecimal costAfter;

    @ApiModelProperty("变更前累计折旧")
    private BigDecimal depBeforeTotal;

    @ApiModelProperty("变更后累计折旧")
    private BigDecimal depAfterTotal;

    @ApiModelProperty("变更前取得日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date addBeforeDate;

    @ApiModelProperty("变更后取得日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date addAfterDate;

    @ApiModelProperty("变更前折旧方法")
    private String depBeforeMethod;

    @ApiModelProperty("变更后折旧方法")
    private String depAfterMethod;

    @ApiModelProperty("变更前设计使用月份")
    private Integer useBeforeLife;

    @ApiModelProperty("变更后设计使用月份")
    private Integer useAfterLife;

    @ApiModelProperty("变更前资产类别编码")
    private String categoryBeforeCode;

    @ApiModelProperty("变更前资产类别名称")
    private String categoryBeforeName;

    @ApiModelProperty("变更后资产类别编码")
    private String categoryAfterCode;

    @ApiModelProperty("变更后资产类别名称")
    private String categoryAfterName;

    @ApiModelProperty("变更前存放地点代码")
    private String placeBeforeCode;

    @ApiModelProperty("变更前存放地点")
    private String placeBeforeName;

    @ApiModelProperty("变更后存放地点代码")
    private String placeAfterCode;

    @ApiModelProperty("变更后存放地点")
    private String placeAfterName;

    @ApiModelProperty("扩展字段1")
    private String extend1;

    @ApiModelProperty("扩展字段2")
    private String extend2;

    @ApiModelProperty("扩展字段3")
    private String extend3;

    @ApiModelProperty("扩展字段4")
    private String extend4;

    @ApiModelProperty("扩展字段5")
    private String extend5;

    @ApiModelProperty("扩展字段6")
    private String extend6;

    @ApiModelProperty("变更前资产类别id")
    private String categoryBeforeId;

    @ApiModelProperty("变更后资产类别id")
    private String categoryAfterId;

    @ApiModelProperty("变更前存放地点id")
    private String placeBeforeId;

    @ApiModelProperty("变更后存放地点id")
    private String placeAfterId;

}

