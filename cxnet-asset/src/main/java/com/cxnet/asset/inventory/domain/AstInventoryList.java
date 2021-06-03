package com.cxnet.asset.inventory.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.framework.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 资产盘点子表
 *
 * @author zhangyl
 * @since 2021-04-06 09:34:12
 */
@Data
@ApiModel(description = "资产盘点子表")
public class AstInventoryList extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -73351785230545192L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("卡片编号")
    private String cardId;

    @ApiModelProperty("年度")
    @Excel(name = "年度")
    private String fiscal;

    @ApiModelProperty("单位id")
    private String unitId;

    @ApiModelProperty("单位编码")
    private String unitCode;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("单据号")
    private String billNo;

    @ApiModelProperty("盘点类型")
    @Excel(name = "盘点类型")
    private String checkType;

    @ApiModelProperty("资产大类代码，存放一级1,2,3,4,5,6")
    @Excel(name = "资产大类代码")
    private String classCode;

    @ApiModelProperty("资产大类名称，存放通用设备、专用设备")
    @Excel(name = "资产大类名称")
    private String className;

    @ApiModelProperty("资产编码")
    @Excel(name = "资产编码")
    private String astTypeCode;

    @ApiModelProperty("资产名称")
    @Excel(name = "资产名称")
    private String astTypeName;

    @ApiModelProperty("规格型号")
    @Excel(name = "规格型号")
    private String spec;

    @ApiModelProperty("计量单位")
    @Excel(name = "计量单位")
    private String measurement;

    @ApiModelProperty("购置日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date acquisitionDate;

    @ApiModelProperty("卡片编号")
    private String cardCode;

    @ApiModelProperty("资产名称")
    @Excel(name = "资产名称")
    private String assetName;

    @ApiModelProperty("使用部门编码")
    private String applyDeptCode;

    @ApiModelProperty("使用部门名称")
    private String applyDeptName;

    @ApiModelProperty("使用人编码")
    private String empCode;

    @ApiModelProperty("使用人名称")
    private String empName;

    @ApiModelProperty("使用年限")
    @Excel(name = "使用年限")
    private Integer assetUselife;

    @ApiModelProperty("原值")
    @Excel(name = "原值")
    private BigDecimal cost;

    @ApiModelProperty("数量")
    @Excel(name = "数量")
    private Integer qty;

    @ApiModelProperty("卡片数量")
    private Integer cardQty;

    @ApiModelProperty("卡片原值")
    private BigDecimal cardCost;

    @ApiModelProperty("实盘数量")
    private Integer firmOfferQty;

    @ApiModelProperty("实盘金额")
    private BigDecimal firmOfferMoney;

    @ApiModelProperty("盘亏数量")
    private Integer lossAssetsQty;

    @ApiModelProperty("盘亏金额")
    private BigDecimal lossAssetsMoney;

    @ApiModelProperty("盘点结果")
    private String planResults;

    @ApiModelProperty("存放地点")
    private String placeLocation;

    @ApiModelProperty("盘点方式")
    private String planMode;

    @ApiModelProperty("复核状态")
    private String reviewStatus;

    @ApiModelProperty("是否盘点 1：已盘点  2：未盘点")
    private String isCheck;

    @ApiModelProperty("是否生成处置 1：已生成  2：未生成")
    private String isDispose;

    @ApiModelProperty("单据状态   1 未开始  2 进行中 3 以结束")
    private String status;

    @ApiModelProperty("是否删除   0正常   2停用")
    private String delFlag;

    @ApiModelProperty("盘点说明")
    private String remark;

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

    @ApiModelProperty("部门人数")
    @TableField(exist = false)
    private String deptCount;

    @ApiModelProperty("已盘点人数")
    @TableField(exist = false)
    private String stockedCount;

    @ApiModelProperty("未盘点人数")
    @TableField(exist = false)
    private String counted;

    @TableField(exist = false)
    private List<AstAnnex> astAnnexList;

    @TableField(exist = false)
    private String billId;
}

