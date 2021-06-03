package com.cxnet.rpc.service;

import com.cxnet.rpc.domain.ContactUnit;

import java.util.List;

/**
 * 系统往来单位Service接口
 *
 * @author
 * @date
 */
public interface ContactUnitServiceRpcI {

    /**
     * 查询系统往来客商
     *
     * @param contactUnit 往来客商
     * @return
     */
    List<ContactUnit> selectContactUnit(ContactUnit contactUnit);


    /**
     * 查询系统往来单位
     *
     * @param unitId 系统往来单位ID
     * @return 系统往来单位
     */
    ContactUnit selectContactUnitById(String unitId);

    /**
     * 保存往来单位
     *
     * @param contactUnit
     * @return
     */
    ContactUnit saveContactUnit(ContactUnit contactUnit);

    /**
     * 修改往来单位
     *
     * @param contactUnit
     * @return
     */
    int updateContactUnit(ContactUnit contactUnit);

    /**
     * 生成往来单位
     *
     * @param unitName      往来单位名称
     * @param sysUnit       系统单位
     * @param accountBank   开户行
     * @param accountNumber 账号
     */
    void insertTheUnit(String unitName, String sysUnit, String accountBank, String accountNumber, String unitCode);

}
