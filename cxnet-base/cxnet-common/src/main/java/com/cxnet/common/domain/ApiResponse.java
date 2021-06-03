package com.cxnet.common.domain;

import lombok.Data;

/**
 * @program: cxnet-direct-funds
 * @description:
 * @author: Mr.Cai
 * @create: 2021-02-25 16:21
 **/
@Data
public class ApiResponse {

    private String data;
    private String noise;
    private String sign;

}
