package com.cxnet.rpc.service.message;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.rpc.domain.message.MobMessageList;

import java.util.List;

/**
 * 消息子表(MobMessageList)表服务接口
 *
 * @author zhangyl
 * @since 2021-05-21 14:01:48
 */
public interface MobMessageListService extends IService<MobMessageList> {

    int updateList(List<MobMessageList> mobMessageLists);
}

