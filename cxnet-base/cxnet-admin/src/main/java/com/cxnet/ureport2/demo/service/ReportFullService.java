package com.cxnet.ureport2.demo.service;

import com.cxnet.ureport2.demo.model.ReportContent;

import java.util.List;

/**
 * @author cxnet
 * @date 2019-04-25
 **/
public interface ReportFullService {
    /**
     * 按文件名称查找报表模板内容
     *
     * @param fileName 文件名称
     * @return 报表模板内容
     */
    ReportContent findByFileName(String fileName);

    /**
     * 按id查找报表模板内容
     *
     * @param id id
     * @return 报表模板内容
     */
    ReportContent findById(String id);

    /**
     * 保存报表模板文件内容
     *
     * @param fileName 文件名称
     * @param content  模板内容
     */
    void save(String fileName, String content);

    /**
     * 更新报表模板文件内容
     *
     * @param fileName 文件名称
     * @param content  模板内容
     */
    void update(String fileName, String content);

    /**
     * 按文件名称删除报表模板
     *
     * @param fileName 文件名称
     */
    boolean deleteByFileName(String fileName);

    /**
     * 按id删除报表模板
     *
     * @param id id
     */
    boolean deleteById(String id);

    /**
     * 查询全部报表模板数据
     *
     * @return list
     */
    List<ReportContent> findAll();
}
