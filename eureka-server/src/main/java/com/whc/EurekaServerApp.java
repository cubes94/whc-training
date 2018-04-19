package com.whc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 服务注册中心
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月16 10:47
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApp {

    /**
     * 启动配置
     *
     * @param args args参数
     */
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApp.class, args);
    }
}
