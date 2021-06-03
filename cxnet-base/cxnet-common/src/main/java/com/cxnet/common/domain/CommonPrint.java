package com.cxnet.common.domain;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通用的打印对象
 *
 * @author caixx
 */
@Data
@ApiModel(value = "打印对象")
public class CommonPrint {

    @ApiModelProperty(value = "单据id")
    private String id;

    @ApiModelProperty(value = "单位id")
    private String unitCode;

    @ApiModelProperty(value = "部门id")
    private String deptCode;

    @ApiModelProperty(value = "用户编码")
    private String userCode;

    @ApiModelProperty(value = "权限标识")
    private String perms;


}
