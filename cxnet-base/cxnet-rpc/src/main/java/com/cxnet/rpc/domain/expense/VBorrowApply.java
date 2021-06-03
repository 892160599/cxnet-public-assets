package com.cxnet.rpc.domain.expense;


import com.cxnet.rpc.domain.RpcBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VBorrowApply extends RpcBaseEntity {
    @ApiModelProperty("前置申请ID")
    private String fromId;
    @ApiModelProperty("前置申请编号")
    private String fromBill;
    @ApiModelProperty("部门id")
    private String deptId;
    @ApiModelProperty("部门名称")
    private String deptName;
    @ApiModelProperty("申请金额")
    private BigDecimal ApplyMoney;
    @ApiModelProperty("前置申请类型名称")
    private String fromName;
    @ApiModelProperty("前置申请类型 ( c: 合同 p:采购 e:报销申请)")
    private String fromType;
    @ApiModelProperty("单位id")
    private String unitId;
    @ApiModelProperty("年度")
    private String fiscal;
    @ApiModelProperty("申请人")
    private String applyPerson;
    @ApiModelProperty("前置申请类型编码")
    private String fromTypeCode;
    @ApiModelProperty("前置申请项目名称 合同：合同名称 ，采购：采购内容 ，报销 ：申请事由")
    private String applyProjectName;
    @ApiModelProperty("合同甲方")
    private String unitOne;
    @ApiModelProperty("合同乙方")
    private String unitTwo;
}
