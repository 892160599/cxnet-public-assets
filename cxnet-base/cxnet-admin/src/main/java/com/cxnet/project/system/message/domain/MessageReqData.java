package com.cxnet.project.system.message.domain;

import lombok.Data;

/**
 * 消息记录 请求数据
 *
 * @author Chanyin
 * @date 2020-03-15
 */
@Data
public class MessageReqData {

    /**
     * ids字符串
     */
    private String ids;

    /**
     * 类型
     */
    private String type;

    /**
     * 已读类型
     */
    private String readType;

    /**
     * 状态
     */
    private String status;
}
