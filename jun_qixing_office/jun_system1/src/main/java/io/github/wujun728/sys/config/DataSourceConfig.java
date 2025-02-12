
package io.github.wujun728.sys.config;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.wujun728.core.context.constant.ConstantContextHolder;
import io.github.wujun728.core.pojo.druid.DruidProperties;

import java.util.HashMap;

/**
 * Druid配置
 *
 * @date 2017/5/20 21:58
 */
@Configuration
public class DataSourceConfig {

    /**
     * druid属性配置
     *
     * @author xuyuxiang
     * @date 2020/8/25
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidProperties druidProperties() {
        return new DruidProperties();
    }

    /**
     * druid数据库连接池
     *
     * @author xuyuxiang
     * @date 2020/8/25
     */
    @Bean(initMethod = "init")
    public DruidDataSource dataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = new DruidDataSource();
        druidProperties.config(dataSource);
        return dataSource;
    }

    /**
     * druid监控，配置StatViewServlet
     *
     * @author xuyuxiang
     * @date 2020/6/28 16:03
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> druidServletRegistration() {

        // 设置servlet的参数
        HashMap<String, String> statViewServletParams = CollectionUtil.newHashMap();
        statViewServletParams.put("resetEnable", "true");
        ServletRegistrationBean<StatViewServlet> registration = new ServletRegistrationBean<>(new StatViewServlet());
        registration.addUrlMappings("/druid/*");
        statViewServletParams.put("loginUsername", ConstantContextHolder.getDruidLoginConfigs().getLoginUsername());
        statViewServletParams.put("loginPassword", ConstantContextHolder.getDruidLoginConfigs().getLoginPassword());
        registration.setInitParameters(statViewServletParams);
        return registration;
    }

}
