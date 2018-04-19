package com.whc.training.service.config.db;

import org.aspectj.lang.annotation.After;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Properties;

/**
 * mybatis XML文件扫描配置
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月19 15:19
 */
@Configuration
public class MapperScannerConfig {

    /**
     * mybatis XML文件扫描配置
     *
     * @return mybatis XML文件扫描配置
     */
    @Bean(name = "primaryMapperScannerConfigurer")
    @After("primarySqlSessionFactory")
    public MapperScannerConfigurer primaryMapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("primarySqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.whc.training.service.*.dao");
        Properties properties = new Properties();
        properties.setProperty("mappers", "com.whc.training.service.config.db.BaseMapper");
        properties.setProperty("IDENTITY", "MYSQL");
        properties.setProperty("notEmpty", "false");
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }
}
