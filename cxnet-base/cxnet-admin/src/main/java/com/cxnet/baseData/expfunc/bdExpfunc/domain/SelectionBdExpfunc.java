package com.cxnet.baseData.expfunc.bdExpfunc.domain;

import com.cxnet.common.utils.poi.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 选用支出功能分类实体 bd_expfunc
 *
 * @author cxnet
 * @date 2020-08-17
 */
@Data
@ApiModel("选用支出功能分类实体")
public class SelectionBdExpfunc {

    /**
     * 功能分类id
     */
    @ApiModelProperty("功能分类id")
    private String[] funcCode;
    /**
     * 业务年度
     */
    @Excel(name = "业务年度")
    @ApiModelProperty("业务年度")
    private Long year;
    /**
     * 单位id
     */
    @Excel(name = "单位id")
    @ApiModelProperty("单位id")
    private String unitId;

}
