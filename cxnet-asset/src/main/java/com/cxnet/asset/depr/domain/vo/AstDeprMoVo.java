package com.cxnet.asset.depr.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: cxnet-internal-control
 * @description:
 * @author: Mr.Cai
 * @create: 2021-04-08 16:53
 **/
@Data
public class AstDeprMoVo {

    @ApiModelProperty("折旧月份")
    private Integer deprMo;

    @ApiModelProperty("是否计提/摊销 0已折旧 2未折旧")
    private String isDepr = "2";

    @ApiModelProperty("是否存在补计提 0有 2无")
    private String isSupplementaryProvision = "2";
}
