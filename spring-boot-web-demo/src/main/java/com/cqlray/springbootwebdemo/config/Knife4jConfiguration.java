package com.cqlray.springbootwebdemo.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableKnife4j
public class Knife4jConfiguration {
    @Value("${knife4j.enable:false}")
    private Boolean enable;

    @Bean(value = "defaultApi")
    public Docket defaultApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enable)
                .apiInfo(apiInfo())
                //分组名称
                .groupName("1.0 版本")
                .select()
                //这里指定Controller扫描包路径(项目路径也行)
                .apis(RequestHandlerSelectors.basePackage("com.cqlray.springbootwebdemo.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * API 说明，包含作者、简介、版本、host、服务URL
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("微服务")
                .description("微服务API文档")
                .contact(new Contact("M", "http://www.XXX.com/", "xxx@qq.com"))//作者信息
                .version("1.0")//定义api 版本号
                .build();
    }
}
