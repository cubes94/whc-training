package com.whc.training.web.sample.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
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

    /**
     * 注入配置文件
     */
    @Getter
    @Value("${spring.profiles.active}")
    private String applicationName;

    /**
     * 注入普通字符
     */
    @Getter
    @Value("Constant String")
    private String constantStr;

    /**
     * 注入操作系统属性
     */
    @Getter
    @Value("#{systemProperties['os.name']}")
    private String osName;

    /**
     * 注入表达式结果
     */
    @Getter
    @Value("#{T(java.lang.Math).random() * 100}")
    private Integer randomNumber;

    /**
     * 注入文件资源
     */
    @Getter
    @Value("classpath:application.properties")
    private Resource propertiesFile;

    /**
     * 注入网站资源
     */
    @Getter
    @Value("http://whcubes.com/")
    private Resource blogUrl;

    /**
     * 注入其他bean属性
     */
    @Getter
    @Value("#{dictController.applicationProperties}")
    private ApplicationProperties applicationProperties;
}
