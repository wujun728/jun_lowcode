package com.jqp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.*;

@Configuration
@Slf4j
@Order
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        List<MyInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new MyInterceptor(new String[]{
                "/**"
        },new String[]{
                "/html/login",
                "/admin/jqpUser/login",
                "/amis/**",
                "/rest/**",
                "/bizrest/**",
                "/magic/web/**",
                "/admin/jqpUser/captcha.png",
        },"/sysConfig/adminInterceptor"));

        Map<String,Object> params = new HashMap<>();
        for(MyInterceptor interceptor:interceptors){
            log.info("注册拦截器:{}",interceptor);
            InterceptorRegistration registration = registry.addInterceptor(new ApiInterceptor(interceptor.interceptorApi));
            registration.addPathPatterns(interceptor.path);
            registration.excludePathPatterns(interceptor.excludePath);
        }
    }

    private static final class MyInterceptor{
        String[] path;
        String[] excludePath;
        String interceptorApi;

        public MyInterceptor(String[] path, String[] excludePath, String interceptorApi) {
            this.path = path;
            this.excludePath = excludePath;
            this.interceptorApi = interceptorApi;
        }

        @Override
        public String toString() {
            return "MyInterceptor{" +
                    "path=" + Arrays.toString(path) +
                    ", excludePath=" + Arrays.toString(excludePath) +
                    ", interceptorApi='" + interceptorApi + '\'' +
                    '}';
        }
    }

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .maxAge(3600);
    }
}
