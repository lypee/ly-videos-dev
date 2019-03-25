package com.miniapi;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * 继承springboot 相当于使用web.xml的形式进行启动
 */
public class WarStartApplication extends SpringBootServletInitializer {
    /**
     * 重写configure方法
     * @param builder
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(LyMiniApiApplication.class) ;
    }
}
