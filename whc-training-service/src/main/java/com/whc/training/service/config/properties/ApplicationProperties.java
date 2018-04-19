package com.whc.training.service.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 配置信息
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月17 10:24
 */
@Configuration
public class ApplicationProperties {

    @Getter
    @Value("${spring.application.name:application}")
    private String applicationName;

    @Getter
    @Value("${app.server.host.url}")
    private String appServerHostUrl;
}
