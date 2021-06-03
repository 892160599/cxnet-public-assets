package com.cxnet.project.assets.task;

import com.cxnet.project.assets.data.DataMgr;
import com.cxnet.project.assets.data.cache.CacheKey;
import com.cxnet.project.assets.data.cache.CacheMgr;
import com.cxnet.framework.web.domain.Server;
import com.cxnet.framework.websocket.common.WebSocketConsts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * 任务管理
 */
@Slf4j
@Component
public class TaskMgr {

    @Autowired(required = false)
    private SimpMessagingTemplate wsTemplate;

    /**
     * webSocketTask
     */
    @Scheduled(cron = "${cxnet.scheduled.webSocketTaskCron}")
    public void webSocketTask() throws Exception {
        // 推送服务器状态
        Server server = new Server();
        server.copyTo();
        wsTemplate.convertAndSend(WebSocketConsts.PUSH_SERVER, server);

        // 推送消息
        List upcomingMessages = new CacheMgr().getCache(CacheKey.CacheMessage.name(), new HashMap<String, String>(3) {{
            put("type", "0");
            put("status", "0");
        }}, null);
        List alarmMessages = new CacheMgr().getCache(CacheKey.CacheMessage.name(), new HashMap<String, String>(3) {{
            put("type", "1");
            put("status", "0");
        }}, null);
        List personalMessages = new CacheMgr().getCache(CacheKey.CacheMessage.name(), new HashMap<String, String>(3) {{
            put("type", "2");
            put("status", "0");
        }}, null);
        wsTemplate.convertAndSend(WebSocketConsts.PUSH_MESSAGE, new HashMap<String, Object>(4) {{
            put("upcomingMessages", upcomingMessages);
            put("alarmMessages", alarmMessages);
            put("personalMessages", personalMessages);
        }});
    }


    /**
     * syncDataTask
     */
    @Scheduled(cron = "${cxnet.scheduled.syncDataTaskCron}")
    public void syncDataTask() {
        new CacheMgr().setCache(CacheKey.CacheDictType.name(), DataMgr.getDictType());
        new CacheMgr().setCache(CacheKey.CacheDictData.name(), DataMgr.getDictData());
        new CacheMgr().setCache(CacheKey.CacheMessage.name(), DataMgr.getMessageData());
    }


}

