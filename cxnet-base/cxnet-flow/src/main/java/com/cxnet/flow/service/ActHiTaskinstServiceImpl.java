package com.cxnet.flow.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.common.constant.ProcessConstant;
import com.cxnet.flow.domain.ActHiTaskinst;
import com.cxnet.flow.mapper.ActHiTaskinstMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * (ActHiTaskinst)表服务实现类
 *
 * @author makejava
 * @since 2021-05-21 10:10:04
 */
@Service
public class ActHiTaskinstServiceImpl extends ServiceImpl<ActHiTaskinstMapper, ActHiTaskinst> implements ActHiTaskinstService {

    @Autowired(required = false)
    private ActHiTaskinstMapper actHiTaskinstMapper;

    /**
     * 修改节点审批信息（直接操作表）
     *
     * @param processinstid 流程id
     * @param opinion       审批意见
     * @param isBack        是否为退回 true false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNodeInfo(String processinstid, String opinion, Boolean isBack) {
        // 查询流程信息
        List<ActHiTaskinst> actHiTaskinsts = actHiTaskinstMapper.selectListByPid(processinstid);
        if (CollectionUtils.isNotEmpty(actHiTaskinsts)) {
            ActHiTaskinst actHiTaskinst = actHiTaskinsts.get(0);
            String description = actHiTaskinst.getDescription_();
            JSONObject jsonObject = JSON.parseObject(description);
            // 更新节点信息
            jsonObject.remove("opinion");
            jsonObject.put("opinion", opinion);
            if (isBack) {
                jsonObject.remove("actStatus");
                jsonObject.remove("submitFlag");
                jsonObject.put("actStatus", ProcessConstant.STATUS_9);
                jsonObject.put("submitFlag", ProcessConstant.REFUSE);
            }
            actHiTaskinst.setDescription_(JSONObject.toJSONString(jsonObject));
            actHiTaskinst.setEndTime_(new Date());
            actHiTaskinstMapper.updateHiById(actHiTaskinst);
        }

    }

}