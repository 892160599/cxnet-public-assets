package com.cxnet.project.system.dept.rpc;

import com.cxnet.project.system.dept.mapper.SysDeptMapper;
import com.cxnet.rpc.domain.system.deptrpc.SysDeptRpc;
import com.cxnet.rpc.service.system.deptservicerpc.DeptServiceRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeptServiceImplRpc implements DeptServiceRpc {
    @Autowired(required = false)
    private SysDeptMapper sysDeptMapper;

    /**
     * @param deptCode 部门编码
     * @param parentId 上级id
     * @return 查询结果
     */
    @Override
    public SysDeptRpc selectRpcDeptByCode(String deptCode, String parentId) {
        return sysDeptMapper.selectRpcDeptByCode(deptCode, parentId);
    }
}
