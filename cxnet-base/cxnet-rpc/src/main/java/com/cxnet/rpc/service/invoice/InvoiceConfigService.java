package com.cxnet.rpc.service.invoice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.rpc.domain.invoice.InvoiceConfig;
import com.cxnet.rpc.domain.invoice.InvoiceConfigVo;

import java.io.Serializable;
import java.util.List;

/**
 * 发票服务配置(InvoiceConfig)表服务接口
 *
 * @author renwei
 * @since 2021-05-14 17:31:15
 */
public interface InvoiceConfigService extends IService<InvoiceConfig> {

    int hasCommonCode(String serviceCode);

    String saveInvoiceConfigVo(InvoiceConfigVo invoiceConfigVo);

    InvoiceConfigVo selectConfigVo(Serializable id);

    String updateInvoiceConfigVo(InvoiceConfigVo invoiceConfigVo);

    List<InvoiceConfig> selectUsingConfig();

    int hasUsingCode(String service);

    InvoiceConfig selectTwoLevel(String serviceLevel);
}