package com.cxnet.basic.expBasicConfig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.basic.expBasicConfig.domain.ExpBasicConfig;
import com.cxnet.common.utils.tree.Zone;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 报销单据配置(ExpBasicConfig)表数据库访问层
 *
 * @author caixx
 * @since 2020-09-11 15:13:54
 */
public interface ExpBasicConfigMapper extends BaseMapper<ExpBasicConfig> {

    /**
     * 查询报销单据tree
     *
     * @param expBasicConfig
     * @return
     */
    @Select("select model_id id,model_code code,model_name name,parent_id pid from sys_model\n" +
            " where model_code !='0' \n" +
            " order by order_num,model_id")
    List<Zone> selectZone(ExpBasicConfig expBasicConfig);
}