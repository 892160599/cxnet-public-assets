package com.cxnet.framework.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(InvoiceProperties.class)
public class InvoiceAutoConfiguration {

    @Autowired(required = false)
    private InvoiceProperties invoiceProperties;


}
