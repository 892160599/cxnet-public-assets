package com.cxnet.project.system.user.controller;

import java.io.IOException;

import com.cxnet.common.constant.Constants;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.exception.user.CaptchaException;
import com.cxnet.common.exception.user.CaptchaExpireException;
import com.cxnet.common.utils.MessageUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.framework.config.ServerConfig;
import com.cxnet.framework.manager.AsyncManager;
import com.cxnet.framework.manager.factory.AsyncFactory;
import com.cxnet.framework.redis.RedisCache;
import com.cxnet.project.system.file.fileUpload.domain.FileUpload;
import com.cxnet.project.system.file.fileUpload.service.FileUploadServiceI;
import com.cxnet.project.system.parameter.domain.SysParameter;
import com.cxnet.project.system.parameter.mapper.SysParameterMapper;
import com.cxnet.project.system.user.domain.SysUserInfo;
import com.cxnet.project.system.user.mapper.SysUserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.common.utils.ServletUtils;
import com.cxnet.common.utils.file.FileUploadUtils;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.common.config.BaseConfig;
import com.cxnet.framework.security.LoginUser;
import com.cxnet.framework.security.service.TokenService;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.project.system.user.service.SysUserServiceI;

/**
 * 个人信息 业务处理
 *
 * @author cxnet
 */
@RestController
@Api(tags = "个人信息")
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {
    @Autowired(required = false)
    private SysUserServiceI userService;

    @Autowired(required = false)
    private TokenService tokenService;

    @Autowired(required = false)
    private FileUploadServiceI fileUploadService;

    @Autowired(required = false)
    private ServerConfig serverConfig;

    /**
     * 获取个人信息
     */
    @GetMapping
    @ApiOperation("获取个人信息")
    public AjaxResult profile() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUsername()));
        ajax.put("postGroup", userService.selectUserPostGroup(loginUser.getUsername()));
        return ajax;
    }

    /**
     * 修改用户
     */
    @ApiOperation("修改用户")
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user) {
        return toAjax(userService.updateUserProfile(user));
    }

    /**
     * 重置密码
     */
    @ApiOperation("重置密码")
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (loginUser.getUser().isAdmin()) {
            throw new CustomException("系统管理员不给予修改！");
        }
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return AjaxResult.error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return AjaxResult.error("新密码不能与旧密码相同");
        }
        return toAjax(userService.resetUserPwd(userName, newPassword));
    }

    /**
     * 弱密码检测-重置密码
     */
    @ApiOperation("弱密码检测-重置密码")
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/changePwd")
    public AjaxResult changePwd(@Validated @RequestBody SysUserInfo userInfo) {
        return userService.changePwd(userInfo.getUserName(), userInfo.getNickName(), userInfo.getNewPassword(), userInfo.getOldPassword(), userInfo.getCode(), userInfo.getUuid());
    }

    /**
     * 用户头像
     */
    @ApiOperation("用户头像")
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            String avatar = FileUploadUtils.upload(BaseConfig.getAvatarPath(), file);
            String url = serverConfig.getUrl() + avatar;
            if (userService.updateUserAvatar(loginUser.getUsername(), avatar)) {
                FileUpload fileUpload = new FileUpload();
                fileUpload.setFileType(file.getContentType());
                String filename = file.getResource().getFilename();
                String originName = file.getOriginalFilename();
                fileUpload.setRealName(originName);
                fileUpload.setFileSuffix(filename.substring(filename.lastIndexOf(".") + 1));
                fileUpload.setFileSize((file.getSize()));
                fileUpload.setFileName(avatar);
                fileUpload.setFileUrl(url);
                fileUpload.setCreateBy(SecurityUtils.getUsername());
                fileUploadService.insertFileUpload(fileUpload);
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatar);
                loginUser.getUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return AjaxResult.error("上传图片异常，请联系管理员");
    }
}