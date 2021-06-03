package com.cxnet.framework.push;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "push")
public class PushProperties {

    /**
     * 用户创建应用时生成的应用唯一标识
     */
    private String appId;

    /**
     * 创建应用时生成的appkey
     */
    private String appKey;

    /**
     *
     */
    private String masterSecret;


}
