package com.cxnet.baseData.govexpeco.bdGovexpeco.domain;

import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 政府预算经济分类实体 bd_govexpeco
 *
 * @author cxnet
 * @date 2020-08-17
 */
@Data
@ApiModel("政府预算经济分类")
public class BdGovexpeco extends BaseEntity {

    /**
     * 政府经济分类ID
     */
    @Excel(name = "政府经济分类ID")
    @ApiModelProperty("政府经济分类ID")
    private String govId;
    /**
     * 业务年度
     */
    @Excel(name = "业务年度")
    @ApiModelProperty("业务年度")
    private Long year;
    /**
     * 政府经济分类代码
     */
    @Excel(name = "政府经济分类代码")
    @ApiModelProperty("政府经济分类代码")
    private String govCode;
    /**
     * 政府经济分类名称
     */
    @Excel(name = "政府经济分类名称")
    @ApiModelProperty("政府经济分类名称")
    private String govName;
    /**
     * 是否末级
     */
    @Excel(name = "是否末级")
    @ApiModelProperty("是否末级")
    private String isLowest;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;
    /**
     * 父级代码
     */
    @Excel(name = "父级代码")
    @ApiModelProperty("父级代码")
    private String parentCode;
    /**
     * 父级ID
     */
    @Excel(name = "父级ID")
    @ApiModelProperty("父级ID")
    private String parentId;
    /**
     * 状态（0正常 2停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,2=停用")
    @ApiModelProperty("状态（0正常 2停用）")
    private String status;
    /**
     * 用途
     */
    @Excel(name = "用途")
    @ApiModelProperty("用途")
    private String purpose;
    @ApiModelProperty("政府/部门经济分类关系表")
    private List<BdGovGovexpeco> bdGovGovexpecos;

    /**
     * 单位编码
     */
    @Excel(name = "单位编码")
    @ApiModelProperty("单位编码")
    private String unitCode;
    /**
     * 单位名称
     */
    @Excel(name = "单位名称")
    @ApiModelProperty("单位名称")
    private String unitName;
    /**
     * 单位id
     */
    @Excel(name = "单位id")
    @ApiModelProperty("单位id")
    private String unitId;
}
