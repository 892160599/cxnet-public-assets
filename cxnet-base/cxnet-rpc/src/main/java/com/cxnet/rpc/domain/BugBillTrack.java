package com.cxnet.rpc.domain;

import com.cxnet.common.jsr303group.AddGroup;
import com.cxnet.common.jsr303group.UpdateGroup;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.cxnet.common.utils.poi.annotation.Excel;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 预算指标过程实体 bug_bill_track
 *
 * @author cxnet
 * @date 2020-08-26
 */
@Data
@ApiModel("预算指标过程")
public class BugBillTrack extends RpcBaseEntity {

    /**
     * 主键
     */
    @NotBlank(message = "指标编号不能为空", groups = {UpdateGroup.class})
    @ApiModelProperty("主键")
    private String billId;
    /**
     * 指标编码
     */
    @Excel(name = "指标编码")
    @ApiModelProperty("指标编码")
    private String billCode;
    /**
     * 来源指标编码
     */
    @Excel(name = "来源指标编码")
    @ApiModelProperty("来源指标编码")
    private String fromBalanceCode;
    /**
     * 来源指标余额表ID
     */
    @Excel(name = "来源指标余额表ID")
    @ApiModelProperty("来源指标余额表ID")
    private String fromBalanceId;
    /**
     * 去向指标余额表ID
     */
    @Excel(name = "去向指标余额表ID")
    @ApiModelProperty("去向指标余额表ID")
    private String toBalanceId;
    /**
     * 年度
     */
    @Excel(name = "年度")
    @ApiModelProperty("年度")
    private Long year;
    /**
     * 指标级次:1-单位级;2-部门级
     */
    @Excel(name = "指标级次:1-单位级;2-部门级")
    @ApiModelProperty("指标级次:1-单位级;2-部门级")
    private Long budLevel;
    /**
     * 调整类型bug_bill_adjust_type
     */
    @Excel(name = "调整类型bug_bill_adjust_type")
    @ApiModelProperty("调整类型bug_bill_adjust_type")
    private String adjustType;
    /**
     * 录入金额
     */
    @Excel(name = "录入金额")
    @ApiModelProperty("录入金额")
    private BigDecimal inputAmt;
    /**
     * 核定金额
     */
    @Excel(name = "核定金额")
    @ApiModelProperty("核定金额")
    private BigDecimal checkAmt;
    /**
     * 单位ID
     */
    @Excel(name = "单位ID")
    @ApiModelProperty("单位ID")
    private String unitId;
    /**
     * 单位代码
     */
    @Excel(name = "单位代码")
    @ApiModelProperty("单位代码")
    private String unitCode;
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
    @Excel(name = "财政项目代码")
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
    @Excel(name = "项目类型代码")
    @ApiModelProperty("项目类型代码")
    private String protypeCode;
    /**
     * 项目类型名称
     */
    @Excel(name = "项目类型名称")
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
    @Excel(name = "预算来源代码")
    @ApiModelProperty("预算来源代码")
    private String budsourceCode;
    /**
     * 预算来源名称
     */
    @Excel(name = "预算来源名称")
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
    @Excel(name = "支出类型代码")
    @ApiModelProperty("支出类型代码")
    private String exptypeCode;
    /**
     * 支出类型名称
     */
    @Excel(name = "支出类型名称")
    @ApiModelProperty("支出类型名称")
    private String exptypeName;
    /**
     * 结算方式代码
     */
    @Excel(name = "结算方式代码")
    @ApiModelProperty("结算方式代码")
    private String setmodeCode;
    /**
     * 结算方式名称
     */
    @Excel(name = "结算方式名称")
    @ApiModelProperty("结算方式名称")
    private String setmodeName;
    /**
     * 往来代码
     */
    @Excel(name = "往来代码")
    @ApiModelProperty("往来代码")
    private String currentCode;
    /**
     * 往来名称
     */
    @Excel(name = "往来名称")
    @ApiModelProperty("往来名称")
    private String currentName;
    /**
     * 部门ID
     */
    @Excel(name = "部门ID")
    @ApiModelProperty("部门ID")
    private String deptId;
    /**
     * 部门代码
     */
    @Excel(name = "部门代码")
    @ApiModelProperty("部门代码")
    private String deptCode;
    /**
     * 部门名称
     */
    @Excel(name = "部门名称")
    @ApiModelProperty("部门名称")
    private String deptName;
    /**
     * 人员代码
     */
    @Excel(name = "人员代码")
    @ApiModelProperty("人员代码")
    private String employeeCode;
    /**
     * 人员名称
     */
    @Excel(name = "人员名称")
    @ApiModelProperty("人员名称")
    private String employeeName;
    /**
     * 资产类型
     */
    @Excel(name = "资产类型")
    @ApiModelProperty("资产类型")
    private String fatypeCode;
    /**
     * 资产类型
     */
    @Excel(name = "资产类型")
    @ApiModelProperty("资产类型")
    private String fatypeName;
    /**
     * 录入批次号
     */
    @Excel(name = "录入批次号")
    @ApiModelProperty("录入批次号")
    private String inputGroupId;
    /**
     * 录入类型：1 录入 2 导入
     */
    @Excel(name = "录入类型：1 录入 2 导入")
    @ApiModelProperty("录入类型：1 录入 2 导入")
    private Long inputType;
    /**
     * 说明
     */
    @Excel(name = "说明")
    @ApiModelProperty("说明")
    private String explain;
    /**
     * 业务日期
     */
    @Excel(name = "业务日期", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("业务日期")
    private Date transDate;
    /**
     * 终审日期
     */
    @Excel(name = "终审日期", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("终审日期")
    private Date auditDate;
    /**
     * 流程实例ID
     */
    @Excel(name = "流程实例ID")
    @ApiModelProperty("流程实例ID")
    private String processinstid;
    /**
     * 创建者名称
     */
    @Excel(name = "创建者名称")
    @ApiModelProperty("创建者名称")
    private String createName;
    /**
     * 单位名称
     */
    @Excel(name = "单位名称")
    @ApiModelProperty("单位名称")
    private String unitName;
    /**
     * 制单日期
     */
    @Excel(name = "制单日期", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("制单日期")
    private Date createDate;
    /**
     * 经办人
     */
    @Excel(name = "经办人")
    @ApiModelProperty("经办人")
    private String operator;
    /**
     * 指标编号(导入或手动输入)
     */
    @Excel(name = "指标编号(导入或手动输入)")
    @ApiModelProperty("指标编号(导入或手动输入)")
    private String billNo;
    /**
     * 状态(0:草稿  1:待审核  2:已审核  9:已退回)
     */
    @Excel(name = "状态(0:草稿  1:待审核  2:已审核  9:已退回)")
    @ApiModelProperty("状态(0:草稿  1:待审核  2:已审核  9:已退回)")
    private String status;
    @Excel(name = "文号")
    @ApiModelProperty("文号")
    private String wh;
    /**
     * 项目年度
     */
    @Excel(name = "项目年度")
    @ApiModelProperty("项目年度")
    private String fiscal;
    @Excel(name = "岗位状态")
    @ApiModelProperty("岗位状态")
    private String approvalPost;
    @ApiModelProperty("附件列表")
    private List<BugAnnex> files;
    private List<String> piis;
    @ApiModelProperty("是否控制金额0：控制 1：不控制")
    private String controlMoney;

    @ApiModelProperty("扩展字段1")
    private String expExtend1;
    @ApiModelProperty("扩展字段2")
    private String expExtend2;
    @ApiModelProperty("扩展字段3")
    private String expExtend3;
    @ApiModelProperty("扩展字段4")
    private String expExtend4;
    @ApiModelProperty("扩展字段5，作为收回标识使用")
    private String expExtend5;
}