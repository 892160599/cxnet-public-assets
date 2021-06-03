package com.cxnet.asset.asttransferbill.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.framework.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 资产移交单主表
 *
 * @author guks
 * @since 2021-04-15 11:58:24
 */
@Data
@ApiModel(description = "资产移交单主表")
public class AstTransferBill extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -58389654411944240L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_UUID)
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

    @ApiModelProperty("移交人id")
    private String transferId;

    @ApiModelProperty("移交人名称")
    private String transferName;

    @ApiModelProperty("移交部门id")
    private String transferDeptId;

    @ApiModelProperty("移交部门编码")
    private String transferDeptCode;

    @ApiModelProperty("移交部门名称")
    private String transferDeptName;

    @ApiModelProperty("数量合计")
    private Integer qty;

    @ApiModelProperty("金额合计")
    private BigDecimal cost;

    @ApiModelProperty("移交日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date transferDate;

    @ApiModelProperty("接收人id")
    private String acceptId;

    @ApiModelProperty("接收人名称")
    private String acceptName;

    @ApiModelProperty("接收部门id")
    private String acceptDeptId;

    @ApiModelProperty("接收部门编码")
    private String acceptDeptCode;

    @ApiModelProperty("接收部门名称")
    private String acceptDeptName;

    @ApiModelProperty("接收人号码")
    private String acceptPhone;

    @ApiModelProperty("移交人号码")
    private String transferPhone;

    @TableField(exist = false)
    private List<String> piids;

    @TableField(exist = false)
    @ApiModelProperty("开始时间")
    private Date startTime;

    @TableField(exist = false)
    @ApiModelProperty("结束时间")
    private Date endTime;

}