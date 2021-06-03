package com.cxnet.project.tool.gen.service;

import com.cxnet.project.tool.gen.domain.GenTableColumn;

import java.util.List;

/**
 * 业务字段 服务层
 *
 * @author cxnet
 */
public interface GenTableColumnServiceI {
    /**
     * 查询业务字段列表
     *
     * @param tableId 表ID
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
     * 删除业务字段信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteGenTableColumnByIds(String ids);

    /**
     * 删除业务字段
     *
     * @param id 需要删除的数据ID
     * @return 结果
     */
    public int deleteGenTableColumnById(String id);


    /**
     * 查询据库列表字段
     *
     * @param tableName 表名称
     * @return 数据库表字段集合
     */
    public List<GenTableColumn> selectDbTableColumnsByName(String tableName);
}
