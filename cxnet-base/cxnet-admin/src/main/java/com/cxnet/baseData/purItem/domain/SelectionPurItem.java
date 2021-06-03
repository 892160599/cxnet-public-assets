package com.cxnet.baseData.purItem.domain;

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
@ApiModel("选用采购品目实体")
public class SelectionPurItem {

    /**
     * 采购品目id
     */
    @ApiModelProperty("采购品目id")
    private String[] id;
    /**
     * 业务年度
     */
    @Excel(name = "业务年度")
    @ApiModelProperty("业务年度")
    private Long purYear;
    /**
     * 单位id
     */
    @Excel(name = "单位id")
    @ApiModelProperty("单位id")
    private String unitId;

}
