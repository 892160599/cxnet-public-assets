package com.cxnet.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.request.RequestContextListener;

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
@ComponentScan(basePackages = {"com.cxnet"})
//@MapperScan("com.cxnet") // Mapper扫描
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BootstrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootstrapApplication.class, args);
        log.info(">>>>>>  The expenses service started successfully.  >>>>>> 内控服务启动成功！");
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

}
