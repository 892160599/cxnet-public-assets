package com.cxnet.asset.allot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.allot.domain.AstAllotBill;
import com.cxnet.asset.allot.domain.vo.AstAllotVo;
import com.cxnet.flow.domain.CommonProcessRequest;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.rpc.domain.basedata.BdPersonnel;

import java.util.List;

/**
 * 资产单位调拨主表(AstAllotBill)表服务接口
 *
 * @author zhaoyi
 * @since 2021-04-09 14:33:01
 */
public interface AstAllotBillService extends IService<AstAllotBill> {

    String saveAstAllotVo(AstAllotVo astAllotVo);

    String updateAstAllotVo(AstAllotVo astAllotVo);

    String delete(List<String> ids);

    String allotSubmit(CommonProcessRequest commonProcessRequest);

    String allotAudit(CommonProcessRequest commonProcessRequest);

    String allotBack(CommonProcessRequest commonProcessRequest);

    String taskBack(CommonProcessRequest commonProcessRequest);

    List<AstAllotBill> selectAll(AstAllotBill astAllotBill);

    List<BdPersonnel> selectUserList(String unitId);

    String selectUserListByUserId(String userId);
}

