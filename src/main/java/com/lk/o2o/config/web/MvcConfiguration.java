package com.lk.o2o.config.web;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
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

import javax.servlet.ServletException;

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
    //实现ApplicationContextAware中的方法，注入applicationContext这个spring容器中所有的bean
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 静态资源配置
     */
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        //registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources");
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
    @Bean(name = "commonsMultipartResolver")
    public CommonsMultipartResolver createMultipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("UTF-8");
        multipartResolver.setMaxUploadSize(20971520);
        multipartResolver.setMaxInMemorySize(20971520);
        return multipartResolver;
    }

    /**
     * 由于web.xml不生效了，需要在这里配置Kaptcha验证码Servlet
     */
    public ServletRegistrationBean servletRegistrationBean() throws ServletException {
        ServletRegistrationBean<KaptchaServlet> servlet = new ServletRegistrationBean<>(new KaptchaServlet(),"/Kaptcha");
        servlet.addInitParameter("kaptcha.border","no");//是否有边框
        servlet.addInitParameter("kaptcha.textproducer.font.color","red");//字体颜色
        servlet.addInitParameter("kaptcha.image.width","135");//图片宽度
        servlet.addInitParameter("kaptcha.textproducer.char.string","AEDXFTBVUJIM7458962");//使用那些字符
        servlet.addInitParameter("kaptcha.image.height","50");//图片高度
        servlet.addInitParameter("kaptcha.textproducer.font.size","43");//字体大小
        servlet.addInitParameter("kaptcha.noise.color","black");//干扰线颜色
        servlet.addInitParameter("kaptcha.textproducer.char.length","4");//字符个数
        servlet.addInitParameter("kaptcha.textproducer.font.names","Arial");//使用那些字体
        return servlet;
    }
}
