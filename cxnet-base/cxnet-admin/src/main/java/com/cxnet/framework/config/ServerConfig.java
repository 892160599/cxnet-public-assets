package com.cxnet.framework.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.cxnet.common.utils.ServletUtils;

/**
 * 服务相关配置
 *
 * @author cxnet
 */
@Component
public class ServerConfig {

    private int port;
    private String contextPath;

    @Value("${server.port}")
    public void setPort(int port) {
        this.port = port;
    }

    @Value("${server.servlet.context-path}")
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    /**
     * 获取完整的请求路径，包括：域名，端口，上下文访问路径
     *
     * @return 服务地址
     */
    public String getUrl() {
        HttpServletRequest request = ServletUtils.getRequest();
        if (contextPath.endsWith("/")) {
            contextPath = contextPath.substring(contextPath.lastIndexOf("/") + 1);
        }
        return "http://" + request.getRemoteHost() + ":" + port + contextPath;
    }
}
