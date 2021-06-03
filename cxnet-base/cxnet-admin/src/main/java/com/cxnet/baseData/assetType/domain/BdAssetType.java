package com.cxnet.baseData.assetType.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 资产类别表
 *
 * @author zhangyl
 * @since 2021-03-24 18:07:53
 */
@Data
@ApiModel(description = "资产类别表")
public class BdAssetType extends BaseEntity {
    private static final long serialVersionUID = 136636758242422194L;

    @ApiModelProperty("资产类别id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String assetId;

    @ApiModelProperty("资产类别代码")
    private String assetCode;

    @ApiModelProperty("资产类别名称")
    private String assetName;

    @ApiModelProperty("上级id")
    private String parentId;

    @ApiModelProperty("上级代码")
    private String parentCode;

    @ApiModelProperty("是否末级")
    private String isLowest;

    @ApiModelProperty("资产类别状态（0正常 1停用）")
    private String status;

    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;

    @ApiModelProperty("单位id")
    private String unitId;

    @ApiModelProperty("单位代码")
    private String unitCode;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("所属资产类型")
    private String assetType;

    @ApiModelProperty("资产类别编码（国标）")
    private String assetCodegb;

    @ApiModelProperty("资产类别名称（国标）")
    private String assetNamegb;

    @ApiModelProperty("预计使用年限")
    private Integer assetUselife;

    @ApiModelProperty("净残值率")
    private Integer assetNetsalvage;

    @ApiModelProperty("计量单位")
    private String measurement;

    @ApiModelProperty("单据样式")
    private String assetPattern;

    @ApiModelProperty("折旧方法")
    private String depreciationMethod;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("所属资产大类")
    private String classification;

    public BdAssetType() {

    }

    public BdAssetType(String unitId) {
        this.unitId = unitId;
    }

}

