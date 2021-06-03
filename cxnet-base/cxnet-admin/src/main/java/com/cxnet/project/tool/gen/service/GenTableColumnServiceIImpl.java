package com.cxnet.project.tool.gen.service;

import com.cxnet.common.core.text.Convert;
import com.cxnet.project.tool.gen.domain.GenTable;
import com.cxnet.project.tool.gen.domain.GenTableColumn;
import com.cxnet.project.tool.gen.mapper.GenTableColumnMapper;
import com.cxnet.project.tool.gen.util.GenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务字段 服务层实现
 *
 * @author cxnet
 */
@Service
public class GenTableColumnServiceIImpl implements GenTableColumnServiceI {
    @Autowired(required = false)
    private GenTableColumnMapper genTableColumnMapper;

    /**
     * 查询业务字段列表
     *
     * @param tableId 表ID
     * @return 业务字段集合
     */
    @Override
    public List<GenTableColumn> selectGenTableColumnListByTableId(String tableId) {
        return genTableColumnMapper.selectGenTableColumnListByTableId(tableId);
    }

    /**
     * 查询业务关联字段列表
     *
     * @param tableId 表ID
     * @return 业务字段集合
     */
    @Override
    public List<GenTableColumn> selectGenTableColumnRelatedListByTableId(String tableId) {
        return genTableColumnMapper.selectGenTableColumnRelatedListByTableId(tableId);
    }


    /**
     * 新增业务字段
     *
     * @param genTableColumn 业务字段信息
     * @return 结果
     */
    @Override
    public int insertGenTableColumn(GenTableColumn genTableColumn) {
        return genTableColumnMapper.insertGenTableColumn(genTableColumn);
    }

    /**
     * 修改业务字段
     *
     * @param genTableColumn 业务字段信息
     * @return 结果
     */
    @Override
    public int updateGenTableColumn(GenTableColumn genTableColumn) {
        return genTableColumnMapper.updateGenTableColumn(genTableColumn);
    }

    /**
     * 删除业务字段对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteGenTableColumnByIds(String ids) {
        return genTableColumnMapper.deleteGenTableColumnByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除业务字段
     *
     * @param id 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteGenTableColumnById(String id) {
        return genTableColumnMapper.deleteGenTableColumnById(id);
    }


    /**
     * 查询据库列表字段
     *
     * @param tableName 表名称
     * @return 数据库表字段集合
     */
    @Override
    public List<GenTableColumn> selectDbTableColumnsByName(String tableName) {
        List<GenTableColumn> genTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
        for (GenTableColumn column : genTableColumns) {
            GenUtils.initColumnField(column);
        }
        return genTableColumns;
    }
}