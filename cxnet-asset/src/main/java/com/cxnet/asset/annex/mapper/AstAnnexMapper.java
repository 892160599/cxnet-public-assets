package com.cxnet.asset.annex.mapper;

import com.cxnet.asset.annex.domain.AstAnnex;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 资产附件关联表(AstAnnex)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-25 10:55:12
 */
public interface AstAnnexMapper extends BaseMapper<AstAnnex> {

    @Select(" select t0.*,t1.create_time,t1.create_name from AST_ANNEX t0 " +
            " inner join SYS_FILE_UPLOAD t1 " +
            " on t0.file_id = t1.file_id " +
            " where t0.ast_id = #{astId} and t0.annex_type = #{annexType}")
    List<AstAnnex> listByAstIdAndAnnexType(@Param("astId") String astId, @Param("annexType") String annexType);
}

