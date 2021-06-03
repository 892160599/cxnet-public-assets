package com.cxnet.asset.allot.mapper;

import com.cxnet.asset.allot.domain.AstAllotBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.rpc.domain.basedata.BdPersonnel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 资产单位调拨主表(AstAllotBill)表数据库访问层
 *
 * @author zhaoyi
 * @since 2021-04-09 14:33:01
 */
public interface AstAllotBillMapper extends BaseMapper<AstAllotBill> {

    List<AstAllotBill> selectAll(AstAllotBill astAllotBill);

    @Select("select t.* from bd_personnel t where t.unit_id=#{unitId} and t.del_flag='0' and t.status='0' and t.is_create='1'")
    List<BdPersonnel> selectUserList(@Param("unitId") String unitId);

    @Select("select t.personnel_id from bd_personnel t where t.user_id=#{userId} and t.del_flag='0' and t.status='0' and t.is_create='1'")
    String selectUserListByUserId(@Param("userId") String userId);
}

