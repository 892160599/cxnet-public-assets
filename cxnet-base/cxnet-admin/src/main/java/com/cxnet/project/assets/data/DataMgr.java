package com.cxnet.project.assets.data;

import com.cxnet.common.utils.spring.SpringUtils;
import com.cxnet.project.system.message.domain.Message;
import com.cxnet.project.system.message.service.MessageServiceI;
import com.cxnet.project.system.dict.domain.SysDictData;
import com.cxnet.project.system.dict.domain.SysDictType;
import com.cxnet.project.system.dict.service.SysDictDataServiceI;
import com.cxnet.project.system.dict.service.SysDictTypeServiceI;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Chanyin尹强
 * @version v1.0.0
 * @title DataMgr.java
 * @package com.cxnet.project.assets.data
 * @description 数据管理类
 * @email yinqiang2007@163.com
 * @date 2020/1/6 12:49
 * @copyright © 2020 南京财信网络科技有限公司 版权所有
 */
public class DataMgr {

    // 消息记录 服务接口
    private static MessageServiceI messageService = SpringUtils.getBean(MessageServiceI.class);

    // 字典数据 服务接口
    private static SysDictTypeServiceI dictTypeService = SpringUtils.getBean(SysDictTypeServiceI.class);
    private static SysDictDataServiceI dictDataService = SpringUtils.getBean(SysDictDataServiceI.class);


    /**
     * 获取消息记录数据
     */
    public static Map getMessageData() {
        List<Message> messages = messageService.selectMessageList(new Message());
        return messages.stream().collect(Collectors.toMap(Message::getMessageId, Function.identity(), (key1, key2) -> key2));
    }

    /**
     * 获取字典类型
     */
    public static Map getDictType() {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeList(new SysDictType());
        return dictTypes.stream().collect(Collectors.toMap(SysDictType::getDictId, Function.identity(), (key1, key2) -> key2));
    }

    /**
     * 获取字典数据
     */
    public static Map getDictData() {
        List<SysDictData> dictDatas = dictDataService.selectDictDataList(new SysDictData());
        return dictDatas.stream().collect(Collectors.toMap(SysDictData::getDictCode, Function.identity(), (key1, key2) -> key2));
    }


}
