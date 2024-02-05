package com.cai.violetcai.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration     //声明全局配置类
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

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
                .allowedOrigins("http://localhost:8080", "null")
                //.allowedOrigins("http://cjl02.top", "null")
                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
