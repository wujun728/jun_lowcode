package com.jqp.ddd.infrastructure.jdbc;

import javax.sql.DataSource;

/**
 * JDBC配置
 * 不依赖Spring，支持独立运行环境
 *
 * @author JQP
 * @date 2026/02/28
 */
public class JdbcConfig {

    private static DataSource dataSource;

    /**
     * 设置数据源（单例）
     */
    public static void setDataSource(DataSource ds) {
        JdbcConfig.dataSource = ds;
    }

    /**
     * 获取数据源
     */
    public static DataSource getDataSource() {
        if (dataSource == null) {
            throw new RuntimeException("DataSource未配置，请先调用JdbcConfig.setDataSource()");
        }
        return dataSource;
    }

    /**
     * 检查DataSource是否已配置
     */
    public static boolean isConfigured() {
        return dataSource != null;
    }
}
