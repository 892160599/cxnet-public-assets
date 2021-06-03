package com.cxnet.rpc.service.message;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.rpc.domain.message.MobMessageBill;

import java.util.List;
import java.util.Map;

/**
 * 消息表(MobMessageBill)表服务接口
 *
 * @author zhangyl
 * @since 2021-05-21 14:01:12
 */
public interface MobMessageBillService extends IService<MobMessageBill> {

    Map<String, List<MobMessageBill>> selectAll(MobMessageBill mobMessageBill);

}

