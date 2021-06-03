package com.cxnet.framework.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.user.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        if (SecurityUtils.isLogin()) {
            SysUser user = SecurityUtils.getLoginUser().getUser();
            this.setInsertFieldValByName("createBy", user.getUserName(), metaObject);
            this.setInsertFieldValByName("createName", user.getNickName(), metaObject);
            this.setUpdateFieldValByName("updateBy", user.getUserName(), metaObject);
        } else {
            this.setInsertFieldValByName("createBy", "#", metaObject);
            this.setInsertFieldValByName("createName", "#", metaObject);
            this.setUpdateFieldValByName("updateBy", "#", metaObject);
        }
        this.setInsertFieldValByName("createTime", new Date(), metaObject);
        this.setUpdateFieldValByName("updateTime", new Date(), metaObject);
    }


    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.setUpdateFieldValByName("updateTime", new Date(), metaObject);
        if (SecurityUtils.isLogin()) {
            SysUser user = SecurityUtils.getLoginUser().getUser();
            this.setUpdateFieldValByName("updateBy", user.getUserName(), metaObject);
        } else {
            this.setUpdateFieldValByName("updateBy", "#", metaObject);
        }
    }
}
