package com.cxnet.rpc.service.system.userservicerpc;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.rpc.domain.system.userrpc.SysUserRpc;

public interface UserServiceRpc {
    /**
     * 根据用户名获取用户信息
     *
     * @param userName
     * @return
     */
    SysUserRpc getUserRpcByUserName(String userName);
}
