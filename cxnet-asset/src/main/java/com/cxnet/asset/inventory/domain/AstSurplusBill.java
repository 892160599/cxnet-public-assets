package com.cxnet.asset.inventory.domain;

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
 * 资产盘盈主表
 *
 * @author zhangyl
 * @since 2021-04-19 14:14:44
 */
@Data
@ApiModel(description = "资产盘盈主表")
public class AstSurplusBill extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -10054995693944547L;

    @ApiModelProperty("主键id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("单据号")
    private String billNo;

    @ApiModelProperty("年度")
    private Integer fiscal;

    @ApiModelProperty("月份")
    private Integer deprMo;

    @ApiModelProperty("单位id")
    private String unitId;

    @ApiModelProperty("单位编码")
    private String unitCode;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("部门id")
    private String deptId;

    @ApiModelProperty("部门代码")
    private String deptCode;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("领用日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date collectDate;

    @ApiModelProperty("领用人编码")
    private String collectCode;

    @ApiModelProperty("领用人")
    private String collectName;

    @ApiModelProperty("领用部门编码")
    private String collectDeptCode;

    @ApiModelProperty("领用部门")
    private String collectDeptName;

    @ApiModelProperty("领用金额合计")
    private String collectAmount;

    @ApiModelProperty("领用数量合计")
    private String collectQuantity;

    @ApiModelProperty("盘盈日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date surplusDate;

    @ApiModelProperty("经办人id")
    private String handlingId;

    @ApiModelProperty("经办人编码")
    private String handlingCode;

    @ApiModelProperty("经办人")
    private String handlingName;

    @ApiModelProperty("经办部门id")
    private String handlingDeptId;

    @ApiModelProperty("经办部门编码")
    private String handlingDeptCode;

    @ApiModelProperty("经办部门")
    private String handlingDeptName;

    @ApiModelProperty("顺序")
    private String sort;

    @ApiModelProperty("流程id")
    private String processinstid;

    @ApiModelProperty("审批岗位")
    private String approvalPost;

    @ApiModelProperty("是否生成卡片 0:是 2:否")
    private String isProduce;

    @ApiModelProperty("是否删除   0正常   2停用")
    private String delFlag;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("说明")
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

    @TableField(exist = false)
    private List<String> piids;

    @TableField(exist = false)
    @ApiModelProperty("开始时间")
    private Date startTime;

    @TableField(exist = false)
    @ApiModelProperty("结束时间")
    private Date endTime;
}

