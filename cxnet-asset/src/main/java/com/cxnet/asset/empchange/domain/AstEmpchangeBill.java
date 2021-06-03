package com.cxnet.asset.empchange.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 资产使用人变动主表
 *
 * @author zhaoyi
 * @since 2021-04-16 14:28:10
 */
@Data
@ApiModel(description = "资产使用人变动主表")
public class AstEmpchangeBill extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 628428519229497921L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("单据号")
    private String empchangeCode;

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

    @ApiModelProperty("经办人ID")
    private String operatorId;

    @ApiModelProperty("经办人编码")
    private String operatorCode;

    @ApiModelProperty("经办人名称")
    private String operatorName;

    @ApiModelProperty("经办人部门ID")
    private String opedepartmentId;

    @ApiModelProperty("经办人部门编码")
    private String opedepartmentCode;

    @ApiModelProperty("经办人部门名称")
    private String opedepartmentName;

    @ApiModelProperty("总额")
    private BigDecimal cost;

    @ApiModelProperty("数量")
    private Integer qty;

    @ApiModelProperty("变更日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date verificationDate;

    @ApiModelProperty("变更原因")
    private String verificationReason;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("单据状态")
    private String status;

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

    @ApiModelProperty("删除标记0存在2已删除")
    private String delFlag;

    @ApiModelProperty("工作流id")
    private String processinstid;

    @ApiModelProperty("审批岗位")
    private String approvalPost;

    @ApiModelProperty("排序")
    private Integer sort;

    @TableField(exist = false)
    private List<String> piids;

    @TableField(exist = false)
    @ApiModelProperty("开始时间")
    private Date startTime;

    @TableField(exist = false)
    @ApiModelProperty("结束时间")
    private Date endTime;

    @TableField(exist = false)
    @ApiModelProperty("所属部门集合")
    private List<String> deptIds;
}

