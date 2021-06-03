package com.cxnet.rpc.service.invoice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.rpc.domain.invoice.InvoiceConfig;
import com.cxnet.rpc.domain.invoice.InvoiceIndex;
import com.cxnet.rpc.domain.invoice.InvoiceIndexVo;

import java.io.Serializable;
import java.util.List;

/**
 * 发票主表(InvoiceIndex)表服务接口
 *
 * @author renwei
 * @since 2021-05-17 17:11:06
 */
public interface InvoiceIndexService extends IService<InvoiceIndex> {

    List<InvoiceIndex> selectMyInvoice(InvoiceIndex invoiceIndex);

    void updateDelFlag(String v);

    String invocieTransfe(String id, String transfer);

    InvoiceIndexVo saveiInvoiceIndex(InvoiceIndexVo invoiceIndexVo);

    InvoiceIndexVo getOneInvoice(Serializable id);

    int getInvoiceByCode(String invoiceCode);

    InvoiceIndexVo saveCheckInvoiceVo(InvoiceIndexVo invoiceIndexVo1);

    List<InvoiceIndex> selectCheckData(InvoiceIndex invoiceIndex);

    List<InvoiceIndex> selectUnitInvoice(InvoiceIndex invoiceIndex);

    InvoiceIndexVo testOcr(InvoiceConfig invoiceConfig);

    InvoiceIndexVo executeRes(InvoiceIndexVo invoiceIndexVo);

    int hasExistInvoiceCode(String invoiceCode);

    InvoiceIndexVo vatInvoice(InvoiceConfig v);

    List<InvoiceIndex> selectMyExpInvoice(InvoiceIndex invoiceIndex);
}