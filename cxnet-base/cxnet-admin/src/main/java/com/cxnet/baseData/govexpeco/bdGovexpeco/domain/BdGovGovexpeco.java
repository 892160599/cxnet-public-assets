package com.cxnet.baseData.govexpeco.bdGovexpeco.domain;

import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 政府/部门经济分类关系实体 bd_gov_expeco
 *
 * @author cxnet
 * @date 2020-08-17
 */
@Data
@ApiModel("政府/部门经济分类关系")
public class BdGovGovexpeco extends BaseEntity {

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
    /**
     * 年度
     */
    @ApiModelProperty("年度")
    private Long year;
    /**
     * 政府经济分类代码
     */
    @ApiModelProperty("政府经济分类代码")
    private String govExpecoCode;
    /**
     * 政府经济分类名称
     */
    @Excel(name = "政府经济分类名称")
    @ApiModelProperty("政府经济分类名称")
    private String govExpecoName;
    /**
     * 部门经济分类代码
     */
    @ApiModelProperty("部门经济分类代码")
    private String expecoCode;
    /**
     * 部门经济分类名称
     */
    @Excel(name = "部门经济分类名称")
    @ApiModelProperty("部门经济分类名称")
    private String expecoName;
    /**
     * 单位类型代码  1行政  2事业 3参公 9其它
     */
    @ApiModelProperty("单位类型代码  1行政  2事业 3参公 9其它")
    private String deptTypeCode;
    /**
     * 单位类型名称
     */
    @Excel(name = "单位类型名称")
    @ApiModelProperty("单位类型名称")
    private String deptTypeName;

}
