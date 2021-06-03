package com.cxnet.framework.invoice;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ocr")
public class InvoiceProperties {

    /**
     * invoice 识别文件映射ip+端口
     */
    private String address;

}
