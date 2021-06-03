package com.cxnet.asset.depr.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.cxnet.framework.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 资产补计提折旧主表
 *
 * @author caixx
 * @since 2021-04-16 18:07:51
 */
@Data
@ApiModel(description = "资产补计提折旧主表")
public class AstDeprRepairBill extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -91441150564783324L;

    @ApiModelProperty("主键id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("单据号")
    private String billNo;

    @ApiModelProperty("补计提折旧状态 0未确认 2已确认")
    private String status;

    @ApiModelProperty("折旧年度")
    private Integer fiscal;

    @ApiModelProperty("折旧月份")
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

    @ApiModelProperty("确认人id")
    private String confirmedId;

    @ApiModelProperty("确认人名称")
    private String confirmedName;

    @ApiModelProperty("确认日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date confirmedDate;

    @ApiModelProperty("是否删除   0正常   2停用")
    private String delFlag;

    @ApiModelProperty("说明")
    private String remark;

    @ApiModelProperty("价值")
    private BigDecimal cost;

    @ApiModelProperty("累计折旧")
    private BigDecimal depTotal;

    @ApiModelProperty("账面净值")
    private BigDecimal netValue;

    @ApiModelProperty("数量")
    private Integer qty;

    @ApiModelProperty("本次折旧")
    private BigDecimal thisValue;

    @ApiModelProperty("净残值率")
    private BigDecimal resiRate;

    @ApiModelProperty("最新折旧期间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deprNewestDate;

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

}