package com.cxnet.rpc.domain.budget;

import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.rpc.domain.BugAnnex;
import com.cxnet.rpc.domain.RpcBaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ssw
 */
@Data
public class BugPlanRpc extends RpcBaseEntity {
    /**
     * 用款计划ID
     */
    @ApiModelProperty("用款计划ID")
    private String planId;
    /**
     * 用款计划编码
     */
    @Excel(name = "用款计划编码")
    @ApiModelProperty("用款计划编码")
    private String planCode;
    /**
     * 用款计划单据编号
     */
    @Excel(name = "用款计划单据编号")
    @ApiModelProperty("用款计划单据编号")
    private String planBill;
    /**
     * 来源指标ID
     */
    @Excel(name = "来源指标ID")
    @ApiModelProperty("来源指标ID")
    private String fromBillId;
    /**
     * 来源字表编码
     */
    @Excel(name = "来源字表编码")
    @ApiModelProperty("来源字表编码")
    private String fromBillCode;
    /**
     * 部门Id
     */
    @Excel(name = "部门Id")
    @ApiModelProperty("部门Id")
    private String deptId;
    /**
     * 部门名称
     */
    @Excel(name = "部门名称")
    @ApiModelProperty("部门名称")
    private String deptName;
    /**
     * 部门编码
     */
    @Excel(name = "部门编码")
    @ApiModelProperty("部门编码")
    private String deptCode;
    /**
     * 单位ID
     */
    @Excel(name = "单位ID")
    @ApiModelProperty("单位ID")
    private String unitId;
    /**
     * 单位编码
     */
    @Excel(name = "单位编码")
    @ApiModelProperty("单位编码")
    private String unitCode;
    /**
     * 单位名称
     */
    @Excel(name = "单位名称")
    @ApiModelProperty("单位名称")
    private String unitName;
    /**
     * 单位项目名称
     */
    @Excel(name = "单位项目名称")
    @ApiModelProperty("单位项目名称")
    private String projectName;
    /**
     * 单位项目编码
     */
    @Excel(name = "单位项目编码")
    @ApiModelProperty("单位项目编码")
    private String projectCode;
    /**
     * 经办人
     */
    @Excel(name = "经办人")
    @ApiModelProperty("经办人")
    private String operator;
    /**
     * 政府经济分类名称
     */
    @Excel(name = "政府经济分类名称")
    @ApiModelProperty("政府经济分类名称")
    private String govexpecoName;
    /**
     * 政府经济分类编码
     */
    @Excel(name = "政府经济分类编码")
    @ApiModelProperty("政府经济分类编码")
    private String govexpecoCode;
    /**
     * 部门经济分类名称
     */
    @Excel(name = "部门经济分类名称")
    @ApiModelProperty("部门经济分类名称")
    private String expecoName;
    /**
     * 部门经济分类编码
     */
    @Excel(name = "部门经济分类编码")
    @ApiModelProperty("部门经济分类编码")
    private String expecoCode;
    /**
     * 支付方式 直接支付 授权支付 ？
     */
    @Excel(name = "支付方式 直接支付 授权支付 ？")
    @ApiModelProperty("支付方式 直接支付 授权支付 ？")
    private String payType;
    /**
     * 计划类型 1 增加 2 减少
     */
    @ApiModelProperty("计划类型 1 增加 2 减少")
    private String planType;
    /**
     * 月份
     */
    @Excel(name = "月份")
    @ApiModelProperty("月份")
    private Long month;
    /**
     * 计划金额
     */
    @Excel(name = "计划金额")
    @ApiModelProperty("计划金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal planMoney;
    /**
     * 计划余额
     */
    @Excel(name = "计划余额")
    @ApiModelProperty("计划余额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal planBalance;
    /**
     * 减少金额
     */
    @Excel(name = "减少金额")
    @ApiModelProperty("减少金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal redMoney;
    /**
     * 流程ID
     */
    @Excel(name = "流程ID")
    @ApiModelProperty("流程ID")
    private String processinstid;
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
     * 待审批岗位
     */
    @Excel(name = "待审批岗位")
    @ApiModelProperty("待审批岗位")
    private String approvalPost;
    /**
     * 状态
     */
    @Excel(name = "状态")
    @ApiModelProperty("状态")
    private String planStatus;
    /**
     * 创建人名称
     */
    @Excel(name = "创建人名称")
    @ApiModelProperty("创建人名称")
    private String createName;
    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    private String delFlag;
    /**
     * 是否下达
     */
    @Excel(name = "是否下达")
    @ApiModelProperty("是否下达")
    private String isSend;
    /**
     * 下达日期
     */
    @Excel(name = "下达日期", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("下达日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendDate;
    /**
     * 说明
     */
    @Excel(name = "说明")
    @ApiModelProperty("说明")
    private String explan;
    /**
     * 支出功能分类代码
     */
    @Excel(name = "支出功能分类代码")
    @ApiModelProperty("支出功能分类代码")
    private String expfuncCode;
    /**
     * 支出功能分类名称
     */
    @Excel(name = "支出功能分类名称")
    @ApiModelProperty("支出功能分类名称")
    private String expfuncName;
    /**
     * 项目年度
     */
    @Excel(name = "项目年度")
    @ApiModelProperty("项目年度")
    private String fiscal;
    /**
     * 是否是自动生成
     */
    @Excel(name = "是否是自动生成 （0：自动生成 2否）")
    @ApiModelProperty("是否是自动生成（0：自动生成 2否）")
    private String autoGenerate;
    /**
     * 附件集合
     */
    private List<BugAnnex> bugAnnexList;

    private List<String> piis;
    /**
     * 合同占用金额
     */
    @ApiModelProperty("合同占用金额")
    private BigDecimal htAmt;
    /**
     * 合同已用金额
     */
    @ApiModelProperty("合同已用金额")
    private BigDecimal htUsedAmt;
    /**
     * 报销占用金额
     */
    @ApiModelProperty("报销占用金额")
    private BigDecimal expenseAmt;
    /**
     * 报销已用金额
     */
    @ApiModelProperty("报销已用金额")
    private BigDecimal expenseUsedAmt;
    /**
     * 用款计划可用余额
     */
    private BigDecimal planAvailableAmt;

    /**
     * 获取用款计划可用金额
     * 用款计划余额-追减金额-合同占用金额-报销占用金额
     *
     * @return 可用金额
     */
    public BigDecimal getPlanAvailableAmt() {
        planAvailableAmt = (planBalance == null ? BigDecimal.ZERO : planBalance)
                .subtract(redMoney == null ? BigDecimal.ZERO : redMoney)
                .subtract(htAmt == null ? BigDecimal.ZERO : htAmt)
                .subtract(expenseAmt == null ? BigDecimal.ZERO : expenseAmt);
        return planAvailableAmt;
    }
}
