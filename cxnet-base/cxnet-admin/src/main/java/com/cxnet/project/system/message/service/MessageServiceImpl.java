package com.cxnet.project.system.message.service;

import java.util.List;

import com.cxnet.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cxnet.project.system.message.domain.Message;
import com.cxnet.project.system.message.mapper.MessageMapper;

/**
 * 消息记录Service业务层处理
 *
 * @author cxnet
 * @date 2020-03-11
 */
@Service
public class MessageServiceImpl implements MessageServiceI {
    @Autowired(required = false)
    private MessageMapper messageMapper;

    /**
     * 查询消息记录
     *
     * @param messageId 消息记录ID
     * @return 消息记录
     */
    @Override
    public Message selectMessageById(String messageId) {
        return messageMapper.selectMessageById(messageId);
    }

    /**
     * 查询消息记录集合
     *
     * @param message 消息记录
     * @return 消息记录
     */
    @Override
    public List<Message> selectMessageList(Message message) {
        return messageMapper.selectMessageList(message);
    }

    /**
     * 新增消息记录
     *
     * @param message 消息记录
     * @return 结果
     */
    @Override
    public int insertMessage(Message message) {
        message.setCreateTime(DateUtils.getNowDate());
        return messageMapper.insertMessage(message);
    }

    /**
     * 批量新增消息记录
     *
     * @param messages 消息记录
     * @return 结果
     */
    @Override
    public int insertBatchMessage(List<Message> messages) {
        return messageMapper.insertBatchMessage(messages);
    }

    /**
     * 修改消息记录
     *
     * @param message 消息记录
     * @return 结果
     */
    @Override
    public int updateMessage(Message message) {
        message.setUpdateTime(DateUtils.getNowDate());
        return messageMapper.updateMessage(message);
    }

    /**
     * 批量修改消息记录
     *
     * @param messages 消息记录
     * @return 结果
     */
    @Override
    public int updateBatchMessage(List<Message> messages) {
        return messageMapper.updateBatchMessage(messages);
    }

    /**
     * 批量删除消息记录
     *
     * @param messageIds 需要删除的消息记录ID
     * @return 结果
     */
    @Override
    public int deleteMessageByIds(String[] messageIds) {
        return messageMapper.deleteMessageByIds(messageIds);
    }

    /**
     * 删除消息记录信息
     *
     * @param messageId 消息记录ID
     * @return 结果
     */
    @Override
    public int deleteMessageById(String messageId) {
        return messageMapper.deleteMessageById(messageId);
    }


    /**
     * 修改消息记录状态
     *
     * @param messageId 主键
     * @param type      类型
     * @param status    状态
     * @return 结果
     */
    @Override
    public int updateMessageStatus(String messageId, String type, String status) {
        return messageMapper.updateMessageStatus(messageId, type, status);
    }
}
