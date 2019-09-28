package com.lk.o2o.config.web;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * 开启mvc，自动注入spring容器。  WebMvcConfigurationAdapter:配置视图解析器
 * 当一个类实现了这个接口(ApplicationContextAware)之后，这个类就可以方便获得ApplicationContext中所有的bean
 */
@Configuration
@EnableWebMvc //等价于<mvc:annotation-driven />
public class MvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware {
    //spring容器
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 静态资源配置
     */
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources");
    }

    /**
     * 定义默认的请求处理器
     */
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * 创建viewResolver
     */
    @Bean(name = "viewResolver")
    public ViewResolver createViewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        //设置Spring容器
        viewResolver.setApplicationContext(this.applicationContext);
        //取消缓存
        viewResolver.setCache(false);
        //设置解析的前缀
        viewResolver.setPrefix("/WEB-INF/html/");
        //设置解析的后缀
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    /**
     * 文件上传解析器
     */
    public CommonsMultipartResolver createMultipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("UTF-8");
        multipartResolver.setMaxUploadSize(20971520);
        multipartResolver.setMaxInMemorySize(20971520);
        return multipartResolver;
    }
}
