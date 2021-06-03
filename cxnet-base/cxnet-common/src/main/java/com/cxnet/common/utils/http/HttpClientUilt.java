package com.cxnet.common.utils.http;

import com.alibaba.fastjson.JSONObject;
import com.cxnet.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * http请求类
 *
 * @author cxnet
 * @date 2021/4/22
 */
@Slf4j
public class HttpClientUilt {

    /**
     * 向指定URL发送post请求
     *
     * @param url        url
     * @param jsonString json
     * @param token      token
     * @return 结果字符串
     */
    public static String sendHttpPost(String url, String jsonString, String token) {
        CloseableHttpClient httpClient = null;
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String result = "接口调用失败！";
        try {
            httpClient = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);
            StringEntity entity = new StringEntity(jsonString);
            entity.setContentEncoding("UTF-8");
            if (StringUtils.isNotEmpty(token)) {
                post.setHeader("token", token);
            }
            entity.setContentType("application/json");
            post.setEntity(entity);
            result = httpClient.execute(post, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定URL发送get请求
     *
     * @param url   url
     * @param token token
     * @return 结果字符串
     */
    public static String sendHttpGet(String url, String token) {
        CloseableHttpClient httpClient = null;
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String result = "";
        try {
            httpClient = HttpClientBuilder.create().build();
            HttpGet get = new HttpGet(url);
            if (StringUtils.isNotEmpty(token)) {
                get.setHeader("token", token);
            }
            result = httpClient.execute(get, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
            result = "";
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
