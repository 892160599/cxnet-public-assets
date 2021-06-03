package com.cxnet.ureport2.demo.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义存储器的配置类
 *
 * @author cxnet
 * @date 2019-04-24
 **/
@Data
@Slf4j
@Configuration
@ConfigurationProperties("ureport")
public class UReportConfig {
    /**
     * 文件名前缀，用于标识不同的存储器存储的文件
     * 否则在查看文件列表的时候只能看到其中一个存储器的文件
     */
    private String metaPrefix = "meta:";
    private String fullPrefix = "full:";

    /**
     * 是否禁用DBMetaReportProvider
     */
    private boolean disableDBMetaProvider = false;

    /**
     * 是否禁用DBFullReportProvider
     */
    private boolean disableDBFullProvider = false;

    /**
     * 是否禁用FileReportProvider
     */
    private boolean disableFileProvider = false;

    /**
     * 报表模板文件存储路径
     */
    private String fileStoreDir = "ureportfiles";
}
