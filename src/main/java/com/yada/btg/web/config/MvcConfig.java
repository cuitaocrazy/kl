package com.yada.btg.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zsy
 * @date 2019/12/20
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /**
     * 磁盘图片所在地址
     */
    @Value("${img.imgPath}")
    String imgPath;
    private final HandlerInterceptor yadaPageInterceptor;

    @Autowired
    public MvcConfig(HandlerInterceptor yadaPageInterceptor) {
        this.yadaPageInterceptor = yadaPageInterceptor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations(imgPath);
    }


    /**
     * 将拦截器注册到spring,同时设置都会拦截哪些请求
     *
     * @param registry registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //所有的query开头的方法都会走拦截器   处理get请求，带有page参数的
        registry.addInterceptor(yadaPageInterceptor).addPathPatterns("/*/query**");
    }
}
