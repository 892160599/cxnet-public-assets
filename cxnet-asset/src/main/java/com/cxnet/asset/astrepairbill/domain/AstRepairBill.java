package com.cxnet.asset.astrepairbill.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.cxnet.framework.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资产报修单主表
 *
 * @author guks
 * @since 2021-04-15 11:57:20
 */
@Data
@ApiModel(description = "资产报修单主表")
public class AstRepairBill extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 977633473775451486L;


    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("单据号")
    private String astBill;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("单据状态")
    private String status;

    @ApiModelProperty("单位id")
    private String unitId;

    @ApiModelProperty("单位代码")
    private String unitCode;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("部门id")
    private String deptId;

    @ApiModelProperty("部门编码")
    private String deptCode;

    @ApiModelProperty("部门名称")
    private String deptName;

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

    @ApiModelProperty("删除标记")
    private String delFlag;

    @ApiModelProperty("顺序")
    private Integer sort;

    @ApiModelProperty("流程id")
    private String processinstid;

    @ApiModelProperty("待审批岗位")
    private String approvalPost;

    @ApiModelProperty("审核日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditDate;

    @ApiModelProperty("审核意见")
    private String auditOpinion;

    @ApiModelProperty("项目年度")
    private String fiscal;

    @ApiModelProperty("报修人id")
    private String repairId;

    @ApiModelProperty("报修人名称")
    private String repairName;

    @ApiModelProperty("报修部门id")
    private String repairDeptId;

    @ApiModelProperty("报修部门编码")
    private String repairDeptCode;

    @ApiModelProperty("报修部门名称")
    private String repairDeptName;

    @ApiModelProperty("数量合计")
    private Integer qty;

    @ApiModelProperty("金额合计")
    private BigDecimal cost;

    @ApiModelProperty("报修日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date repairDate;

    @ApiModelProperty("报修人电话")
    private String repairPhone;

    @ApiModelProperty("紧急程度（1一般 2 紧急）")
    private String urgency;

    @ApiModelProperty("故障描述")
    private String faultDescription;

    @TableField(exist = false)
    private List<String> piids;

    @TableField(exist = false)
    @ApiModelProperty("开始时间")
    private Date startTime;

    @TableField(exist = false)
    @ApiModelProperty("结束时间")
    private Date endTime;


}