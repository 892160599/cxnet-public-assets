package com.cxnet.asset.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.inventory.domain.AstInventoryList;
import com.cxnet.asset.inventory.domain.vo.AstInventoryBillVo;

import java.util.List;
import java.util.Map;

/**
 * 资产盘点子表(AstInventoryList)表服务接口
 *
 * @author zhangyl
 * @since 2021-04-06 09:34:12
 */
public interface AstInventoryListService extends IService<AstInventoryList> {

    List<AstInventoryList> selectDept(String billNo);

    boolean insertBatchList(List<AstInventoryList> astInventoryLists);

    List<AstInventoryList> selectPan(String billNo, String unitId, String applyDeptCode, String empCode);

    List<AstInventoryList> selectTory(String billNo, String userId);

    List<AstInventoryList> updateTory(List<AstInventoryList> astInventoryLists);

    String insertDispose(List<AstInventoryList> astInventoryLists);
}

