package com.whc.crawler.api.config;

import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author whc
 * @version 1.0.0
 * @since 2018年09月25 17:02
 */
@Configuration
public class CrawlerFeignConfiguration {

    /**
     * 重试机制 失败不重试
     *
     * @return bean
     */
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(100, 1, 0);
    }

    /**
     * 请求配置
     *
     * @return bean
     */
    @Bean
    Request.Options feignOptions() {
        return new Request.Options(10000, 10 * 60 * 1000);
    }

}
