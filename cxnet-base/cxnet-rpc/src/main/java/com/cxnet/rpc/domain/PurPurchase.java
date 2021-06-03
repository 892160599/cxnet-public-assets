package com.cxnet.rpc.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.rpc.domain.budget.BugPlanRpc;
import com.cxnet.rpc.domain.expense.ExpApplyBorrowListVo;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;

/**
 * 采购单实体 pur_purchase
 *
 * @author caixx
 * @date 2020-07-23
 */
@Data
@ApiModel("采购单")
public class PurPurchase extends RpcBaseEntity {

    /**
     * 采购单id
     */
    @ApiModelProperty("采购单id")
    private String purId;

    @ApiModelProperty("单位id")
    private String unitId;
    /**
     * 单位名称
     */
    @Excel(name = "单位名称")
    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("采购部门id")
    private String deptId;
    /**
     * 采购部门编码
     */
    @Excel(name = "采购部门编码")
    @ApiModelProperty("采购部门编码")
    private String deptCode;
    /**
     * 采购部门名称
     */
    @Excel(name = "采购部门名称")
    @ApiModelProperty("采购部门名称")
    private String deptName;
    /**
     * 申报日期
     */
    @Excel(name = "申报日期", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("申报日期")
    private Date applyDate;
    /**
     * 采购组织形式
     */
    @Excel(name = "采购组织形式")
    @ApiModelProperty("采购组织形式")
    private String purForm;
    /**
     * 采购方式
     */
    @Excel(name = "采购方式")
    @ApiModelProperty("采购方式")
    private String purWay;
    /**
     * 采购类型
     */
    @Excel(name = "采购类型")
    @ApiModelProperty("采购类型")
    private String purType;
    /**
     * 采购内容
     */
    @Excel(name = "采购内容")
    @ApiModelProperty("采购内容")
    private String purContent;
    /**
     * 资金合计
     */
    @Excel(name = "资金合计")
    @ApiModelProperty("资金合计")
    private BigDecimal totalFunds;
    /**
     * 采购代理机构
     */
    @Excel(name = "采购代理机构")
    @ApiModelProperty("采购代理机构")
    private String purOrg;
    /**
     * 采购联系人
     */
    @Excel(name = "采购联系人")
    @ApiModelProperty("采购联系人")
    private String purContacts;
    /**
     * 联系电话
     */
    @Excel(name = "联系电话")
    @ApiModelProperty("联系电话")
    private String phone;
    /**
     * 申请理由
     */
    @Excel(name = "申请理由")
    @ApiModelProperty("申请理由")
    private String applyReason;
    /**
     * 流程实例ID
     */
    @Excel(name = "流程实例ID")
    @ApiModelProperty("流程实例ID")
    private String processinstid;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;
    /**
     * 单据编码
     */
    @Excel(name = "单据编码")
    @ApiModelProperty("单据编码")
    private String purCode;
    /**
     * 单据状态
     */
    @Excel(name = "单据状态 0草稿  1审核中 2审核完成 9已退回")
    @ApiModelProperty("单据状态 0草稿  1审核中 2审核完成 9已退回")
    private String purStatus;

    @ApiModelProperty("待审批岗位")
    private String approvalPost;
    /**
     * 单位编码
     */
    @Excel(name = "单位编码")
    @ApiModelProperty("单位编码")
    private String unitCode;
    /**
     * 审核日期
     */
    @Excel(name = "审核日期", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("审核日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditDate;
    /**
     * 审核意见
     */
    @Excel(name = "审核意见")
    @ApiModelProperty("审核意见")
    private String auditOpinion;
    /**
     * 备案人
     */
    @Excel(name = "备案人")
    @ApiModelProperty("备案人")
    private String recBy;
    /**
     * 备案日期
     */
    @Excel(name = "备案日期", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("备案日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date recDate;
    /**
     * 是否备案（0代表已备案 2代表未备案）
     */
    @Excel(name = "是否备案", readConverterExp = "0=代表已备案,2=代表未备案")
    @ApiModelProperty("是否备案（0代表已备案 2代表未备案）")
    private String isRecord;
    /**
     * 是否验收（0代表已验收 2代表未验收）
     */
    @Excel(name = "是否验收", readConverterExp = "0=代表已验收,2=代表未验收")
    @ApiModelProperty("是否验收（0代表已验收 2代表未验收）")
    private String isCheck;
    /**
     * 确认验收日期
     */
    @Excel(name = "确认验收日期", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("确认验收日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date checkDate;
    /**
     * 创建者名称
     */
    @Excel(name = "创建者名称")
    @ApiModelProperty("创建者名称")
    private String createName;
    /**
     * 制单日期
     */
    @Excel(name = "制单日期", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("制单日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    /**
     * 开始日期
     */
    private Date startDate;
    /**
     * 结束日期
     */
    private Date endDate;
    private List<String> piis;

    @Excel(name = "财政年度")
    @ApiModelProperty("财政年度")
    private String fiscal;
    @ApiModelProperty("经费来源")
    private List<PurPurchaseIndex> purPurchaseIndexs;
    /**
     * 用款计划
     */
    private List<BugPlanRpc> bugPlanRpcs;

    @ApiModelProperty("采购商品信息")
    private List<PurPurchaseItem> itemList;

    @ApiModelProperty("是否在政府集中采购目录 0是 2否")
    private String isCentralizedPurchasing;

    @ApiModelProperty("是否达集中分散采购限额 0是 2否")
    private String isConcentrationLimits;
    /**
     * 中标金额
     */
    @ApiModelProperty("中标金额")
    private BigDecimal winningMoney;

    @ApiModelProperty("备用字段1")
    private String expExtend1;
    @ApiModelProperty("备用字段2")
    private String expExtend2;
    @ApiModelProperty("备用字段3")
    private String expExtend3;
    @ApiModelProperty("备用字段4")
    private String expExtend4;
    @ApiModelProperty("扩展字段5，暂时作为收回标识使用")
    private String expExtend5;
    @ApiModelProperty("是否无合同采购")
    private String isNoContract;
    @ApiModelProperty("是否可以取消")
    private String isCancel;

    @ApiModelProperty("借款单")
    private List<ExpApplyBorrowListVo> expApplyBorrowListVos;


    public String toJSONString() {
        return JSON.toJSONString(this);
    }

}
