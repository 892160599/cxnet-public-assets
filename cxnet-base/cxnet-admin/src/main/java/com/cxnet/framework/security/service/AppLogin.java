package com.cxnet.framework.security.service;

import cn.hutool.core.util.ObjectUtil;
import com.cxnet.common.constant.Constants;
import com.cxnet.common.exception.BaseException;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.exception.user.UserPasswordNotMatchException;
import com.cxnet.common.utils.MessageUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.framework.manager.AsyncManager;
import com.cxnet.framework.manager.factory.AsyncFactory;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.framework.security.service.logindomain.LoginDomain;
import com.cxnet.project.system.parameter.domain.SysParameter;
import com.cxnet.project.system.parameter.service.SysParameterServiceI;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@Component
public class AppLogin extends DefaultCommonLogin {

    @Resource
    private AuthenticationManager authenticationManager;
    @Autowired(required = false)
    private SysUserMapper sysUserMapper;
    @Autowired(required = false)
    private SysParameterServiceI sysParameterServiceI;

    /**
     * 验证是否是手机号
     */
    private static final String PHONE = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";

    @Override
    public Map<String, Object> login(LoginDomain loginDomain) {
        SysUser sysUser = null;
        boolean flag = false;
        Map<String, Object> map = new HashMap<>(4);
        flag = Pattern.compile(PHONE).matcher(loginDomain.getUsername()).matches();
        sysUser = flag ? sysUserMapper.selectUserByPhone(loginDomain.getUsername()) : sysUserMapper.selectUserByUserName(loginDomain.getUsername());
        SysParameter sysParameter = sysParameterServiceI.selectSysParameter();
        if (ObjectUtil.isNull(sysUser)) {
            throw new BaseException("对不起，您输入的的用户名或密码不正确。请重新再试！");
        }
        //登陆方式
        loginType(sysUser, flag);
        //密码复杂度
        if (pwdType(loginDomain.getPassword(), sysParameter)) {
            if (SecurityUtils.matchesPassword(loginDomain.getPassword(), sysUser.getPassword())) {
                map.put("token", Constants.SUCCESS);
                return map;
            }
        }
        // 验证用户
        Authentication authentication = null;
        map = authentication(authentication, sysUser.getUserName(), loginDomain.getPassword(), null, null);
        //记录
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(sysUser.getUserName(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        // MobEquipment mobEquipment = new MobEquipment(sysUser.getUserId(), "");//TODO 设备唯一编号
        //     SpringUtils.getBean(MobEquipmentService.class).insertEquipment(mobEquipment);
        //  登陆超时
        Map<String, Object> overTimeMap = loginOverTime((Authentication) map.get("authentication"), loginDomain.getUsername(), Constants.MOIBLE);
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
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            map.put("authentication", authentication);
            return map;
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new CustomException(e.getMessage());
            }
        }
    }

    /**
     * 若登陆方式为空 默认普通登陆
     *
     * @param user 用户信息
     * @param flag true 手机号登陆 false 非手机号登陆
     */
    private void loginType(SysUser user, boolean flag) {
        //查询此用户的登陆方式
        String loginMode = user.getLoginMode();
        if (StringUtils.isEmpty(loginMode)) {
            throw new CustomException("抱歉！您没有登陆权限，请联系管理员设置登陆权限！");
        }
        String[] split = loginMode.split(",");
        List<String> strings = Arrays.asList(split);
        if (flag) {
            if (!strings.contains("3")) {
                throw new CustomException("此用户的允许登陆方式为：[普通登陆]！请输入用户名！");
            }
        }
        if (!flag) {
            if (!strings.contains("1")) {
                throw new CustomException("此用户的允许登陆方式为：[手机号登陆]!请输入手机号！");
            }
        }
    }
}
