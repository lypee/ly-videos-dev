package com.miniapi;

import com.miniapi.controller.MiniInterceptor.MiniInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//注解配置
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry){
        //访问所有资源
        resourceHandlerRegistry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/").addResourceLocations("file:C:/dev-reposity/"); //
    }

    //注册拦截器的bean
    @Bean
    public MiniInterceptor miniInterceptor(){
        return new MiniInterceptor() ;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

//        registry.addInterceptor(miniInterceptor()).addPathPatterns("/user/**")
//                .addPathPatterns("/video/upload", "/video/uploadCover",
//                        "/video/userLike", "/video/userUnLike",
//                        "/video/saveComment")
//                .addPathPatterns("/bgm/**")
//                .excludePathPatterns("/user/queryPublisher");
        registry.addInterceptor(miniInterceptor()).addPathPatterns("/user/**") ;
        super.addInterceptors(registry);
    }


}
