package com.cxnet.rpc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.rpc.domain.ExpCost;

import java.util.List;

/**
 * 报销费用明细子表(ExpCost)表服务接口
 *
 * @author makejava
 * @since 2020-09-14 10:41:41
 */
public interface ExpCostServiceRpc extends IService<ExpCost> {

    List<ExpCost> getConCostById(String conId);
}