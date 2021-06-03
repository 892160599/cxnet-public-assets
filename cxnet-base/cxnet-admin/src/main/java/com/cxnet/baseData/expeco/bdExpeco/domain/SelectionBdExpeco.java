package com.cxnet.baseData.expeco.bdExpeco.domain;

import com.cxnet.common.utils.poi.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 选用部门预算经济分类实体 bd_expfunc
 *
 * @author cxnet
 * @date 2020-08-17
 */
@Data
@ApiModel("选用部门预算经济分类实体")
public class SelectionBdExpeco {

    /**
     * 部门预算经济分类id
     */
    @ApiModelProperty("部门预算经济分类id")
    private String[] expecoCodes;
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
