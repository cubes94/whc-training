package com.whc.training.service.config.db.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * 数据源配置信息
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月19 14:11
 */
@ConfigurationProperties(prefix="spring.datasource")
public class PrimaryDataSourceProperties extends Properties {

    private static final long serialVersionUID = -5574539188156582181L;

    private String url;

    private String username;

    private String password;

    private String driverClassName;
}
