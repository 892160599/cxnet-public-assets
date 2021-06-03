package com.cxnet.project.system.message.controller;

import java.util.ArrayList;
import java.util.List;

import com.cxnet.project.assets.data.DataMgr;
import com.cxnet.project.assets.data.cache.CacheKey;
import com.cxnet.project.assets.data.cache.CacheMgr;
import com.cxnet.common.core.text.Convert;
import com.cxnet.project.system.message.domain.MessageReqData;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.project.system.message.domain.Message;
import com.cxnet.project.system.message.service.MessageServiceI;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.utils.poi.ExcelUtil;

import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 消息记录控制层
 *
 * @author cxnet
 * @date 2020-03-11
 */
@Slf4j
@RestController
@Api(tags = "消息记录")
@RequestMapping("/system/message")
public class SysMessageController extends BaseController {

    @Autowired(required = false)
    private MessageServiceI messageService;


    /**
     * 查询消息记录集合
     *
     * @param message 消息记录
     */
    @ApiOperation("查询消息记录集合")
    @GetMapping
    public AjaxResult listMessage(Message message) {
        startPage();
        List<Message> list = messageService.selectMessageList(message);
        return success(getDataTable(list));
    }


    /**
     * 查询消息记录明细
     *
     * @param id 消息记录ID
     */
    @ApiOperation("查询消息记录明细")
    @GetMapping("/{id}")
    public AjaxResult getMessage(@PathVariable String id) {
        return success(messageService.selectMessageById(id));
    }

    /**
     * 新增消息记录
     *
     * @param message 消息记录
     */
    @ApiOperation("新增消息记录")
    @Log(title = "新增消息记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult addMessage(@RequestBody Message message) {
        return success(messageService.insertMessage(message));
    }

    /**
     * 修改消息记录
     *
     * @param message 消息记录
     */
    @ApiOperation("修改消息记录")
    @Log(title = "修改消息记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateMessage(@Validated @RequestBody Message message) {
        return success(messageService.updateMessage(message));
    }

    /**
     * 删除消息记录
     *
     * @param id 消息记录ID
     */
    @ApiOperation("删除消息记录")
    @Log(title = "删除消息记录", businessType = BusinessType.DELETE)
    @DeleteMapping()
    public AjaxResult deleteMessage(String[] id) {
        return success(messageService.deleteMessageByIds(id));
    }

    /**
     * 导出消息记录
     *
     * @param message 消息记录
     */
    @ApiOperation("导出消息记录")
    @Log(title = "导出消息记录", businessType = BusinessType.EXPORT)
    @PutMapping("/exportMessage")
    public AjaxResult exportMessage(Message message) {
        List<Message> list = messageService.selectMessageList(message);
        ExcelUtil<Message> util = new ExcelUtil<>(Message.class);
        return util.exportExcel(list, "消息记录数据");

    }

    /**
     * 修改消息记录状态
     *
     * @param messageReqData 消息记录请求参数
     */
    @ApiOperation("修改消息记录状态")
    @Log(title = "修改消息记录状态 ", businessType = BusinessType.UPDATE)
    @PutMapping("/updateMessageStatus")
    public AjaxResult updateMessageStatus(@Validated @RequestBody MessageReqData messageReqData) {
        if ("all".equals(messageReqData.getReadType())) {
            messageService.updateMessageStatus(null, messageReqData.getType(), "1");
        } else {
            String[] idArr = Convert.toStrArray(messageReqData.getIds());
            ArrayList<Message> lstMsg = new ArrayList<>();
            for (String id : idArr) {
                Message message = new Message();
                message.setMessageId(id);
                message.setStatus("1");
                message.setType(messageReqData.getType());
                lstMsg.add(message);
            }
            messageService.updateBatchMessage(lstMsg);
        }
        new CacheMgr().setCache(CacheKey.CacheMessage.name(), DataMgr.getMessageData());
        return success();

    }
}
