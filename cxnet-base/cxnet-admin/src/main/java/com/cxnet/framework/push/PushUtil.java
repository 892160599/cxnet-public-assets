package com.cxnet.framework.push;

import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.api.AuthApi;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.core.client.DefaultApiClient;
import com.getui.push.v2.sdk.core.factory.GtApiProxyFactory;
import com.getui.push.v2.sdk.dto.req.AuthDTO;
import com.getui.push.v2.sdk.dto.res.TokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangyl
 * @date 2021-5-26 13:40
 */
@Slf4j
@Component
public class PushUtil {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private PushApi pushApi;
    @Resource
    private AuthApi authApi;
    @Resource
    private PushProperties pushProperties;

    /**
     * 鉴权并且选择何种推送
     */
    public void pushAll(String push) {
        Map<String, String> map = new HashMap();
        //将鉴权存入缓存
        TokenDTO tokenList = (TokenDTO) redisTemplate.opsForValue().get("TokenList");
        if (tokenList == null) {
            //鉴权拿到token
            TokenDTO token = this.token(authApi);
            //将拿到的token放到请求头中
            map.put("token", token.getToken());
            HttpUtils.postJson(push, map);
        } else {
            //校验token是否过期true表示过期
            boolean expired = tokenList.expired();
            if (expired) {
                //token已经过期，需要重新生成
                TokenDTO token = this.token(authApi);
                //将拿到的token放到请求头中
                map.put("token", token.getToken());
                HttpUtils.postJson(push, map);
            } else {
                //将未过期的token放入请求头中
                map.put("token", tokenList.getToken());
                HttpUtils.postJson(push, map);
            }
        }
    }

    /**
     * 鉴权并存入缓存
     *
     * @param authApi
     * @return
     */
    public TokenDTO token(AuthApi authApi) {
        AuthDTO authdto = AuthDTO.build(pushProperties.getAppKey(), pushProperties.getMasterSecret());
        ApiResult<TokenDTO> auth = authApi.auth(authdto);
        redisTemplate.opsForValue().set("TokenList", auth.getData());
        TokenDTO tokenList = (TokenDTO) redisTemplate.opsForValue().get("TokenList");
        return tokenList;
    }

}
