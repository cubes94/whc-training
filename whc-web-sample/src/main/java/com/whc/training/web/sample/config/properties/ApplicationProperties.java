package com.whc.training.web.sample.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 配置信息
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年11月26 17:42
 */
@Component
public class ApplicationProperties {

    @Getter
    @Value("${spring.profiles.active}")
    private String applicationName;

}
