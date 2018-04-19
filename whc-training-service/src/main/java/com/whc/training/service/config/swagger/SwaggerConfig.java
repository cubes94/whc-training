package com.whc.training.service.config.swagger;

import com.whc.training.service.config.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月17 10:11
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final ApplicationProperties properties;

    @Autowired
    public SwaggerConfig(ApplicationProperties properties) {
        this.properties = properties;
    }

    /**
     * Swagger配置bean
     *
     * @return Swagger配置bean
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(properties.getApplicationName())
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")
                .select()
                .paths(PathSelectors.regex("/api/.*"))
                .build()
                .apiInfo(apiInfo());


    }

    /**
     * api基本信息
     *
     * @return api基本信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("whc-training")
                .description("whc-training的API")
                .termsOfServiceUrl(properties.getAppServerHostUrl() + "/swagger-ui.html")
                .version("1.0")
                .contact(new Contact("wuhaichao", "https://github.com/cubes94", "whc_94@163.com"))
                .build();
    }
}
