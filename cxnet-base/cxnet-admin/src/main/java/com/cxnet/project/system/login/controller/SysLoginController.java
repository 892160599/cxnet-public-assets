package com.cxnet.project.system.login.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cxnet.framework.security.service.*;
import com.cxnet.framework.security.service.logindomain.LoginDomain;
import com.cxnet.project.system.login.domain.LoginUserInfo;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.constant.Constants;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.ServletUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.framework.redis.RedisUtil;
import com.cxnet.framework.security.LoginUser;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.domain.SysDeptVO;
import com.cxnet.project.system.dept.service.SysDeptServiceI;
import com.cxnet.project.system.login.service.UserLoginServiceI;
import com.cxnet.project.system.menu.domain.SysMenu;
import com.cxnet.project.system.menu.service.SysMenuServiceI;
import com.cxnet.project.system.parameter.domain.SysParameter;
import com.cxnet.project.system.parameter.service.SysParameterServiceI;
import com.cxnet.project.system.user.domain.SysUser;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;

/**
 * ????????????
 *
 * @author cxnet
 */
@Api(tags = "????????????")
@RestController
@Slf4j
public class SysLoginController {

    @Autowired(required = false)
    private SysLoginService loginService;

    @Autowired(required = false)
    private SysMenuServiceI menuService;

    @Autowired(required = false)
    private SysPermissionService permissionService;

    @Autowired(required = false)
    private TokenService tokenService;

    @Autowired(required = false)
    private SysDeptServiceI sysDeptServiceI;
    @Autowired(required = false)
    private RedisUtil redisUtil;
    @Autowired(required = false)
    private Environment environment;
    @Autowired(required = false)
    private UserLoginServiceI userLoginServiceI;
    @Autowired(required = false)
    private SysParameterServiceI sysParameterServiceI;
    @Autowired(required = false)
    private AppLogin appLogin;
    @Autowired(required = false)
    private WebLogin webLogin;

    /**
     * ????????????
     *
     * @param loginUserInfo ??????????????????
     * @return ??????
     */
    @ApiOperation("??????")
    @PostMapping("/login")
    public AjaxResult login(@Validated @RequestBody LoginUserInfo loginUserInfo) {
        AjaxResult ajax = AjaxResult.success();
        LoginDomain loginDomain = new LoginDomain(loginUserInfo.getUsername(), loginUserInfo.getPassword(), loginUserInfo.getCode(), loginUserInfo.getUuid());
        Map<String, Object> result = webLogin.login(loginDomain);
        String token = (String) result.get("token");
        String type = (String) result.get("type");
        responseValue(token, type, ajax);
        return ajax;
    }

    /**
     * ????????????
     *
     * @param token
     * @param type
     * @param ajax
     */
    public void responseValue(String token, String type, AjaxResult ajax) {
        ajax.put(Constants.TOKEN, token);
        if (Constants.SHOW_CHECK.equals(token)) {
            ajax.put(Constants.CODE, 200);
            ajax.put(Constants.MSG, "??????????????????????????????????????????????????????????????????????????????????????????");
        }
        // ?????????????????????
        SysParameter sysParameter = sysParameterServiceI.selectSysParameter();
        if (Constants.SUCCESS.equals(token)) {
            ajax.put("data", sysParameter.getCodeRule());
            ajax.put(Constants.CODE, 200);
            ajax.put(Constants.MSG, "????????????????????????????????????????????????????????????");
        }
        // ??????????????????
        if (StringUtils.isNotEmpty(type)) {
            ajax.put("data", sysParameter.getCodeRule());
            ajax.put(Constants.MSG, "?????????????????????????????????????????????????????????");
            ajax.put("type", type);
        } else {
            ajax.put("type", "");
        }
    }

    /**
     * ????????????
     *
     * @param
     * @return ??????
     */
    @ApiOperation("??????????????????")
    @GetMapping("/changeDefaultDeptCode")
    public AjaxResult changeDefaultDeptCode(String unitId, String userId) {
        if (StringUtils.isBlank(unitId)) {
            throw new CustomException("?????????????????????");
        }

        if (StringUtils.isBlank(userId)) {
            throw new CustomException("?????????????????????");
        }
        sysDeptServiceI.updateDeptId(unitId, userId);
        SysDept sysDept = sysDeptServiceI.selectDeptById(unitId);
        String tokenStr = JSONObject.parseObject(JSON.toJSONString(SecurityUtils.getAuthentication().getPrincipal())).getString("token");
        Object token = redisUtil.get("login_tokens:".concat(tokenStr));
        if (ObjectUtil.isNotNull(token) && ObjectUtil.isNotNull(sysDept)) {
            LoginUser loginUser = JSON.parseObject(JSON.toJSONString(token), LoginUser.class);
            loginUser.getUser().setDept(sysDept);
            loginUser.getUser().setDeptId(sysDept.getDeptId());
            loginUser.getUser().setDeptCode(sysDept.getDeptCode());
            redisUtil.set("login_tokens:".concat(tokenStr), loginUser);
        }
        return AjaxResult.success(sysDept);
    }

    /**
     * ??????????????????
     *
     * @return ????????????
     */
    @ApiOperation("??????????????????")
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        String deptCode1 = permissionService.getDeptPermission(user);
        user.setDeptCode(deptCode1);
        // ????????????
        Set<String> roles = permissionService.getRolePermission(user);
        // ????????????
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        // ??????
        List<SysDeptVO> dept = sysDeptServiceI.getDeptsByUserId(user.getUserId());
        ajax.put("dept", dept);
        return ajax;
    }

    /**
     * ??????????????????id
     *
     * @return ??????????????????id
     */
    @ApiOperation("??????????????????id")
    @GetMapping("UnitDeptInfo")
    public AjaxResult unitDeptInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        AjaxResult ajax = AjaxResult.success();
        //????????????id
        SysDept sysDept = permissionService.getUnit(user);
        //????????????id
        List<SysDeptVO> dept = sysDeptServiceI.getDeptsByUserId(user.getUserId());
        ajax.put("unitId", sysDept.getDeptId());
        ajax.put("dept", dept);
        return ajax;
    }

    /**
     * ??????????????????
     *
     * @return ????????????
     */
    @ApiOperation("??????????????????")
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // ????????????
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    /**
     * ????????????????????????????????????
     *
     * @return ????????????
     */
    @ApiOperation("????????????????????????????????????")
    @GetMapping("getAnaRouters")
    public AjaxResult getAnaRouters() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // ????????????
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return AjaxResult.success(menuService.buildAnaMenus(menus));
    }

    /**
     * sso???????????? ?????????
     *
     * @return ????????????
     */
    @ApiOperation("sso????????????")
    @GetMapping("ssoLogin")
    public AjaxResult ssoLogin(@RequestParam(value = "sid", required = false) String sid) {
        JSONObject obj = JSON.parseObject(sid);

        String sessionIdByJson = obj.getString("sessionId");
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        //??????HttpClient?????????????????????OA??????checkSso.jsp??????true or false?????????
        HttpGet httpGet = new HttpGet(environment.getProperty("sso.url") + sessionIdByJson);
        System.out.println(environment.getProperty("sso.url"));
        System.out.println(environment.getProperty("sso.url") + sessionIdByJson);

        String ssoResult = "";
        String resultInfo = "";//??????????????????
        String userName = "";
        AjaxResult ajax = AjaxResult.success();
        CloseableHttpResponse response = null;

        try {
            // ??????????????????(??????)Get??????
            response = httpClient.execute(httpGet);
            // ????????????????????????????????????
            HttpEntity responseEntity = response.getEntity();
            ssoResult = EntityUtils.toString(responseEntity);
            //???????????????
            JSONObject jsonData = JSON.parseObject(ssoResult);
            System.out.println(jsonData);
            if ("true".equals(jsonData.get("issuccess"))) {
                userName = jsonData.get("userName").toString();
                if (userName == null) {
                    resultInfo = "200";
                } else {
                    SysUser loginUser = userLoginServiceI.selectUserInfoByUserName(userName);
                    ajax.put("user", loginUser);
                    resultInfo = "100";
                }
            } else {
                resultInfo = "200";
            }
        } catch (Exception e) {
            log.error("????????????:{}", e.getMessage(), e);
        }
        ajax.put("ret", resultInfo);
        return ajax;
    }

    /**
     * sso????????????
     *
     * @param loginUserInfo ??????????????????
     * @return ??????
     */
    @ApiOperation("??????")
    @PostMapping("/loginBySso")
    public AjaxResult loginBySso(@Validated @RequestBody LoginUserInfo loginUserInfo) {
        AjaxResult ajax = AjaxResult.success();
        // ????????????
        String token = loginService.loginBySso(loginUserInfo.getUsername());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * ???????????????
     *
     * @param loginUserInfo
     * @return
     */
    @PostMapping("/mobileLogin")
    public AjaxResult mobileLogin(@Validated @RequestBody LoginUserInfo loginUserInfo) {
        if (StringUtils.isEmpty(loginUserInfo.getServerAddr())) {
            throw new CustomException("???????????????????????????");
        }
        AjaxResult ajax = AjaxResult.success();
        LoginDomain loginDomain = new LoginDomain(loginUserInfo.getUsername(), loginUserInfo.getPassword(), null, null);
        Map<String, Object> result = appLogin.login(loginDomain);
        String token = (String) result.get("token");
        String type = (String) result.get("type");
        responseValue(token, type, ajax);
        return ajax;
    }
}