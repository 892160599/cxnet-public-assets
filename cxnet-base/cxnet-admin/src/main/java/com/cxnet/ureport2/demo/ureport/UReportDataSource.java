package com.cxnet.ureport2.demo.ureport;

import com.bstek.ureport.definition.datasource.BuildinDatasource;
import com.bstek.ureport.exception.ReportException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * UReport数据源配置
 *
 * @author cxnet
 * @date 2019-04-23
 **/
@Slf4j
@Component
public class UReportDataSource implements BuildinDatasource {

    private final DataSource dataSource;

    public UReportDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 数据源名称
     */
    @Override
    public String name() {
        return UReportDataSource.class.getSimpleName();
    }

    /**
     * 获取数据源连接对象
     */
    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error("UReport2 内置数据源配置失败", e);
            throw new ReportException(e);
        }
    }
}
