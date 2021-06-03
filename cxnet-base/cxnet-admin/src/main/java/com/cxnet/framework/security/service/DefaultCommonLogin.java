package com.cxnet.framework.security.service;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.cxnet.common.constant.Constants;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.exception.user.CaptchaException;
import com.cxnet.common.exception.user.CaptchaExpireException;
import com.cxnet.common.utils.MessageUtils;
import com.cxnet.common.utils.RegularUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.framework.manager.AsyncManager;
import com.cxnet.framework.manager.factory.AsyncFactory;
import com.cxnet.framework.redis.RedisCache;
import com.cxnet.framework.security.LoginUser;
import com.cxnet.framework.security.service.logindomain.LoginDomain;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.service.SysDeptServiceI;
import com.cxnet.project.system.parameter.domain.SysParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public abstract class DefaultCommonLogin implements CommonLogin {


    @Autowired(required = false)
    private SysDeptServiceI sysDeptServiceI;

    @Autowired(required = false)
    private RedisCache redisCache;

    @Autowired(required = false)
    private TokenService tokenService;


    /**
     * 数字0
     */
    public static final String NUMBER_0 = "0";

    /**
     * 数字1
     */
    public static final String NUMBER_1 = "1";


    @Override
    public boolean pwdType(String pwd, SysParameter sysParameter) {
        if (StringUtils.isEmpty(pwd)) {
            return true;
        }
        switch (sysParameter.getCodeRule()) {
            case RegularUtils.NUMBER:
                return false;
            case RegularUtils.NUMBER_AND_LETTER:
            case RegularUtils.COMPLEXITY:
                if (pwd.length() < 6) {
                    return true;
                }
                return RegularUtils.isNumOrLetter(pwd);
            case "9":
                return false;
            default:
                throw new CustomException("未定义的密码规则");
        }
    }

    @Override
    public Map<String, Object> pwdValidity(String validityType, Date validity) {
        Map<String, Object> map = new HashMap<>(2);
        // 查询密码过期类型
        DateTime now = DateUtil.date();
        // 密码是否过期，0仅提醒，1强制修改
        if (NUMBER_0.equals(validityType) && now.isAfter(validity)) {
            map.put("type", NUMBER_0);
        } else if (NUMBER_1.equals(validityType) && now.isAfter(validity)) {
            map.put("type", NUMBER_1);
            map.put("token", NUMBER_0);
        }
        return map;
    }


    @Override
    public Map<String, Object> loginOverTime(Authentication authentication, String username, String type) {
        Map<String, Object> map = new HashMap<>(1);
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        SysDept sysDept = sysDeptServiceI.selectDeptById(loginUser.getUser().getDeptId());
        loginUser.getUser().setDept(sysDept);
        // 生成token
        map.put("token", tokenService.createToken(loginUser, type));
        return map;
    }

    /**
     * 验证码
     *
     * @param loginDomain
     * @return
     */
    public Map<String, Object> verifyCode(LoginDomain loginDomain, String isUsingCode) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotEmpty(loginDomain.getCode()) || NUMBER_0.equals(isUsingCode) && loginDomain.getLoginNum() != null && loginDomain.getLoginNum() >= 3) {
            if (StringUtils.isEmpty(loginDomain.getCode())) {
                map.put("token", Constants.SHOW_CHECK);
                return map;
            }
            String verifyKey = Constants.CAPTCHA_CODE_KEY + loginDomain.getUuid();
            String captcha = redisCache.getCacheObject(verifyKey);
            redisCache.deleteObject(verifyKey);
            if (captcha == null) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginDomain.getUsername(), Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                throw new CaptchaExpireException();
            }
            if (!loginDomain.getCode().equalsIgnoreCase(captcha)) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginDomain.getUsername(), Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                throw new CaptchaException();
            }
        }
        return map;
    }

}
