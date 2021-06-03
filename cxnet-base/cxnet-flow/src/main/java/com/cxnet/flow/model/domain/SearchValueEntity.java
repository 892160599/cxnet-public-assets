package com.cxnet.flow.model.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 待办已办查询条件实体
 *
 * @author Aowen
 * @date 2021/5/12
 */
@Data
public class SearchValueEntity {

    @ApiModelProperty("查询条件，：开始时间")
    private Date startTime;
    @ApiModelProperty("查询条件，：结束时间")
    private Date endTime;
    @ApiModelProperty("查询条件，：模块名称（单据类型）")
    private String modelName;
    @ApiModelProperty("查询条件")
    private String searchValue;
    @ApiModelProperty("查询条件，：部门编码")
    private String deptCode;
    @ApiModelProperty("查询条件，：开始时间")
    private String billNo;
    @ApiModelProperty("查询条件，：最小金额")
    private BigDecimal minAmt;
    @ApiModelProperty("查询条件，：最大金额")
    private BigDecimal maxAmt;
    @ApiModelProperty("查询条件，：单据状态")
    private String status;
    @ApiModelProperty("查询条件，：父级路径，即属于哪个模块下的 例如 contract  合同 ")
    private String parentPath;

}
