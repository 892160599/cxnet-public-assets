package com.cxnet.baseData.purItem.domain;

import com.alibaba.fastjson.JSON;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 采购品目实体 db_pur_item
 *
 * @author caixx
 * @date 2020-07-21
 */
@Data
@ApiModel("采购品目")
public class BdPurItem extends BaseEntity {

    /**
     * 采购品目id
     */
    @ApiModelProperty("采购品目id")
    private String itemId;
    /**
     * 采购品目代码
     */
    @Excel(name = "采购品目代码")
    @ApiModelProperty("采购品目代码")
    private String itemCode;
    /**
     * 采购品目名称
     */
    @Excel(name = "采购品目名称")
    @ApiModelProperty("采购品目名称")
    private String itemName;
    /**
     * 上级id
     */
    @Excel(name = "上级id")
    @ApiModelProperty("上级id")
    private String parentId;
    /**
     * 上级代码
     */
    @Excel(name = "上级代码")
    @ApiModelProperty("上级代码")
    private String parentCode;
    /**
     * 采购年度
     */
    @Excel(name = "采购年度")
    @ApiModelProperty("采购年度")
    private Long purYear;
    /**
     * 采购类别
     */
    @Excel(name = "采购类别")
    @ApiModelProperty("采购类别")
    private String purType;
    /**
     * 是否重要品目(0是 2否)
     */
    @Excel(name = "是否重要品目(0是 2否)")
    @ApiModelProperty("是否重要品目(0是 2否)")
    private String isPrecious;
    /**
     * 品目类型
     */
    @Excel(name = "品目类型")
    @ApiModelProperty("品目类型")
    private String itemType;
    /**
     * 计量单位
     */
    @Excel(name = "计量单位")
    @ApiModelProperty("计量单位")
    private String measurement;
    /**
     * 采购组织形式
     */
    @Excel(name = "采购组织形式")
    @ApiModelProperty("采购组织形式")
    private String purForm;
    /**
     * 是否末级
     */
    @Excel(name = "是否末级")
    @ApiModelProperty("是否末级")
    private String isLowest;
    /**
     * 采购品目状态（0正常 2停用）
     */
    @Excel(name = "采购品目状态", readConverterExp = "0=正常,2=停用")
    @ApiModelProperty("采购品目状态（0正常 2停用）")
    private String status;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;
    /**
     * 描述
     */
    @Excel(name = "描述")
    @ApiModelProperty("描述")
    private String description;
    /**
     * 上级名称
     */
    @ApiModelProperty("上级名称")
    private String parentName;
    /**
     * 限额标准
     */
    @ApiModelProperty("限额标准")
    private BigDecimal limitStandard;
    @ApiModelProperty("所属单位id")
    private String unitId;
    @ApiModelProperty("所属单位编码")
    private String unitCode;
    @ApiModelProperty("所属单位名称")
    private String unitName;

    public String toJSONString() {
        return JSON.toJSONString(this);
    }

    public BdPurItem() {
    }

    public BdPurItem(Long purYear, String unitId) {
        this.purYear = purYear;
        this.unitId = unitId;
    }

}
