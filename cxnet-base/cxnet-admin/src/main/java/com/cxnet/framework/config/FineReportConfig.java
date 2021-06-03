package com.cxnet.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义存储器的配置类
 *
 * @author cxnet
 * @date
 **/
@Data
@Slf4j
@Configuration
public class FineReportConfig {

    /**
     * 报表模板文件存储路径
     */
    @Value("${finereport.fileStoreDir}")
    private String fileStoreDir;

    /**
     * 默认文件模板
     */
    @Value("${finereport.defaultFile}")
    private String defaultFile;


}