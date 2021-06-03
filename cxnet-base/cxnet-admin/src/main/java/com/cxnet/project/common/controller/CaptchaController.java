package com.cxnet.project.common.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.constant.Constants;
import com.cxnet.common.utils.IdUtils;
import com.cxnet.common.utils.VerifyCodeUtils;
import com.cxnet.common.utils.sign.Base64;
import com.cxnet.framework.redis.RedisCache;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 验证码操作处理
 *
 * @author cxnet
 */
@RestController
@Api(tags = "验证码操作处理")
@Slf4j
public class CaptchaController {
    @Autowired(required = false)
    private RedisCache redisCache;

    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    @ApiOperation("生成验证码")
    public AjaxResult getCode(HttpServletResponse response) throws IOException {
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        // 唯一标识
        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        redisCache.setCacheObject(verifyKey, verifyCode, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 生成图片
        int w = 111, h = 36;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        VerifyCodeUtils.outputImage(w, h, stream, verifyCode);
        try {
            AjaxResult ajax = AjaxResult.success();
            ajax.put("uuid", uuid);
            ajax.put("img", Base64.encode(stream.toByteArray()));
            return ajax;
        } catch (Exception e) {
            log.error("错误原因:{}", e.getMessage(), e);
            return AjaxResult.error(e.getMessage());
        } finally {
            stream.close();
        }
    }
}
