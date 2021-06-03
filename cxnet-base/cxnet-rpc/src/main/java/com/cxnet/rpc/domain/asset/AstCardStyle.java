package com.cxnet.rpc.domain.asset;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.rpc.domain.RpcBaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 单据样式配置表
 *
 * @author makejava
 * @since 2021-03-24 18:28:49
 */
@Data
@ApiModel(description = "单据样式配置表")
public class AstCardStyle extends RpcBaseEntity implements Serializable {
    private static final long serialVersionUID = -77249813703112872L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "样式编码", required = true)
    private String cardStyleCode;

    @ApiModelProperty(value = "样式名称", required = true)
    private String cardStyleName;

    @ApiModelProperty("资产类型（1G固定资产 2W无形资产 ）")
    private String assetType;

    @ApiModelProperty("单据类型")
    private String billType;

    @ApiModelProperty("单位id")
    private String unitId;

    @ApiModelProperty("年度")
    private String fiscal;

    @ApiModelProperty("状态（0正常 2停用）")
    private String status;

    @ApiModelProperty("是否默认（0是 2否）")
    private String isDefault;

    @ApiModelProperty("是否预置（0是 2否）")
    private String isPreset;

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

    @ApiModelProperty("单据样式（1pc样式2 移动端样式）")
    private String style;

    @ApiModelProperty("删除标识(0表示存在 2表示删除 )")
    private String delFlag;

    @ApiModelProperty("备注")
    private String remark;


}