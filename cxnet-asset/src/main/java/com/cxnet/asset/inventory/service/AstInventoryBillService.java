package com.cxnet.asset.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.inventory.domain.AstInventoryBill;
import com.cxnet.asset.inventory.domain.vo.AstInventoryBillVo;
import com.cxnet.rpc.domain.basedata.BdPersonnel;

import java.util.List;

/**
 * (AstInventoryBill)表服务接口
 *
 * @author zhangyl
 * @since 2021-04-02 10:02:36
 */
public interface AstInventoryBillService extends IService<AstInventoryBill> {

    AstInventoryBill insertList(AstInventoryBill astInventoryBill);

    AstInventoryBillVo selectAll(String billNo, String applyDeptCode, String empCode);

    String update(AstInventoryBillVo astInventoryBillVo);

    AstInventoryBill getList(AstInventoryBill astInventoryBill);

    AstInventoryBill updateList(AstInventoryBill astInventoryBill);

    List<AstInventoryBill> getBillOne(String id);

    List<BdPersonnel> selList(List<String> deptIds);

}

