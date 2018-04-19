package com.whc.training.service.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;
import com.whc.training.service.config.db.properties.PrimaryDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * mysql 数据源配置
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月19 11:55
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({PrimaryDataSourceProperties.class})
public class MyBatisConfig {

    @Autowired
    private PrimaryDataSourceProperties primaryDataSourceProperties;

    /**
     * 数据源配置
     *
     * @return 数据源配置
     */
    @Bean(name = "primaryDataSource")
    @Primary
    public DataSource primaryDataSource() {
        log.info("Configuring primaryDataSource");
        DruidDataSource datasource = new DruidDataSource();
        datasource.setDriverClassName(primaryDataSourceProperties.getProperty("driver-class-name"));
        datasource.setUrl(primaryDataSourceProperties.getProperty("url"));
        datasource.setUsername(primaryDataSourceProperties.getProperty("username"));
        datasource.setPassword(primaryDataSourceProperties.getProperty("password"));
        datasource.setDefaultAutoCommit(false);
        datasource.setMaxActive(20);
        datasource.setInitialSize(10);
        datasource.setMaxWait(60000);
        datasource.setMinIdle(5);
        datasource.setTimeBetweenEvictionRunsMillis(60000);
        datasource.setMinEvictableIdleTimeMillis(30000);
        datasource.setValidationQuery("select 'x'");
        datasource.setTestWhileIdle(true);
        datasource.setTestOnBorrow(true);
        datasource.setTestOnReturn(true);
        datasource.setPoolPreparedStatements(true);
        datasource.setMaxOpenPreparedStatements(20);
        return datasource;
    }

    /**
     * 数据源配置
     *
     * @return 数据源配置
     */
    @Bean(name = "primarySqlSessionFactory")
    public SqlSessionFactory primarySqlSessionFactory(
            @Qualifier("primaryDataSource") DataSource primaryDataSource) {
        try {
            SqlSessionFactoryBean readSqlSessionFactory = new SqlSessionFactoryBean();
            readSqlSessionFactory.setDataSource(primaryDataSource);
            readSqlSessionFactory.setTypeAliasesPackage("com.whc.training.service.*.domain.model");
            PageHelper pageHelper = new PageHelper();
            Properties properties = new Properties();
            properties.setProperty("reasonable", "true");
            properties.setProperty("supportMethodsArguments", "true");
            properties.setProperty("returnPageInfo", "check");
            properties.setProperty("params", "count=countSql");
            pageHelper.setProperties(properties);
            readSqlSessionFactory.setPlugins(new Interceptor[]{pageHelper});
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            readSqlSessionFactory.setMapperLocations(resolver.getResources("classpath:mapper/*/*.xml"));
            return readSqlSessionFactory.getObject();
        } catch (Exception e) {
            log.error("Could not configure mybatis session factory", e);
            return null;
        }
    }

    /**
     * 数据库事务配置
     *
     * @return 数据库事务配置
     */
    @Bean(name = "primaryDataSourceTransactionManager")
    @Primary
    public PlatformTransactionManager primaryDataSourceTransactionManager(
            @Qualifier("primaryDataSource") DataSource primaryDataSource) {
        log.info("-----------------CREATE primaryDataSourceTransactionManager----------------");
        return new DataSourceTransactionManager(primaryDataSource);
    }
}
