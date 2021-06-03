package com.cxnet.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.flow.domain.ActHiTaskinst;

/**
 * (ActHiTaskinst)表服务接口
 *
 * @author makejava
 * @since 2021-05-21 10:10:01
 */
public interface ActHiTaskinstService extends IService<ActHiTaskinst> {

    /**
     * 修改节点信息（直接操作表）
     *
     * @param processinstid 流程id
     * @param opinion       审批意见
     * @param isBack        是否为退回 true false
     */
    void updateNodeInfo(String processinstid, String opinion, Boolean isBack);

}