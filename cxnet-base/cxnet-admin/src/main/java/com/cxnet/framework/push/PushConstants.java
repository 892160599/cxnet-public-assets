package com.cxnet.framework.push;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author zhangyl
 * @date 2021-5-26 13:36
 */
@Component
public class PushConstants {

    /**
     * 根据cid推送
     */
    public static final String PUSHBYCID = "/push/single/cid";

    /**
     * 根据别名推送
     */
    public static final String PUSHBYALIAS = "/push/single/alias";

    /**
     * 根据cid批量推送
     */
    public static final String PUSHBATCHBYCID = "/push/single/batch/cid";

    /**
     * 执行群推
     */
    public static final String PUSHALL = "/push/all";
}
