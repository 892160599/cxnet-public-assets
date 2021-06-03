package com.cxnet.rpc.service.supervise;


import java.io.Serializable;
import java.util.Map;

/**
 * @Auther: Administrator
 * @Date: 2021-3-5 17:45
 * @Description:
 */
public interface SysSuperviseMatterServiceRpc {

    /**
     * 添加监督任务
     *
     * @param superviseId
     * @param variables
     */
    void addSysSuperviseMatter(Serializable superviseId, Map<String, Object> variables);
}
