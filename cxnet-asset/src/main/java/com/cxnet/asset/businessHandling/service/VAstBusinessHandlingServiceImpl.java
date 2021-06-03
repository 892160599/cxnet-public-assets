package com.cxnet.asset.businessHandling.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.businessHandling.domain.VAstBusinessHandling;
import com.cxnet.asset.businessHandling.mapper.VAstBusinessHandlingMapper;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.flow.model.domain.SysModel;
import com.cxnet.flow.model.mapper.SysModelMapper;
import com.cxnet.flow.utils.TaskUtils;
import com.cxnet.framework.security.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 资产业务办理(VAstBusinessHandling)表服务实现类
 *
 * @author caixx
 * @since 2021-03-31 16:04:18
 */
@Service
public class VAstBusinessHandlingServiceImpl extends ServiceImpl<VAstBusinessHandlingMapper, VAstBusinessHandling> implements VAstBusinessHandlingService {

    @Resource
    private SysModelMapper sysModelMapper;

    @Override
    public List<SysModel> getAstModel() {
        SysModel sysModel = sysModelMapper.selectSysModelByBillTypeCode("ast");
        String modelId = sysModel.getModelId();
        sysModel = new SysModel();
        sysModel.setParentId(modelId);
        return sysModelMapper.selectSysModelList(sysModel);
    }

    /**
     * 分页查询所有数据(我的事项)
     *
     * @param vAstBusinessHandling
     * @return
     */
    @Override
    public List<VAstBusinessHandling> selectAssetMatter(VAstBusinessHandling vAstBusinessHandling) {
        String status = StringUtils.isEmpty(vAstBusinessHandling.getStatus()) ? "" : vAstBusinessHandling.getStatus();
        List<VAstBusinessHandling> astCheckBills = new ArrayList<>();
        List<String> runTaskByUserNameAndStatus = null;
        vAstBusinessHandling.setCreateBy(SecurityUtils.getUsername());
        switch (status) {
            //草稿
            case "0":
                astCheckBills = this.baseMapper.selectAssetMatter(vAstBusinessHandling);
                break;
            // 待办
            case "1":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    vAstBusinessHandling.setPiids(runTaskByUserNameAndStatus);
                    astCheckBills = this.baseMapper.selectAssetMatter(vAstBusinessHandling);
                }
                break;
            // 已办
            case "2":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    vAstBusinessHandling.setPiids(runTaskByUserNameAndStatus);
                    astCheckBills = this.baseMapper.selectAssetMatter(vAstBusinessHandling);
                }
                break;
            // 终审
            case "3":
                vAstBusinessHandling.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("2"));
                astCheckBills = this.baseMapper.selectAssetMatter(vAstBusinessHandling);
                break;
            // 全部
            case "":
                vAstBusinessHandling.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("3"));
                astCheckBills = this.baseMapper.selectAssetMatter(vAstBusinessHandling);
                break;
            default:
                throw new CustomException("单据状态未定义!");
        }
        return astCheckBills;
    }

    /**
     * 分页查询所有数据(业务审批)
     *
     * @param vAstBusinessHandling
     * @return
     */
    @Override
    public List<VAstBusinessHandling> selectAssetAudit(VAstBusinessHandling vAstBusinessHandling) {
        String status = StringUtils.isEmpty(vAstBusinessHandling.getStatus()) ? "" : vAstBusinessHandling.getStatus();
        List<VAstBusinessHandling> astCheckBills = new ArrayList<>();
        List<String> runTaskByUserNameAndStatus = null;
        switch (status) {
            // 待办
            case "1":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    vAstBusinessHandling.setPiids(runTaskByUserNameAndStatus);
                    astCheckBills = this.baseMapper.selectAssetAudit(vAstBusinessHandling);
                }
                break;
            // 已办
            case "2":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    vAstBusinessHandling.setPiids(runTaskByUserNameAndStatus);
                    astCheckBills = this.baseMapper.selectAssetAudit(vAstBusinessHandling);
                }
                break;
            // 终审
            case "3":
                vAstBusinessHandling.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("2"));
                astCheckBills = this.baseMapper.selectAssetAudit(vAstBusinessHandling);
                break;
            // 全部
            case "":
                vAstBusinessHandling.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("3"));
                astCheckBills = this.baseMapper.selectAssetAudit(vAstBusinessHandling);
                break;
            default:
                throw new CustomException("单据状态未定义!");
        }
        return astCheckBills;
    }
}