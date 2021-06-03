package com.cxnet.rpc.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author makejava
 * @since 2021-04-02 14:24:00
 */
@Data
@ApiModel(description = "报销费用明细拆分表")
public class ExpCostSplit extends RpcBaseEntity implements Serializable {
    private static final long serialVersionUID = -98034723961944366L;

    @ApiModelProperty("主键id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String splitId;

    @ApiModelProperty("指标id")
    private String balId;

    @ApiModelProperty("主表id")
    private String expId;

    @ApiModelProperty("费用明细id")
    private String costId;

    @ApiModelProperty("指标编码")
    private String balCode;

    @ApiModelProperty("财政项目代码")
    private String projectCode;

    @ApiModelProperty("财政项目名称")
    private String projectName;

    @ApiModelProperty("支出内容")
    private String costName;

    @ApiModelProperty("单位ID")
    private String unitId;

    @ApiModelProperty("单位代码")
    private String unitCode;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("部门ID")
    private String deptId;

    @ApiModelProperty("部门编码")
    private String deptCode;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("部门经济分类代码")
    private String expecoCode;

    @ApiModelProperty("部门经济分类名称")
    private String expecoName;

    @ApiModelProperty("拆分金额")
    private Object splitCost;

    @ApiModelProperty("年度")
    private String fiscal;

    @ApiModelProperty("扩展字段1")
    private String expExtend1;

    @ApiModelProperty("扩展字段2")
    private String expExtend2;

    @ApiModelProperty("扩展字段3")
    private String expExtend3;

    @ApiModelProperty("扩展字段4")
    private String expExtend4;

    @ApiModelProperty("扩展字段5")
    private String expExtend5;

}