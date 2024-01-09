package com.cai.violetcai.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration     //声明全局配置类
public class WebConfig extends WebMvcConfigurerAdapter {

    /*
    * 1.任何东西都允许跨域
    * 2.来源
    * 3.方法
    * 4.允许携带信息
    * 5.最大响应时间
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("Http://localhost:8080", "null")
                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
