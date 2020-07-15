package com.whc.training.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 接口层启动类
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月16 17:33
 */
@EnableEurekaClient
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.whc.training.service.*.dao"})
@SpringBootApplication(scanBasePackages = "com.whc")
public class TrainingServiceApp {

    /**
     * 启动方法
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(TrainingServiceApp.class, args);
    }
}
