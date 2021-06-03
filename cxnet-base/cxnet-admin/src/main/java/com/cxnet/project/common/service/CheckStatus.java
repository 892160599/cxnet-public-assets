package com.cxnet.project.common.service;

import com.cxnet.common.constant.ProcessConstant;
import com.cxnet.common.constant.UserConstants;
import com.cxnet.common.exception.CustomException;
import com.cxnet.project.system.config.domain.SysConfig;
import com.cxnet.project.system.config.service.SysConfigServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Aowen
 * @date 2021/5/27
 */
@Component
public class CheckStatus {

    @Autowired(required = false)
    private SysConfigServiceI sysConfigServiceI;

    private static final String CONFIG_ID = "3";

    /**
     * 校验状态
     *
     * @param billStatus  单据状态
     * @param checkStatus 校验状态列表
     */
    public void checkStatus(String billStatus, String... checkStatus) {
        SysConfig sysConfig = sysConfigServiceI.selectConfigById(CONFIG_ID);
        if (checkStatus != null) {
            List<String> statusList = new ArrayList<>(Arrays.asList(checkStatus));
            // 是否开启工作流审批节点可编辑单据
            if (sysConfig != null && UserConstants.YES.equals(sysConfig.getConfigValue())) {
                statusList.add(ProcessConstant.STATUS_1);
            }
            if (!statusList.contains(billStatus)) {
                throw new CustomException("单据状态不合法！");
            }
        }
    }

    /**
     * 校验状态
     *
     * @param billStatus  单据状态集合
     * @param checkStatus 校验状态列表
     */
    public void checkStatus(List<String> billStatus, String... checkStatus) {
        SysConfig sysConfig = sysConfigServiceI.selectConfigById(CONFIG_ID);
        if (checkStatus != null) {
            List<String> statusList = new ArrayList<>(Arrays.asList(checkStatus));
            // 是否开启工作流审批节点可编辑单据
            if (sysConfig != null && UserConstants.YES.equals(sysConfig.getConfigValue())) {
                statusList.add(ProcessConstant.STATUS_1);
            }
            for (String status : billStatus) {
                if (!statusList.contains(status)) {
                    throw new CustomException("单据状态不合法！");
                }
            }
        }
    }

}
