package com.cxnet.rpc.domain;

import com.alibaba.fastjson.JSON;
import com.cxnet.common.utils.poi.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购单采购明细实体 pur_purchase_item
 *
 * @author caixx
 * @date 2020-07-23
 */
@Data
@ApiModel("采购单采购明细")
public class PurPurchaseItem extends RpcBaseEntity {


    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;
    /**
     * 采购单id
     */
    @ApiModelProperty("采购单id")
    private String purId;
    /**
     * 采购品目id
     */
    @ApiModelProperty("采购品目id")
    private String itemId;
    /**
     * 采购品目代码
     */
    @Excel(name = "采购品目代码")
    @ApiModelProperty("采购品目代码")
    private String itemCode;
    /**
     * 采购品目名称
     */
    @Excel(name = "采购品目名称")
    @ApiModelProperty("采购品目名称")
    private String itemName;
    /**
     * 资产类别代码
     */
    @Excel(name = "资产类别代码")
    @ApiModelProperty("资产类别代码")
    private String assetCode;
    /**
     * 资产类别名称
     */
    @Excel(name = "资产类别名称")
    @ApiModelProperty("资产类别名称")
    private String assetName;
    /**
     * 主要技术参数和要求
     */
    @Excel(name = "主要技术参数和要求")
    @ApiModelProperty("主要技术参数和要求")
    private String specs;
    /**
     * 采购数量
     */
    @Excel(name = "采购数量")
    @ApiModelProperty("采购数量")
    private Long purQty;
    /**
     * 采购单价
     */
    @Excel(name = "采购单价")
    @ApiModelProperty("采购单价")
    private BigDecimal purPrice;
    /**
     * 计量单位
     */
    @Excel(name = "计量单位")
    @ApiModelProperty("计量单位")
    private String measurement;
    /**
     * 资金合计
     */
    @Excel(name = "资金合计")
    @ApiModelProperty("资金合计")
    private BigDecimal totalFunds;
    /**
     * 现存量
     */
    @Excel(name = "现存量")
    @ApiModelProperty("现存量")
    private Long stock;
    /**
     * 购置用途
     */
    @Excel(name = "购置用途")
    @ApiModelProperty("购置用途")
    private String purpost;
    /**
     * 采购品名称
     */
    @Excel(name = "采购品名称")
    @ApiModelProperty("采购品名称")
    private String curItemName;
    /**
     * 资产类别id
     */
    @Excel(name = "资产类别id")
    @ApiModelProperty("资产类别id")
    private Long assetId;
    /**
     * 中标金额
     */
    @Excel(name = "中标金额")
    @ApiModelProperty("中标金额")
    private BigDecimal winningMoney;
    /**
     * 中标日期
     */
    @Excel(name = "中标日期", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("中标日期")
    private Date winningDate;
    /**
     * 中标供应商
     */
    @Excel(name = "中标供应商")
    @ApiModelProperty("中标供应商")
    private String winningSupplier;
    /**
     * 采购计划号
     */
    @Excel(name = "采购计划号")
    @ApiModelProperty("采购计划号")
    private String curPlanNum;
    /**
     * 采购合同编号
     */
    @Excel(name = "采购合同编号")
    @ApiModelProperty("采购合同编号")
    private String curConNum;

    @ApiModelProperty("备用字段1")
    private String expExtend1;
    @ApiModelProperty("备用字段2")
    private String expExtend2;
    @ApiModelProperty("备用字段3")
    private String expExtend3;
    @ApiModelProperty("备用字段4")
    private String expExtend4;
    @ApiModelProperty("备用字段5")
    private String expExtend5;


    public String toJSONString() {
        return JSON.toJSONString(this);
    }

}
