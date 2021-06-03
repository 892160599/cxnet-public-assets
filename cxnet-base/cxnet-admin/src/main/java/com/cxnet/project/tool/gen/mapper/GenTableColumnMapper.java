package com.cxnet.project.tool.gen.mapper;

import com.cxnet.project.tool.gen.domain.GenTableColumn;

import java.util.List;

/**
 * 业务字段 数据层
 *
 * @author cxnet
 */
public interface GenTableColumnMapper {
    /**
     * 根据表名称查询列信息
     *
     * @param tableName 表名称
     * @return 列信息
     */
    public List<GenTableColumn> selectDbTableColumnsByName(String tableName);

    /**
     * 查询业务字段列表
     *
     * @param tableId 业务字段编号
     * @return 业务字段集合
     */
    public List<GenTableColumn> selectGenTableColumnListByTableId(String tableId);

    /**
     * 查询业务关联字段列表
     *
     * @param tableId 表ID
     * @return 业务字段集合
     */
    public List<GenTableColumn> selectGenTableColumnRelatedListByTableId(String tableId);

    /**
     * 新增业务字段
     *
     * @param genTableColumn 业务字段信息
     * @return 结果
     */
    public int insertGenTableColumn(GenTableColumn genTableColumn);

    /**
     * 修改业务字段
     *
     * @param genTableColumn 业务字段信息
     * @return 结果
     */
    public int updateGenTableColumn(GenTableColumn genTableColumn);

    /**
     * 批量删除业务字段
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteGenTableColumnByIds(String[] ids);

    /**
     * 删除业务字段
     *
     * @param id 需要删除的数据ID
     * @return 结果
     */
    public int deleteGenTableColumnById(String id);
}