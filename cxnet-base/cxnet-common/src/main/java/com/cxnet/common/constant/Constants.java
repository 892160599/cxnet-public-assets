package com.cxnet.common.constant;

import io.jsonwebtoken.Claims;

/**
 * 通用常量信息
 *
 * @author cxnet
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 消息
     */
    public static final String MSG = "msg";

    /**
     * 状态码
     */
    public static final String CODE = "code";

    /**
     * 前端显示验证码标识
     */
    public static final String SHOW_CHECK = "-1";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = Claims.SUBJECT;

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * 财政年度
     */
    public static final String FISCAL = "fiscal";

    /**
     * 菜单id
     */
    public static final String MENU_ID = "menuId";

    /**
     * Referer
     */
    public static final String REFERER = "Referer";

    /**
     * 模板编码
     */
    public static final String TEMPLATE_CODE = "10001";

    /**
     * 是(存在、正常)
     */
    public static final String YES = "0";


    /**
     * 否(不存在、停用)
     */
    public static final String NO = "2";


    /**
     * 默认单位
     */
    public static final String DEFAULT_UNIT_ID = "*";


    /**
     * web端
     */
    public static final String WEB = "web";

    /**
     * 移动端
     */
    public static final String MOIBLE = "mobile";


}
