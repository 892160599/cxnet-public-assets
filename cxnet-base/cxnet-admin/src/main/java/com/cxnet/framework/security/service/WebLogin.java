package com.cxnet.framework.security.service;

import com.cxnet.common.constant.Constants;
import com.cxnet.common.exception.BaseException;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.exception.user.UserPasswordNotMatchException;
import com.cxnet.common.utils.MessageUtils;
import com.cxnet.framework.manager.AsyncManager;
import com.cxnet.framework.manager.factory.AsyncFactory;
import com.cxnet.framework.redis.RedisCache;
import com.cxnet.framework.redis.RedisUtil;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.framework.security.service.logindomain.LoginDomain;
import com.cxnet.project.system.parameter.domain.SysParameter;
import com.cxnet.project.system.parameter.mapper.SysParameterMapper;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.project.system.user.mapper.SysUserMapper;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static java.util.concurrent.TimeUnit.MINUTES;

@Component
public class WebLogin extends DefaultCommonLogin {


    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired(required = false)
    private RedisCache redisCache;
    @Autowired(required = false)
    private RedisUtil redisUtil;

    @Autowired(required = false)
    private SysParameterMapper sysParameterMapper;

    @Autowired(required = false)
    private SysUserMapper sysUserMapper;

    public static final String NUMBER_0 = "0";

    @Override
    public Map<String, Object> login(LoginDomain loginDomain) {
        String username = loginDomain.getUsername();
        String password = loginDomain.getPassword();
        Map<String, Object> map = new HashMap<>(3);
        Integer loginNum = redisCache.getCacheObject(username);
        SysParameter sysParameter = sysParameterMapper.selectSysParameter();
        //验证码校验
        Map<String, Object> verifyCodeMap = verifyCode(loginDomain, sysParameter.getExpExtend1());
        if (MapUtils.isNotEmpty(verifyCodeMap)) {
            return verifyCodeMap;
        }
        // 密码复杂度验证
        SysUser sysUser = sysUserMapper.selectUserByUserName(username);
        if (sysUser == null) {
            throw new BaseException("对不起，您的用户名和/或密码不正确。请重新再试！");
        }
        if (pwdType(password, sysParameter)) {
            if (SecurityUtils.matchesPassword(password, sysUser.getPassword())) {
                map.put("token", Constants.SUCCESS);
                return map;
            }
        }
        //用户验证
        Authentication authentication = null;
        map = authentication(authentication, username, password, sysParameter, loginNum);
        if (loginNum != null && loginNum >= 5) {
            long expire = redisUtil.getExpire(username);
            throw new BaseException("对不起，您的账号：" + username + " 以被锁定，请" + expire + "秒后再试！");
        }
        redisCache.deleteObject(username);
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        //登陆超时
        Map<String, Object> overTimeMap = loginOverTime((Authentication) map.get("authentication"), username, Constants.WEB);
        if (MapUtils.isNotEmpty(overTimeMap)) {
            map.putAll(overTimeMap);
        }
        //密码过期控制规则、（密码有效期不在此体现）
        Map<String, Object> pwdValidityMap = pwdValidity(sysParameter.getExpExtend4(), sysUser.getValidity());
        if (MapUtils.isNotEmpty(pwdValidityMap)) {
            map.putAll(pwdValidityMap);
        }
        return map;
    }

    @Override
    public Map<String, Object> authentication(Authentication authentication, String username, String password, SysParameter sysParameter, Integer loginNum) {
        Map<String, Object> map = new HashMap<>();
        // 用户验证
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            map.put("authentication", authentication);
            return map;
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                // 根据条件锁定账户
                if (NUMBER_0.equals(sysParameter.getExpExtend1())) {
                    loginNum = loginNum == null ? 1 : loginNum + 1;

                    if (loginNum >= 1 && loginNum < 3) {
                        redisCache.setCacheObject(username, loginNum);
                    }
                    if (loginNum >= 3 && loginNum < 5) {
                        redisCache.setCacheObject(username, loginNum);
                        map.put("token", Constants.SHOW_CHECK);
                        return map;
                    }
                    if (loginNum == 5) {
                        redisCache.setCacheObject(username, loginNum, 5, MINUTES);
                        throw new BaseException("对不起，您的账号：" + username + " 以被锁定，请5分钟后再试！");
                    }
                    if (loginNum > 5) {
                        long expire = redisUtil.getExpire(username);
                        throw new BaseException("对不起，您的账号：" + username + " 以被锁定，请" + expire + "秒后再试！");
                    }
                }
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new CustomException(e.getMessage());
            }
        }

    }


}
