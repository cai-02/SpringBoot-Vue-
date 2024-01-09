package com.cai.violetcai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.cai.violetcai.dao")
@SpringBootApplication
public class VioletCaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VioletCaiApplication.class, args);
    }

}
