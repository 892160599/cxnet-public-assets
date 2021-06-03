package com.cxnet.rpc.service.zdzjsubsidize;


import com.cxnet.common.domain.ApiResponse;
import org.springframework.stereotype.Component;

/**
 * @Auther: Administrator
 * @Date: 2021-3-19 17:45
 * @Description:
 */
@Component
public interface ZdzjsubsidizeRpc {

    ApiResponse bulidApiResponse(boolean result, String message);

}
