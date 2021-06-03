package com.cxnet.flow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.flow.domain.ActHiTaskinst;

import java.util.List;

/**
 * (ActHiTaskinst)表数据库访问层
 *
 * @author makejava
 * @since 2021-05-21 10:10:04
 */
public interface ActHiTaskinstMapper extends BaseMapper<ActHiTaskinst> {

    List<ActHiTaskinst> selectAll(ActHiTaskinst actHiTaskinst);

    List<ActHiTaskinst> selectListByPid(String processinstid);

    int updateHiById(ActHiTaskinst actHiTaskinst);
}