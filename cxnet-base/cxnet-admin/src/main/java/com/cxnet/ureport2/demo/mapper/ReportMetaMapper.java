package com.cxnet.ureport2.demo.mapper;

import com.cxnet.ureport2.demo.model.ReportMeta;

public interface ReportMetaMapper {
    int deleteByPrimaryKey(String id);

    int insert(ReportMeta record);

    int insertSelective(ReportMeta record);

    ReportMeta selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ReportMeta record);

    int updateByPrimaryKey(ReportMeta record);

    int deleteByPath(String path);

    ReportMeta selectByPath(String path);
}