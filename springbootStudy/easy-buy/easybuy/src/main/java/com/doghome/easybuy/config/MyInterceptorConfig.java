package com.doghome.easybuy.config;

import com.doghome.easybuy.interceptor.UserCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置类  (过滤,放行页面)
 */
@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private UserCheckInterceptor userCheckInterceptor;

    @Autowired
    public MyInterceptorConfig(UserCheckInterceptor userCheckInterceptor) {
        this.userCheckInterceptor = userCheckInterceptor;
    }

    //把所有页面给放行  controller拦截
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userCheckInterceptor)
                .addPathPatterns("/news/**")
                .addPathPatterns("/product/**")
                .addPathPatterns("/order/**")
                .addPathPatterns("/address/**")
                .addPathPatterns("/loginOut")
                .addPathPatterns("/pc/**")
                .addPathPatterns("/collect/**")
                .addPathPatterns("/car/**")

                .excludePathPatterns("/news/getNewsList")
                .excludePathPatterns("/news/selectNewsById")
                .excludePathPatterns("/product/showProductList")
                .excludePathPatterns("/pc/showPCList")
                .excludePathPatterns("/product/findProductByEs")
                .excludePathPatterns("/product/selectProduct")
                .excludePathPatterns("/car/getCar")
        ;
    }


}
