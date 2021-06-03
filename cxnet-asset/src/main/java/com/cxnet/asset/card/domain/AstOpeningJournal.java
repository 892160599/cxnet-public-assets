package com.cxnet.asset.card.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 期初卡片导入日志表
 *
 * @author zhangyl
 * @since 2021-04-22 16:29:33
 */
@Data
@ApiModel(description = "期初卡片导入日志表")
public class AstOpeningJournal extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 899878131278717237L;

    @ApiModelProperty("主键id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("资产卡片id")
    private String astId;

    @ApiModelProperty("期初卡片导入操作记录id")
    private String openingId;

    @ApiModelProperty("年度")
    private Integer fiscal;

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

    @ApiModelProperty("导入人编码")
    private String confirmedCode;

    @ApiModelProperty("导入人名称")
    private String confirmedName;

    @ApiModelProperty("资产名称")
    private String assetName;

    @ApiModelProperty("卡片行号")
    private String cardLine;

    @ApiModelProperty("卡片数量")
    private String qty;

    @ApiModelProperty("卡片原值")
    private BigDecimal cost;

    @ApiModelProperty("状态 1 生成成功  2 生成失败")
    private String status;

    @ApiModelProperty("日志信息")
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

}

