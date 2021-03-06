package com.cxnet.framework.security.service;

import javax.annotation.Resource;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cxnet.common.exception.BaseException;
import com.cxnet.common.utils.RegularUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.framework.redis.RedisUtil;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.service.SysDeptServiceI;
import com.cxnet.project.system.login.service.UserLoginServiceI;
import com.cxnet.project.system.parameter.domain.SysParameter;
import com.cxnet.project.system.parameter.mapper.SysParameterMapper;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.project.system.user.mapper.SysUserMapper;
import com.cxnet.project.system.user.service.SysUserServiceI;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.cxnet.common.constant.Constants;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.exception.user.CaptchaException;
import com.cxnet.common.exception.user.CaptchaExpireException;
import com.cxnet.common.exception.user.UserPasswordNotMatchException;
import com.cxnet.common.utils.MessageUtils;
import com.cxnet.framework.manager.AsyncManager;
import com.cxnet.framework.manager.factory.AsyncFactory;
import com.cxnet.framework.redis.RedisCache;
import com.cxnet.framework.security.LoginUser;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * ??????????????????
 *
 * @author cxnet
 */
@Component
public class SysLoginService {
    @Autowired(required = false)
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired(required = false)
    private SysPermissionService permissionService;

    @Autowired(required = false)
    private SysDeptServiceI sysDeptServiceI;

    @Autowired(required = false)
    private RedisCache redisCache;
    @Autowired(required = false)
    private RedisUtil redisUtil;

    @Autowired(required = false)
    private UserLoginServiceI userLoginServiceI;

    @Autowired(required = false)
    private SysUserMapper sysUserMapper;

    @Autowired(required = false)
    private SysParameterMapper sysParameterMapper;

    /**
     * ???????????????
     */
    private static final String EXP_EXTEND1 = "0";

    /**
     * ????????????
     *
     * @param username ?????????
     * @param password ??????
     * @param code     ?????????
     * @param uuid     ????????????
     * @return ??????
     */
//    public Map<String, String> login(String username, String password, String code, String uuid) {
//        Map<String, String> map = new HashMap<>(3);
//        Integer loginNum = redisCache.getCacheObject(username);
//        SysParameter sysParameter = sysParameterMapper.selectSysParameter();
//        // ???????????????
//        if (StringUtils.isNotEmpty(code) || EXP_EXTEND1.equals(sysParameter.getExpExtend1()) && loginNum != null && loginNum >= 3) {
//            if (StringUtils.isEmpty(code)) {
//                map.put("token", Constants.SHOW_CHECK);
//                return map;
//            }
//            String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
//            String captcha = redisCache.getCacheObject(verifyKey);
//            redisCache.deleteObject(verifyKey);
//            if (captcha == null) {
//                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
//                throw new CaptchaExpireException();
//            }
//            if (!code.equalsIgnoreCase(captcha)) {
//                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
//                throw new CaptchaException();
//            }
//        }
//        // ?????????????????????
//        SysUser sysUser = sysUserMapper.selectUserByUserName(username);
//        if (sysUser == null) {
//            throw new BaseException("??????????????????????????????/???????????????????????????????????????");
//        }
//        if (pwdType(password, sysParameter)) {
//            if (SecurityUtils.matchesPassword(password, sysUser.getPassword())) {
//                map.put("token", Constants.SUCCESS);
//                return map;
//            }
//        }
//        // ????????????
//        Authentication authentication = null;
//        try {
//            // ?????????????????????UserDetailsServiceImpl.loadUserByUsername
//            authentication = authenticationManager
//                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
//        } catch (Exception e) {
//            if (e instanceof BadCredentialsException) {
//                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
//                // ????????????????????????
//                if (EXP_EXTEND1.equals(sysParameter.getExpExtend1())) {
//                    loginNum = loginNum == null ? 1 : loginNum + 1;
//
//                    if (loginNum >= 1 && loginNum < 3) {
//                        redisCache.setCacheObject(username, loginNum);
//                    }
//                    if (loginNum >= 3 && loginNum < 5) {
//                        redisCache.setCacheObject(username, loginNum);
//                        map.put("token", Constants.SHOW_CHECK);
//                        return map;
//                    }
//                    if (loginNum == 5) {
//                        redisCache.setCacheObject(username, loginNum, 5, MINUTES);
//                        throw new BaseException("???????????????????????????" + username + " ??????????????????5??????????????????");
//                    }
//                    if (loginNum > 5) {
//                        long expire = redisUtil.getExpire(username);
//                        throw new BaseException("???????????????????????????" + username + " ??????????????????" + expire + "???????????????");
//                    }
//                }
//                throw new UserPasswordNotMatchException();
//            } else {
//                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
//                throw new CustomException(e.getMessage());
//            }
//        }
//        if (loginNum != null && loginNum >= 5) {
//            long expire = redisUtil.getExpire(username);
//            throw new BaseException("???????????????????????????" + username + " ??????????????????" + expire + "???????????????");
//        }
//        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        SysDept sysDept = sysDeptServiceI.selectDeptById(loginUser.getUser().getDeptId());
//        loginUser.getUser().setDept(sysDept);
//        redisCache.deleteObject(username);
//        // ??????token
//        map.put("token", tokenService.createToken(loginUser, Constants.WEB));
//
//        // ????????????????????????
//        String validityType = sysParameter.getExpExtend4();
//        DateTime now = DateUtil.date();
//        Date validity = sysUser.getValidity();
//        // ?????????????????????0????????????1????????????
//        if (Constants.SUCCESS.equals(validityType) && now.isAfter(validity)) {
//            map.put("type", Constants.SUCCESS);
//        } else if (Constants.FAIL.equals(validityType) && now.isAfter(validity)) {
//            map.put("type", Constants.FAIL);
//            map.put("token", Constants.SUCCESS);
//        }
//        return map;
//    }


    /**
     * ??????????????????
     *
     * @param pwd ??????
     * @return ???????????????????????????true ?????????false ??????
     */
//    public boolean pwdType(String pwd, SysParameter sysParameter) {
//        if (StringUtils.isEmpty(pwd)) {
//            return true;
//        }
//        switch (sysParameter.getCodeRule()) {
//            case RegularUtils.NUMBER:
//                return false;
//            case RegularUtils.NUMBER_AND_LETTER:
//            case RegularUtils.COMPLEXITY:
//                if (pwd.length() < 6) {
//                    return true;
//                }
//                return RegularUtils.isNumOrLetter(pwd);
//            case "9":
//                return false;
//            default:
//                throw new CustomException("????????????????????????");
//        }
//
//    }


    /**
     * sso????????????
     *
     * @param username ?????????
     * @return ??????
     */
    public String loginBySso(String username) {
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = new LoginUser();
        SysUser sysUser = userLoginServiceI.selectUserInfoByUserName(username);
        loginUser.setUser(sysUser);
        // ??????token
        return tokenService.createToken(loginUser, Constants.WEB);
    }

}
