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
 * @author zhangyl
 * @since 2021-04-02 10:02:39
 */
@Data
@ApiModel(description = "")
public class AstInventoryBill extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 669124371287586036L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("年度")
    private String fiscal;

    @ApiModelProperty("单位id")
    private String unitId;

    @ApiModelProperty("单位编码")
    private String unitCode;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("部门id")
    private String deptId;

    @ApiModelProperty("部门编码")
    private String deptCode;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("单据号")
    private String billNo;

    @ApiModelProperty("盘点方案代码")
    private String checkPlanCode;

    @ApiModelProperty("盘点方案名称")
    private String checkPlanName;

    @ApiModelProperty("盘点方案数量合计")
    private String checkPlanCount;

    @ApiModelProperty("盘点方案金额合计")
    private String checkPlanSum;

    @ApiModelProperty("盘点开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date planStartDate;

    @ApiModelProperty("盘点结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date planEndDate;

    @ApiModelProperty("盘点类型")
    private String planType;

    @ApiModelProperty("盘存资产合计")
    private String planTakeTotal;

    @ApiModelProperty("盘盈资产合计")
    private String planSurplusTotal;

    @ApiModelProperty("盘亏资产合计")
    private String planLossTotal;

    @ApiModelProperty("购置开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date acquisitionStartDate;

    @ApiModelProperty("购置结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date acquisitionEndDate;

    @ApiModelProperty("资产类别编码")
    private String assetsTypeCode;

    @ApiModelProperty("资产类别名称")
    private String assetsTypeName;

    @ApiModelProperty("使用部门编码")
    private String applyDeptCode;

    @ApiModelProperty("使用部门名称")
    private String applyDeptName;

    @ApiModelProperty("使用人编码")
    private String empCode;

    @ApiModelProperty("使用人名称")
    private String empName;

    @ApiModelProperty("存放地点编码")
    private String placeCode;

    @ApiModelProperty("存放地点名称")
    private String placeName;

    @ApiModelProperty("使用状况")
    private String applyStatus;

    @ApiModelProperty("查询条件说明")
    private String queryRemark;

    @ApiModelProperty("单据状态  1 未开始 2 已生成盘点清单 3 进行中 4 已结束")
    private String status;

    @ApiModelProperty("是否删除   0正常   2停用")
    private String delFlag;

    @ApiModelProperty("盘点说明")
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
    private List<String> deptIds;

}

