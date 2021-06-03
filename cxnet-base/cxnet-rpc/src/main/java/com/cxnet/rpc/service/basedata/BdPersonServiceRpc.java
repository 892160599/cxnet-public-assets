package com.cxnet.rpc.service.basedata;

import com.cxnet.rpc.domain.basedata.BdPersonRpc;
import com.cxnet.rpc.domain.basedata.BdPersonnel;

import java.util.List;

/**
 * 部门人员Rpc接口
 *
 * @author ssw
 */
public interface BdPersonServiceRpc {

    /**
     * 根据部门id查找部门人员
     *
     * @param unitId 单位id
     * @param deptId 部门id
     * @return
     */
    List<BdPersonRpc> selectPersonByDeptId(String unitId, String deptId);


    /**
     * 根据用户id判断当前登录用户是否是财务人员
     *
     * @param userId 用户id
     * @return
     */
    boolean isFinancialOfficer(String userId);

    /**
     * 根据部门id和用户id查询
     *
     * @param deptId
     * @param userId
     * @return
     */
    BdPersonRpc selectPersonByDeptIdAndDeptId(String deptId, String userId);


    BdPersonnel selectBdPersonnelById(String personId);

}
