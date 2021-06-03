package com.cxnet.framework.push;

import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.GtApiConfiguration;
import com.getui.push.v2.sdk.api.AuthApi;
import com.getui.push.v2.sdk.api.PushApi;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PushProperties.class)
public class PushAutoConfiguration {

    @Autowired(required = false)
    private PushProperties pushProperties;

    /**
     * 获取 GtApiConfiguration
     *
     * @return
     */
    @Bean
    public GtApiConfiguration getApiConfiguration() {
        GtApiConfiguration apiConfiguration = new GtApiConfiguration();
        //填写应用配置
        apiConfiguration.setAppId(pushProperties.getAppId());
        apiConfiguration.setAppKey(pushProperties.getAppKey());
        apiConfiguration.setMasterSecret(pushProperties.getMasterSecret());
        // 接口调用前缀，请查看文档: 接口调用规范 -> 接口前缀, 可不填写appId
        apiConfiguration.setDomain("https://restapi.getui.com/v2/");
        return apiConfiguration;
    }


    /**
     * 获取 GtApiConfiguration
     *
     * @return
     */
    @Bean
    public ApiHelper getApiHelper() {
        GtApiConfiguration apiConfiguration = getApiConfiguration();
        // 实例化ApiHelper对象，用于创建接口对象
        ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
        return apiHelper;
    }

    /**
     * 获取 PushApi
     *
     * @return
     */
    @Bean
    public PushApi getPushApi() {
        ApiHelper apiHelper = getApiHelper();
        PushApi pushApi = apiHelper.creatApi(PushApi.class);
        return pushApi;
    }

    /**
     * 获取 AuthApi
     *
     * @return
     */
    @Bean
    public AuthApi getAuthApi() {
        ApiHelper apiHelper = getApiHelper();
        AuthApi authApi = apiHelper.creatApi(AuthApi.class);
        return authApi;
    }

}
