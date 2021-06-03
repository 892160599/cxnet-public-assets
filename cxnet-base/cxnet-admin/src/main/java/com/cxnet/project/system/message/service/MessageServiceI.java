package com.cxnet.project.system.message.service;

import com.cxnet.project.system.message.domain.Message;

import java.util.List;

/**
 * 消息记录Service接口
 *
 * @author cxnet
 * @date 2020-03-11
 */
public interface MessageServiceI {
    /**
     * 查询消息记录
     *
     * @param messageId 消息记录ID
     * @return 消息记录
     */
    public Message selectMessageById(String messageId);

    /**
     * 查询消息记录集合
     *
     * @param message 消息记录
     * @return 消息记录集合
     */
    public List<Message> selectMessageList(Message message);

    /**
     * 新增消息记录
     *
     * @param message 消息记录
     * @return 结果
     */
    public int insertMessage(Message message);

    /**
     * 批量新增消息记录
     *
     * @param messages 消息记录
     * @return 结果
     */
    public int insertBatchMessage(List<Message> messages);

    /**
     * 修改消息记录
     *
     * @param message 消息记录
     * @return 结果
     */
    public int updateMessage(Message message);

    /**
     * 批量修改消息记录
     *
     * @param messages 消息记录
     * @return 结果
     */
    public int updateBatchMessage(List<Message> messages);

    /**
     * 批量删除消息记录
     *
     * @param messageIds 需要删除的消息记录ID
     * @return 结果
     */
    public int deleteMessageByIds(String[] messageIds);

    /**
     * 删除消息记录信息
     *
     * @param messageId 消息记录ID
     * @return 结果
     */
    public int deleteMessageById(String messageId);


    /**
     * 修改消息记录状态
     *
     * @param messageId 主键
     * @param type      类型
     * @param status    状态
     * @return 结果
     */
    public int updateMessageStatus(String messageId, String type, String status);
}
