package com.lino.secondkill.config;

import com.lino.secondkill.access.AccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

//是一个配置
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Autowired
    UserArgumentResolver userArgumentResolver;
    @Autowired
    AccessInterceptor accessInterceptor;

    //方法用处：给参数赋值的，框架会回调这个方法，往controller里面的参数给他赋值
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        //往argumentResolvers里面添加一个值
        argumentResolvers.add(userArgumentResolver);
    }


    //注册拦截器

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessInterceptor);
    }
}
