package com.cxnet.asset.check.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.framework.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资产验收主表
 *
 * @author caixx
 * @since 2021-03-25 09:39:41
 */
@Data
@ApiModel(description = "资产验收主表")
public class AstCheckBill extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -63552461400825807L;

    @ApiModelProperty("主键id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("验收单号")
    private String checkCode;

    @ApiModelProperty("验收人id")
    private String checkPerson;

    @ApiModelProperty("验收人名称")
    private String checkPersonName;

    @ApiModelProperty("验收部门id")
    private String checkDeptId;

    @ApiModelProperty("验收部门编码")
    private String checkDeptCode;

    @ApiModelProperty("验收部门名称")
    private String checkDeptName;

    @ApiModelProperty("验收日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date checkDate;

    @ApiModelProperty("验收合计金额")
    private BigDecimal totalAmt;

    @ApiModelProperty("验收合计数")
    private Integer totalNum;

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

    @ApiModelProperty("审批岗位")
    private String approvalPost;

    @ApiModelProperty("是否生成卡片 0:是 2:否")
    private String isProduce;

    @TableField(exist = false)
    private List<String> piids;

    @TableField(exist = false)
    @ApiModelProperty("开始时间")
    private Date startTime;

    @TableField(exist = false)
    @ApiModelProperty("结束时间")
    private Date endTime;

}