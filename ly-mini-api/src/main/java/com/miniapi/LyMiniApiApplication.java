package com.miniapi;

import tk.mybatis.spring.annotation.MapperScan ;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.miniapi","com.ly.service","com.ly.common.org.n3r.idworker","com.ly.common.utils","com.miniapi"})
@MapperScan(basePackages = "com.ly.mapper")
public class LyMiniApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LyMiniApiApplication.class, args);
    }

}
