package com.cxnet.framework.minio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cxnet.framework.push.PushProperties;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class MinioAutoConfiguration {

    @Autowired(required = false)
    private MinioProperties minioProp;


    @Autowired(required = false)
    private PushProperties pushProperties;

    /**
     * 获取 MinioClient
     *
     * @return
     * @throws InvalidPortException
     * @throws InvalidEndpointException
     */
    @Bean
    public MinioClient minioClient() throws InvalidPortException, InvalidEndpointException {

        return MinioClient.builder().endpoint(minioProp.getEndpoint()).credentials(minioProp.getAccessKey(), minioProp.getSecretKey()).build();
    }
}
