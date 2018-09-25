package com.whc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Configuration;

/**
 * 配置中心
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年09月21 16:14
 */
@Configuration
@EnableAutoConfiguration
@EnableConfigServer
public class ConfigServerApp {

    /**
     * 启动配置
     *
     * @param args args参数
     */
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApp.class, args);
    }

}
