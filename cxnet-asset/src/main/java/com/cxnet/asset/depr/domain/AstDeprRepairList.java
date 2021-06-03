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
 * 资产补计提折旧明细表
 *
 * @author caixx
 * @since 2021-04-16 18:07:52
 */
@Data
@ApiModel(description = "资产补计提折旧明细表")
public class AstDeprRepairList implements Serializable {
    private static final long serialVersionUID = 867599560806421261L;

    @ApiModelProperty("主键id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("资产折旧主表id")
    private String astDeprId;

    @ApiModelProperty("资产卡片id")
    private String astId;

    @ApiModelProperty("卡片编号")
    private String astCode;

    @ApiModelProperty("资产名称")
    private String assetName;

    @ApiModelProperty("折旧年度")
    private Integer fiscal;

    @ApiModelProperty("折旧月份")
    private Integer deprMo;

    @ApiModelProperty("资产类别id")
    private String categoryId;

    @ApiModelProperty("资产类别编码")
    private String categoryCode;

    @ApiModelProperty("资产类别名称")
    private String categoryName;

    @ApiModelProperty("资产大类代码，存放一级1,2,3,4,5,6")
    private String classCode;

    @ApiModelProperty("资产大类名称，存放通用设备、专用设备")
    private String className;

    @ApiModelProperty("折旧方法")
    private String depMethod;

    @ApiModelProperty("价值")
    private BigDecimal cost;

    @ApiModelProperty("累计折旧")
    private BigDecimal depTotal;

    @ApiModelProperty("已计提月份")
    private Integer depedMo;

    @ApiModelProperty("账面净值")
    private BigDecimal netValue;

    @ApiModelProperty("月折旧额")
    private BigDecimal depMoValue;

    @ApiModelProperty("待折旧月份")
    private Integer depreciationMo;

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

    @ApiModelProperty("卡片单据号(编码器生成)")
    private String cardBill;

    @ApiModelProperty("本次折旧")
    private BigDecimal thisValue;

    @ApiModelProperty("净残值率")
    private BigDecimal resiRate;

    @ApiModelProperty("使用部门id")
    private String departmentId;

    @ApiModelProperty("使用部门代码")
    private String departmentCode;

    @ApiModelProperty("使用部门名称")
    private String departmentName;

    @ApiModelProperty("购置日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date acquisitionDate;

    @ApiModelProperty("财务入账日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date vouDate;

    @ApiModelProperty("设计使用月份")
    private Integer useLife;

    @ApiModelProperty("数量")
    private Integer qty;

    @ApiModelProperty("原已计提月份")
    private Integer cardDepedMo;

    @ApiModelProperty("原累计折旧")
    private BigDecimal cardDepTotal;

    @ApiModelProperty("原待折旧月份")
    private Integer cardDepreciationMo;

    @ApiModelProperty("原月折旧额")
    private BigDecimal cardDepMoValue;

    @ApiModelProperty("原账面净值")
    private BigDecimal cardNetValue;

    @ApiModelProperty("投入使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startUsedate;

    @ApiModelProperty("本次折旧月份")
    private Integer thisMo;

    @ApiModelProperty("补计提金额")
    private BigDecimal deprRepairValue;

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