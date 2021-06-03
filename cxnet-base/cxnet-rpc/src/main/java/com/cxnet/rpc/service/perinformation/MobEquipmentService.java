package com.cxnet.rpc.service.perinformation;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.rpc.domain.perinformation.MobEquipment;

/**
 * 设备绑定表(MobEquipment)表服务接口
 *
 * @author zhangyl
 * @since 2021-05-14 10:18:05
 */
public interface MobEquipmentService extends IService<MobEquipment> {

    /**
     * 保存绑定设备的信息
     *
     * @param mobEquipment
     * @return
     */
    int insertEquipment(MobEquipment mobEquipment);
}

