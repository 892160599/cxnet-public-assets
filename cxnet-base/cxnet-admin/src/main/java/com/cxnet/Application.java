package com.cxnet;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动程序
 *
 * @author cxnet
 */
@Slf4j // 日志
@EnableAsync // 开启异步
@EnableCaching // 开启缓存
@EnableTransactionManagement // 开启事务
@EnableScheduling // 开启定时
@MapperScan("com.cxnet.**.mapper") // Mapper扫描
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info(">>>>>>  The system service started successfully.  >>>>>>  系统服务启动成功！");
    }


}
