package com.whc.training.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启动类
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月18 19:17
 */
@SpringBootApplication(scanBasePackages = "com.whc")
@EnableFeignClients(basePackages = {"com.whc.training.api.*.service"})
@EnableDiscoveryClient
public class TrainingAppApp {

    /**
     * 启动入口
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(TrainingAppApp.class, args);
    }
}
