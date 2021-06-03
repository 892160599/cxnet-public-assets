package com.cxnet.asset.allot.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 资产单位调拨主表
 *
 * @author zhaoyi
 * @since 2021-04-09 14:33:01
 */
@Data
@ApiModel(description = "资产单位调拨主表")
public class AstAllotBill extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 108293984786143583L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;


    @ApiModelProperty("单据号")
    private String allotCode;

    @Excel
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

    @ApiModelProperty("调出部门id")
    private String deptIdOut;

    @ApiModelProperty("调出部门编码")
    private String deptCodeOut;

    @ApiModelProperty("调出部门名称")
    private String deptNameOut;

    @ApiModelProperty("调出部门使用人ID")
    private String empIdOut;

    @ApiModelProperty("调出部门使用人代码")
    private String empCodeOut;

    @ApiModelProperty("调出部门使用人")
    private String empNameOut;

    @ApiModelProperty("调出部门使用人电话")
    private String empTelOut;

    @ApiModelProperty("调入部门id")
    private String deptIdIn;

    @ApiModelProperty("调入部门编码")
    private String deptCodeIn;

    @ApiModelProperty("调入部门名称")
    private String deptNameIn;

    @ApiModelProperty("调入部门使用人ID")
    private String empIdIn;

    @ApiModelProperty("调入部门使用人代码")
    private String empCodeIn;

    @ApiModelProperty("调入部门使用人")
    private String empNameIn;

    @ApiModelProperty("调入部门使用人电话")
    private String empTelIn;

    @ApiModelProperty("调拨日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date verificationDate;

    @ApiModelProperty("调拨总额")
    private BigDecimal cost;

    @ApiModelProperty("调拨数量")
    private Integer qty;

    @ApiModelProperty("调出单位id")
    private String unitIdOut;

    @ApiModelProperty("调出单位代码")
    private String unitCodeOut;

    @ApiModelProperty("调出单位名称")
    private String unitNameOut;

    @ApiModelProperty("调入单位id")
    private String unitIdIn;

    @ApiModelProperty("调入单位代码")
    private String unitCodeIn;

    @ApiModelProperty("调入单位名称")
    private String unitNameIn;

    @ApiModelProperty("单据状态")
    private String status;

    @ApiModelProperty("备注")
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

    @ApiModelProperty("删除标记0存在2已删除")
    private String delFlag;

    @ApiModelProperty("工作流id")
    private String processinstid;

    @ApiModelProperty("审批岗位")
    private String approvalPost;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("经办人ID")
    private String operatorId;

    @ApiModelProperty("经办人编码")
    private String operatorCode;

    @ApiModelProperty("经办人名称")
    private String operatorName;

    @TableField(exist = false)
    private List<String> piids;

    @TableField(exist = false)
    @ApiModelProperty("开始时间")
    private Date startTime;

    @TableField(exist = false)
    @ApiModelProperty("结束时间")
    private Date endTime;

    @TableField(exist = false)
    @ApiModelProperty("当前登录用户")
    private String userId;

    @TableField(exist = false)
    @ApiModelProperty("查询状态 0-调出，1-调入")
    private String selectType;

    @TableField(exist = false)
    @ApiModelProperty("所属部门集合")
    private List<String> deptIds;
}

