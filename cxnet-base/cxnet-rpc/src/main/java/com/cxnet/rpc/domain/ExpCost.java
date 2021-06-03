package com.cxnet.rpc.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 报销费用明细子表
 *
 * @author makejava
 * @since 2020-09-14 10:41:50
 */
@Data
@ApiModel(description = "报销费用明细子表")
public class ExpCost extends RpcBaseEntity implements Serializable {
    private static final long serialVersionUID = -71445140095286701L;

    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("费用明细代码")
    @NotEmpty(message = "费用明细不能为空！")
    private String costCode;
    @ApiModelProperty("费用明细名称")
    @NotEmpty(message = "费用明细不能为空！")
    private String costName;
    @NotEmpty(message = "报销信息不能为空！")
    @ApiModelProperty("报销信息id")
    private String expId;
    @ApiModelProperty("报销单据号(编码器生成)")
    private String expBill;
    @ApiModelProperty("支出内容")
    private String costContent;
    @ApiModelProperty("报销金额")
    @NotNull(message = "费用明细申请金额不能为空！")
    private BigDecimal costMoney;
    @ApiModelProperty("核定金额")
    private BigDecimal checkMoney;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("排序字段")
    private String expExtend1;
    @ApiModelProperty("扩展字段2")
    private String expExtend2;
    @ApiModelProperty("扩展字段3")
    private String expExtend3;
    @ApiModelProperty("扩展字段4")
    private String expExtend4;
    @ApiModelProperty("扩展字段5")
    private String expExtend5;
    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;
    @TableField(exist = false)
    private List<ExpCostSplit> expCostSplits;
}