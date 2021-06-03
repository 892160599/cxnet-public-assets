package com.cxnet.ureport2.demo.mapper;

import com.cxnet.ureport2.demo.model.ReportContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReportContentMapper {
    int deleteByPrimaryKey(String id);

    int insert(ReportContent record);

    int insertSelective(ReportContent record);

    ReportContent selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ReportContent record);

    int updateByPrimaryKey(ReportContent record);

    ReportContent findByFileName(String fileName);

    int deleteByFileName(String fileName);

    List<ReportContent> findAll();

    int updateContentByFileName(@Param("fileName") String fileName,
                                @Param("content") String content);
}