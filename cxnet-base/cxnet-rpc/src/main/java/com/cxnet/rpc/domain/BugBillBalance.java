package com.cxnet.rpc.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.cxnet.common.utils.poi.annotation.Excel;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预算指标余额实体 bug_bill_balance
 *
 * @author cxnet
 * @date 2020-08-26
 */
@Data
@ApiModel("预算指标余额")
public class BugBillBalance extends RpcBaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String balId;
    /**
     * 指标编码
     */
//    @Excel(name = "指标编码")
    @ApiModelProperty("指标编码")
    private String balCode;
    /**
     * 指标类型
     */
//    @Excel(name = "指标类型")
    @ApiModelProperty("指标类型")
    private String balType;
    /**
     * 原指标金额
     */
    @Excel(name = "原指标金额")
    @ApiModelProperty("原指标金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal financeAmt;
    /**
     * 期初支出金额
     */
//    @Excel(name = "期初支出金额")
    @ApiModelProperty("期初支出金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal qczcAmt;
    /**
     * 期末结转金额
     */
//    @Excel(name = "期末结转金额")
    @ApiModelProperty("期末结转金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal qmjzAmt;
    /**
     * 年度
     */
    @Excel(name = "年度")
    @ApiModelProperty("年度")
    private Long year;
    /**
     * 预算总金额（现预算金额）
     */
    @Excel(name = "预算总金额", readConverterExp = "现预算金额")
    @ApiModelProperty("预算总金额（现预算金额）")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal totalAmt;
    /**
     * 增加金额
     */
//    @Excel(name = "增加金额")
    @ApiModelProperty("增加金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal addAmt;
    /**
     * 减少金额
     */
//    @Excel(name = "减少金额")
    @ApiModelProperty("减少金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal subAmt;
    /**
     * 预算已用金额
     */
//    @Excel(name = "预算已用金额")
    @ApiModelProperty("预算已用金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal usedAmt;
    /**
     * 申请金额
     */
//    @Excel(name = "申请金额")
    @ApiModelProperty("申请金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal applyAmt;
    /**
     * 申请已用金额
     */
//    @Excel(name = "申请已用金额")
    @ApiModelProperty("申请已用金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal applyUsedAmt;
    /**
     * 生成借款金额
     */
//    @Excel(name = "生成借款金额")
    @ApiModelProperty("生成借款金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal loanAmt;
    /**
     * 报销冲借款金额
     */
//    @Excel(name = "报销冲借款金额")
    @ApiModelProperty("报销冲借款金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal loanUsedAmt;
    /**
     * 生成报销金额
     */
    @Excel(name = "生成报销金额")
    @ApiModelProperty("生成报销金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal expenseAmt;
    /**
     * 报销使用金额
     */
    @Excel(name = "报销使用金额")
    @ApiModelProperty("报销使用金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal expenseUsedAmt;
    /**
     * 报销单已终审金额
     */
//    @Excel(name = "报销单已终审金额")
    @ApiModelProperty("报销单已终审金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal expensePayAmt;
    /**
     * 报销单生成凭证金额
     */
//    @Excel(name = "报销单生成凭证金额")
    @ApiModelProperty("报销单生成凭证金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal pexGalAmt;
    /**
     * 生成记账金额
     */
//    @Excel(name = "生成记账金额")
    @ApiModelProperty("生成记账金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal galAmt;
    /**
     * 生成资产金额
     */
//    @Excel(name = "生成资产金额")
    @ApiModelProperty("生成资产金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal assetAmt;
    /**
     * 生成合同金额
     */
//    @Excel(name = "生成合同金额")
    @ApiModelProperty("生成合同金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal htAmt;
    /**
     * 合同结算金额
     */
//    @Excel(name = "合同结算金额")
    @ApiModelProperty("合同结算金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal htUsedAmt;
    /**
     * 生成用款额度金额
     */
//    @Excel(name = "生成用款额度金额")
    @ApiModelProperty("生成用款额度金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal quotaAmt;
    /**
     * 用款额度已用金额
     */
//    @Excel(name = "用款额度已用金额")
    @ApiModelProperty("用款额度已用金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal quotaUsedAmt;
    /**
     * 生成采购申请金额
     */
//    @Excel(name = "生成采购申请金额")
    @ApiModelProperty("生成采购申请金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal purchaseAmt;
    /**
     * 采购申请已用金额
     */
//    @Excel(name = "采购申请已用金额")
    @ApiModelProperty("采购申请已用金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal purchaseUsedAmt;
    /**
     * 单位id
     */
    @Excel(name = "单位id")
    @ApiModelProperty("单位id")
    private String unitId;
    /**
     * 单位代码
     */
    @Excel(name = "单位代码")
    @ApiModelProperty("单位代码")
    private String unitCode;
    /**
     * 单位名称
     */
    @Excel(name = "单位名称")
    @ApiModelProperty("单位名称")
    private String unitName;
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
     * 政府预算经济分类代码
     */
    @Excel(name = "政府预算经济分类代码")
    @ApiModelProperty("政府预算经济分类代码")
    private String govexpecoCode;
    /**
     * 政府预算经济分类名称
     */
    @Excel(name = "政府预算经济分类名称")
    @ApiModelProperty("政府预算经济分类名称")
    private String govexpecoName;
    /**
     * 部门预算经济分类代码
     */
    @Excel(name = "部门预算经济分类代码")
    @ApiModelProperty("部门预算经济分类代码")
    private String expecoCode;
    /**
     * 部门预算经济分类名称
     */
    @Excel(name = "部门预算经济分类名称")
    @ApiModelProperty("部门预算经济分类名称")
    private String expecoName;
    /**
     * 部门项目代码
     */
    @Excel(name = "部门项目代码")
    @ApiModelProperty("部门项目代码")
    private String depproCode;
    /**
     * 部门项目名称
     */
    @Excel(name = "部门项目名称")
    @ApiModelProperty("部门项目名称")
    private String depproName;
    /**
     * 财政项目代码
     */
    @Excel(name = "财政项目目代码")
    @ApiModelProperty("财政项目代码")
    private String projectCode;
    /**
     * 财政项目名称
     */
    @Excel(name = "财政项目名称")
    @ApiModelProperty("财政项目名称")
    private String projectName;
    /**
     * 项目类型代码
     */
//    @Excel(name = "项目类型代码")
    @ApiModelProperty("项目类型代码")
    private String protypeCode;
    /**
     * 项目类型名称
     */
//    @Excel(name = "项目类型名称")
    @ApiModelProperty("项目类型名称")
    private String protypeName;
    /**
     * 资金性质代码
     */
    @Excel(name = "资金性质代码")
    @ApiModelProperty("资金性质代码")
    private String fundtypeCode;
    /**
     * 资金性质名称
     */
    @Excel(name = "资金性质名称")
    @ApiModelProperty("资金性质名称")
    private String fundtypeName;
    /**
     * 预算来源代码
     */
//    @Excel(name = "预算来源代码")
    @ApiModelProperty("预算来源代码")
    private String budsourceCode;
    /**
     * 预算来源名称
     */
//    @Excel(name = "预算来源名称")
    @ApiModelProperty("预算来源名称")
    private String budsourceName;
    /**
     * 经费来源代码
     */
    @Excel(name = "经费来源代码")
    @ApiModelProperty("经费来源代码")
    private String fundsourceCode;
    /**
     * 经费来源名称
     */
    @Excel(name = "经费来源名称")
    @ApiModelProperty("经费来源名称")
    private String fundsourceName;
    /**
     * 支付方式代码
     */
    @Excel(name = "支付方式代码")
    @ApiModelProperty("支付方式代码")
    private String paytypeCode;
    /**
     * 支付方式名称
     */
    @Excel(name = "支付方式名称")
    @ApiModelProperty("支付方式名称")
    private String paytypeName;
    /**
     * 支出类型代码
     */
//    @Excel(name = "支出类型代码")
    @ApiModelProperty("支出类型代码")
    private String exptypeCode;
    /**
     * 支出类型名称
     */
//    @Excel(name = "支出类型名称")
    @ApiModelProperty("支出类型名称")
    private String exptypeName;
    /**
     * 结算方式代码
     */
//    @Excel(name = "结算方式代码")
    @ApiModelProperty("结算方式代码")
    private String setmodeCode;
    /**
     * 结算方式名称
     */
//    @Excel(name = "结算方式名称")
    @ApiModelProperty("结算方式名称")
    private String setmodeName;
    /**
     * 往来代码
     */
//    @Excel(name = "往来代码")
    @ApiModelProperty("往来代码")
    private String currentCode;
    /**
     * 往来名称
     */
//    @Excel(name = "往来名称")
    @ApiModelProperty("往来名称")
    private String currentName;
    /**
     * 部门id
     */
//    @Excel(name = "部门id")
    @ApiModelProperty("部门id")
    private String deptId;
    /**
     * 部门编码
     */
    @Excel(name = "部门编码")
    @ApiModelProperty("部门编码")
    private String deptCode;
    /**
     * 部门部门名称
     */
    @Excel(name = "部门部门名称")
    @ApiModelProperty("部门部门名称")
    private String deptName;
    /**
     * 人员代码
     */
//    @Excel(name = "人员代码")
    @ApiModelProperty("人员代码")
    private String employeeCode;
    /**
     * 人员名称
     */
//    @Excel(name = "人员名称")
    @ApiModelProperty("人员名称")
    private String employeeName;
    /**
     * 资产类型代码
     */
//    @Excel(name = "资产类型代码")
    @ApiModelProperty("资产类型代码")
    private String fatypeCode;
    /**
     * 资产类型名称
     */
//    @Excel(name = "资产类型名称")
    @ApiModelProperty("资产类型名称")
    private String fatypeName;
    /**
     * 制单日期
     */
//    @Excel(name = "制单日期", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("制单日期")
    private Date createDate;
    /**
     * 经办人
     */
//    @Excel(name = "经办人")
    @ApiModelProperty("经办人")
    private String operator;
    /**
     * 指标编号(导入或手动输入)
     */
//    @Excel(name = "指标编号(导入或手动输入)")
    @ApiModelProperty("指标编号(导入或手动输入)")
    private String billNo;
    /**
     * 预算余额
     */
    @Excel(name = "预算余额")
    @ApiModelProperty("预算余额")
    private BigDecimal balAmt;
    /**
     * 预算项代码
     */
//    @Excel(name = "预算项代码")
    @ApiModelProperty("预算项代码")
    private String buditemCode;
    /**
     * 预算项名称
     */
//    @Excel(name = "预算项名称")
    @ApiModelProperty("预算项名称")
    private String buditemName;
    /**
     * 说明
     */
//    @Excel(name = "说明")
    @ApiModelProperty("说明")
    private String explain;
    /**
     * 工资标识0：非工资 1：工资
     */
//    @Excel(name = "工资标识0：非工资 1：工资")
    @ApiModelProperty("工资标识0：非工资 1：工资")
    private String salflagCode;
    /**
     * 工资标识0：非工资 1：工资
     */
//    @Excel(name = "工资标识0：非工资 1：工资")
    @ApiModelProperty("工资标识0：非工资 1：工资")
    private String salflagName;
    /**
     * 采购标识0：非采购 1：采购
     */
//    @Excel(name = "采购标识0：非采购 1：采购")
    @ApiModelProperty("采购标识0：非采购 1：采购")
    private String purflagCode;
    /**
     * 采购标识0：非采购 1：采购资
     */
//    @Excel(name = "采购标识0：非采购 1：采购资")
    @ApiModelProperty("采购标识0：非采购 1：采购资")
    private String purflagName;
    /**
     * 来源指标余额表ID
     */
//    @Excel(name = "来源指标余额表ID")
    @ApiModelProperty("来源指标余额表ID")
    private String fromBalanceId;
    /**
     * 指标级次:1-单位级;2-部门级
     */
//    @Excel(name = "指标级次:1-单位级;2-部门级")
    @ApiModelProperty("指标级次:1-单位级;2-部门级")
    private Long budLevel;
    /**
     * 项目负责人
     */
//    @Excel(name = "项目负责人")
    @ApiModelProperty("项目负责人")
    private String projectUser;
    /**
     * 项目负责人名称
     */
//    @Excel(name = "项目负责人名称")
    @ApiModelProperty("项目负责人名称")
    private String projectUserName;
    /**
     * 最后同步时间
     */
//    @Excel(name = "最后同步时间")
    @ApiModelProperty("最后同步时间")
    private Date lastTime;
    /**
     * 创建者名称
     */
//    @Excel(name = "创建者名称")
    @ApiModelProperty("创建者名称")
    private String createName;
    //    @Excel(name = "文号")
    @ApiModelProperty("文号")
    private String wh;
    /**
     * 项目年度
     */
//    @Excel(name = "项目年度")
    @ApiModelProperty("项目年度")
    @NotNull(message = "年度不能为空")
    private String fiscal;

    @ApiModelProperty("可用金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal availableAmt;

    @ApiModelProperty("是否控制金额0：控制 1：不控制")
    private String controlMoney;

    @ApiModelProperty("是否可用 0可用 1不可用")
    private String isUsed;

    /**
     * 指标id集合
     */
    private List<String> balIds;
    /**
     * 部门项目code集合
     */
    private List<String> projectLibrarieCodes;
    /**
     * 金额范围
     */
    private BigDecimal maxTotalAmt;
    private BigDecimal minTotalAmt;
    private BigDecimal maxAvailableAmt;
    private BigDecimal minAvailableAmt;

    private Integer pageSize;
    private Integer pageNum;

    /**
     * 可用金额
     *
     * @return
     */
    public BigDecimal getAvailableAmt() {
        // 可用金额 = 指标余额-减少金额-生成报销金额-生成合同金额-生成用款额度金额-生成采购申请金额-借款单
        availableAmt = (balAmt == null ? BigDecimal.valueOf(0) : balAmt)
                .subtract(subAmt == null ? BigDecimal.valueOf(0) : subAmt)
                .subtract(expenseAmt == null ? BigDecimal.valueOf(0) : expenseAmt)
                .subtract(htAmt == null ? BigDecimal.valueOf(0) : htAmt)
                .subtract(quotaAmt == null ? BigDecimal.valueOf(0) : quotaAmt)
                .subtract(purchaseAmt == null ? BigDecimal.valueOf(0) : purchaseAmt)
                .subtract(loanAmt == null ? BigDecimal.valueOf(0) : loanAmt);
        return availableAmt;
    }

    public BugBillBalance() {
    }

    public BugBillBalance(BugBillTrack bugBillTrack) {
        Date date = DateUtil.date().toJdkDate();
        BeanUtil.copyProperties(bugBillTrack, this);
        this.financeAmt = bugBillTrack.getInputAmt();
        this.totalAmt = bugBillTrack.getInputAmt();
        this.balAmt = bugBillTrack.getInputAmt();
        super.setCreateBy(bugBillTrack.getCreateBy());
        super.setCreateTime(date);
        this.createName = bugBillTrack.getCreateName();
        this.createDate = date;
        this.wh = bugBillTrack.getWh();
        this.fromBalanceId = bugBillTrack.getFromBalanceId();
    }
}