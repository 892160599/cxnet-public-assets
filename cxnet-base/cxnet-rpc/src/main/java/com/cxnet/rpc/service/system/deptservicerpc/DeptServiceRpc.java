package com.cxnet.rpc.service.system.deptservicerpc;

import com.cxnet.rpc.domain.system.deptrpc.SysDeptRpc;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface DeptServiceRpc {

    /**
     * @param deptCode 部门编码
     * @param parentId 上级id
     * @return
     */
    SysDeptRpc selectRpcDeptByCode(String deptCode, String parentId);
}
