package com.cxnet.ureport2.demo.service;

/**
 * @author cxnet
 * @date 2019-04-24
 **/
public interface ReportService {

    /**
     * 存储报表模板文件路径
     *
     * @param path path
     */
    void save(String path);

    /**
     * 删除报表数据
     *
     * @param path path
     */
    void delete(String path);
}
