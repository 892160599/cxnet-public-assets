package com.cxnet.framework.security.service;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;

import com.cxnet.common.exception.CustomException;
import com.cxnet.project.system.parameter.domain.SysParameter;
import com.cxnet.project.system.parameter.mapper.SysParameterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.cxnet.common.constant.Constants;
import com.cxnet.common.utils.IdUtils;
import com.cxnet.common.utils.ServletUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.ip.AddressUtils;
import com.cxnet.common.utils.ip.IpUtils;
import com.cxnet.framework.redis.RedisCache;
import com.cxnet.framework.security.LoginUser;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * token验证处理
 *
 * @author cxnet
 */
@Component
public class TokenService {
    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认30分钟）
//    @Value("${token.expireTime}")
//    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 24 * 60 * 60 * 1000L;

    private static final Integer TWENTY_MINUTE = 20 * 60 * 1000;
    private static final Integer FIFTEEN_MINUTE = 15 * 60 * 1000;
    private static final Integer THIRTY_MINUTE = 30 * 60 * 1000;
    private static final Integer SIXTY_MINUTE = 60 * 60 * 1000;
    private static final Integer ONE_DAY = 24 * 60 * 60 * 1000;
    private static final Integer ONE_WEEK = 7 * 24 * 60 * 60 * 1000;
    private static final Integer ONE_MONTH = 30 * 24 * 60 * 60 * 1000;


    @Autowired(required = false)
    private RedisCache redisCache;
    @Autowired(required = false)
    private SysParameterMapper sysParameterMapper;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = parseToken(token);
            // 解析对应的权限以及用户信息
            String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
            String userKey = getTokenKey(uuid);
            LoginUser user = redisCache.getCacheObject(userKey);
            return user;
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            String userKey = getTokenKey(loginUser.getToken());
            redisCache.setCacheObject(userKey, loginUser);
        }
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser, String type) {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        setUserAgent(loginUser);
        refreshToken(loginUser, type);

        Map<String, Object> claims = new HashMap<>(1);
        claims.put(Constants.LOGIN_USER_KEY, token);
        return createToken(claims);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser 令牌
     */
    public void verifyToken(LoginUser loginUser, String type) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= TWENTY_MINUTE) {
            String token = loginUser.getToken();
            loginUser.setToken(token);
            refreshToken(loginUser, type);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser, String type) {
        SysParameter sysParameter = sysParameterMapper.selectSysParameter();
        String overtime = Constants.WEB.equals(type) ? sysParameter.getExpExtend3() : sysParameter.getExpExtend5();
        Integer expireTime = setExpireTime(type, overtime);
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MILLISECONDS);
    }

    private Integer setExpireTime(String type, String overtime) {
        Integer expireTime = null;
        switch (type) {
            case Constants.WEB:
                switch (overtime) {
                    case "1":
                        expireTime = FIFTEEN_MINUTE;
                        break;
                    case "2":
                        expireTime = THIRTY_MINUTE;
                        break;
                    case "3":
                        expireTime = SIXTY_MINUTE;
                        break;
                    case "0":
                        expireTime = -1;
                        break;
                    default:
                        throw new CustomException("未定义的登录超时规则");
                }
                break;
            case Constants.MOIBLE:
                switch (overtime) {
                    case "1":
                        expireTime = ONE_DAY;
                        break;
                    case "2":
                        expireTime = ONE_WEEK;
                        break;
                    case "3":
                        expireTime = ONE_MONTH;
                        break;
                    case "0":
                        expireTime = -1;
                        break;
                    default:
                        throw new CustomException("未定义的登录超时规则");
                }
                break;
            default:

        }
        return expireTime;

    }

    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {
//        Date date = null;
//        String overtime = sysParameterMapper.selectSysParameter().getExpExtend3();
//        switch (overtime) {
//            case "1":
//                date = new Date(System.currentTimeMillis() + FIFTEEN_MINUTE);
//                break;
//            case "2":
//                date = new Date(System.currentTimeMillis() + THIRTY_MINUTE);
//                break;
//            case "3":
//                date = new Date(System.currentTimeMillis() + SIXTY_MINUTE);
//                break;
//            case "0":
//                date = new Date(System.currentTimeMillis() + 24 * SIXTY_MINUTE);
//                break;
//            default:
//                throw new CustomException("未定义的登录超时规则");
//        }
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getTokenKey(String uuid) {
        return Constants.LOGIN_TOKEN_KEY + uuid;
    }
}
