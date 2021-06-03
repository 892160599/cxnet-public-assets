package com.cxnet.ureport2.demo.config;

import com.bstek.ureport.console.UReportServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.servlet.Servlet;

/**
 * Servlet 配置类
 *
 * @author cxnet
 * @date 2019-04-23
 **/
@Configuration
@ImportResource("classpath:ureport-console-context.xml")
public class ServletConfig {

    @Bean
    public ServletRegistrationBean<Servlet> buildUReportServlet() {
        return new ServletRegistrationBean<>(new UReportServlet(), "/ureport/*");
    }
}
