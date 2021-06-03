package com.cxnet.flow.utils;

import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.flow.model.mapper.SysModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @program: cxnet
 * @description:
 * @author: Mr.Cai
 * @create: 2020-08-14 15:17
 **/
@Component
public class BillUtils {


    @Autowired(required = false)
    private SysModelMapper sysModelMapper;

    public static BillUtils billUtils;

    @PostConstruct
    public void init() {
        billUtils = this;
        billUtils.sysModelMapper = sysModelMapper;
    }

    /**
     * 查询单据绑定的流程key
     *
     * @param billTypeCode 单据类型编码
     * @return
     */
    public static String getProcessKey(String billTypeCode) {
        String key = billUtils.sysModelMapper.selectProcessKeyByModelCode(billTypeCode);
        if (StringUtils.isEmpty(key)) {
            throw new CustomException("未查询到该单据绑定的审批流，请确认！");
        }
        return key;
    }

    /**
     * 根据单据类型和单位id查询单据绑定的流程key
     *
     * @param billTypeCode 单据类型编码
     * @return
     */
    public static String getProcessKeyByUnit(String billTypeCode, String unitId) {
        String key = billUtils.sysModelMapper.selectProcessKeyByModelCodeAndUnitId(billTypeCode, unitId);
        if (StringUtils.isEmpty(key)) {
            key = billUtils.sysModelMapper.selectProcessKeyByModelCode(billTypeCode);
        }
        if (StringUtils.isEmpty(key)) {
            throw new CustomException("未查询到该单据绑定的审批流，请确认！");
        }
        return key;
    }

    /**
     * 查询单据编号器编码
     *
     * @return
     */
    public static String getRuleCode(String billTypeCode) {
        String ruleCode = billUtils.sysModelMapper.selectRuleCodeByModelCode(billTypeCode);
        if (StringUtils.isEmpty(ruleCode)) {
            throw new CustomException("未查询到该单据绑定的编号器，请确认！");
        }
        return ruleCode;
    }


    /**
     * 查询单据附件类型值集编码
     *
     * @return
     */
    public static String getDictTypeCode(String billTypeCode) {
        return billUtils.sysModelMapper.selectDictTypeCodeByModelCode(billTypeCode);
    }


}
