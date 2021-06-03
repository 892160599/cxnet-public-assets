package com.cxnet.project.system.user.rpc;


import com.cxnet.project.system.user.mapper.SysUserMapper;
import com.cxnet.rpc.domain.system.userrpc.SysUserRpc;
import com.cxnet.rpc.service.system.userservicerpc.UserServiceRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImplRpc implements UserServiceRpc {

    @Autowired(required = false)
    private SysUserMapper sysUserMapper;

    /**
     * 根据用户名获取用户信息
     *
     * @param userName
     * @return
     */
    @Override
    public SysUserRpc getUserRpcByUserName(String userName) {
        return sysUserMapper.selectUserRpcByName(userName);
    }


}
