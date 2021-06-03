package com.cxnet.baseData.expfunc.bdExpfunc.domain;

import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 支出功能分类实体 bd_expfunc
 *
 * @author cxnet
 * @date 2020-08-17
 */
@Data
@ApiModel("支出功能分类")
public class BdExpfunc extends BaseEntity {

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String funcId;
    /**
     * 业务年度
     */
    @Excel(name = "业务年度")
    @ApiModelProperty("业务年度")
    private Long year;
    /**
     * 单位代码
     */
    @Excel(name = "单位代码")
    @ApiModelProperty("单位代码")
    private String unitCode;
    @ApiModelProperty("单位名称")
    private String unitName;
    @ApiModelProperty("单位id")
    private String unitId;
    /**
     * 功能分类代码
     */
    @Excel(name = "功能分类代码")
    @ApiModelProperty("功能分类代码")
    private String funcCode;
    /**
     * 功能分类名称
     */
    @Excel(name = "功能分类名称")
    @ApiModelProperty("功能分类名称")
    private String funcName;
    /**
     * 科目性质
     */
    @Excel(name = "科目性质")
    @ApiModelProperty("科目性质")
    private String subjectNature;
    /**
     * 是否末级
     */
    @Excel(name = "是否末级")
    @ApiModelProperty("是否末级")
    private String isLowest;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @Excel(name = "删除标志", readConverterExp = "0=代表存在,2=代表删除")
    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;
    /**
     * 父级代码
     */
    @Excel(name = "父级代码")
    @ApiModelProperty("父级代码")
    private String parentCode;
    /**
     * 父级id
     */
    @Excel(name = "父级id")
    @ApiModelProperty("父级id")
    private String parentId;
    /**
     * 状态（0正常 2停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,2=停用")
    @ApiModelProperty("状态（0正常 2停用）")
    private String status;

    public BdExpfunc() {
    }

    public BdExpfunc(Long year, String unitId) {
        this.year = year;
        this.unitId = unitId;
    }
}
