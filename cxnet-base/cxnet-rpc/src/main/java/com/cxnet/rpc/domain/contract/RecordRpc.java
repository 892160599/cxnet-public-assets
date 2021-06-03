package com.cxnet.rpc.domain.contract;

import com.alibaba.fastjson.JSON;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.rpc.domain.ImplementPlan;
import com.cxnet.rpc.domain.expense.ExpApplyBorrow;
import com.cxnet.rpc.domain.expense.ExpApplyBorrowListVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class RecordRpc {

    /**
     * 备案编号
     */
    @ApiModelProperty("备案编号")
    private String recordId;
    /**
     * 合同编号
     */
    @Excel(name = "合同编号")
    @ApiModelProperty("合同编号")
    private String conId;
    /**
     * 备案状态
     */
    @Excel(name = "备案状态")
    @ApiModelProperty("备案状态")
    private String recordStatus;
    /**
     * 备案年度
     */
    @Excel(name = "备案年度")
    @ApiModelProperty("备案年度")
    private String recordYear;
    /**
     * 合同编码
     */
    @Excel(name = "合同编码")
    @ApiModelProperty("合同编码")
    private String conCode;
    /**
     * 合同名称
     */
    @Excel(name = "合同名称")
    @ApiModelProperty("合同名称")
    private String conName;
    /**
     * 合同类型
     */
    @Excel(name = "合同类型")
    @ApiModelProperty("合同类型")
    private String conType;
    /**
     * 经办人
     */
    @Excel(name = "经办人")
    @ApiModelProperty("经办人")
    private String conOperator;
    /**
     * 经办部门
     */
    @Excel(name = "经办部门")
    @ApiModelProperty("经办部门")
    private String operatorDept;
    /**
     * 合同金额
     */
    @Excel(name = "合同金额")
    @ApiModelProperty("合同金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal conMoney;
    /**
     * 备案备注
     */
    @Excel(name = "备案备注")
    @ApiModelProperty("备案备注")
    private String recordBz;
    /**
     * 合同签订日期
     */
    @Excel(name = "合同签订日期", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("合同签订日期")
    private Date recordDate;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;
    /**
     * 甲方信息
     */
    @Excel(name = "甲方信息")
    @ApiModelProperty("甲方信息")
    private String unitOne;
    /**
     * 乙方信息
     */
    @Excel(name = "乙方信息")
    @ApiModelProperty("乙方信息")
    private String unitTwo;
    /**
     * 部门编码
     */
    @Excel(name = "部门编码")
    @ApiModelProperty("部门编码")
    private String deptCode;
    /**
     * 部门名称
     */
    @Excel(name = "部门名称")
    @ApiModelProperty("部门名称")
    private String deptName;

    /**
     * 部门id
     */
    @Excel(name = "部门id")
    @ApiModelProperty("部门id")
    private String deptId;

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
     * 单位id
     */
    @Excel(name = "单位id")
    @ApiModelProperty("单位id")
    private String unitId;
    /** 附件列表 */
    // private List<Annex> annexes;
    /**
     * 是否历史备案
     */
    private String recordHistory;
    /**
     * 支付明细列表
     */
    private List<ImplementPlan> plans;
    /**
     * 中止原因
     */
    @Excel(name = "中止原因")
    @ApiModelProperty("中止原因")
    private String discontinue;
    /**
     * 合同支付明细
     */
    @Excel(name = "合同支付明细")
    @ApiModelProperty("合同支付明细")
    private List<ConPayDetailRpc> conPayDetails;
    /**
     * 合同余额
     */
    @Excel(name = "合同余额")
    @ApiModelProperty("合同余额")
    private BigDecimal conBalancer;
    /**
     * 业务年度
     */
    @Excel(name = "业务年度")
    @ApiModelProperty("业务年度")
    private String fiscal;
    @ApiModelProperty("借款单信息")
    List<ExpApplyBorrowListVo> expApplyBorrowListVos;

    public String toJSONString() {
        return JSON.toJSONString(this);
    }

}
