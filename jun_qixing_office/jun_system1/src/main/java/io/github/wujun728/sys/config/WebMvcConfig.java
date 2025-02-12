
package io.github.wujun728.sys.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.github.wujun728.sys.core.error.QixingErrorAttributes;
import io.github.wujun728.sys.core.error.QixingErrorView;
import io.github.wujun728.sys.core.filter.RequestNoFilter;
import io.github.wujun728.sys.core.validator.QixingValidator;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import io.github.wujun728.core.web.QixingRequestResponseBodyMethodProcessor;
import io.github.wujun728.sys.core.filter.xss.XssFilter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * web配置
 *
 * @date 2020/4/11 10:23
 */
@Configuration
@Import({cn.hutool.extra.spring.SpringUtil.class})
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 错误信息提示重写
     *
     *
     * @date 2020/4/14 22:27
     */
    @Bean
    public QixingErrorAttributes qixingErrorAttributes() {
        return new QixingErrorAttributes();
    }

    /**
     * 默认错误页面
     *
     *
     * @date 2020/4/14 22:27
     */
    @Bean("error")
    public QixingErrorView qixingErrorView() {
        return new QixingErrorView();
    }

    /**
     * 静态资源映射
     *
     *
     * @date 2020/4/11 10:23
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //swagger增强的静态资源映射
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        //flowable设计器静态资源映射
        registry.addResourceHandler("/designer/**").addResourceLocations("classpath:/designer/");
        //前端静态资源映射
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/assets/");
    }

    /**
     * xss过滤器
     *
     *
     * @date 2020/6/21 10:30
     */
    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterFilterRegistrationBean() {
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>(new XssFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }

    /**
     * 请求唯一编号生成器
     *
     *
     * @date 2020/6/21 10:30
     */
    @Bean
    public FilterRegistrationBean<RequestNoFilter> requestNoFilterFilterRegistrationBean() {
        FilterRegistrationBean<RequestNoFilter> registration = new FilterRegistrationBean<>(new RequestNoFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }

    /**
     * json自定义序列化工具,long转string
     *
     *
     * @date 2020/5/28 14:48
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder ->
                jacksonObjectMapperBuilder
                        .serializerByType(Long.class, ToStringSerializer.instance)
                        .serializerByType(Long.TYPE, ToStringSerializer.instance);
    }

    /**
     * 自定义的spring参数校验器，重写主要为了保存一些在自定义validator中读不到的属性
     *
     * @author xuyuxiang
     * @date 2020/8/12 20:18
     */
    @Bean
    public QixingValidator qixingValidator() {
        return new QixingValidator();
    }


    /**
     * 自定义的QixingRequestResponseBodyMethodProcessor，放在所有resolvers之前
     *
     * @author xuyuxiang
     * @date 2020/8/21 21:09
     */
    @Configuration
    public static class MethodArgumentResolver {

        @Resource
        private RequestMappingHandlerAdapter adapter;

        @PostConstruct
        public void injectSelfMethodArgumentResolver() {
            List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();
            argumentResolvers.add(new QixingRequestResponseBodyMethodProcessor(adapter.getMessageConverters()));
            argumentResolvers.addAll(Objects.requireNonNull(adapter.getArgumentResolvers()));
            adapter.setArgumentResolvers(argumentResolvers);
        }
    }

}
