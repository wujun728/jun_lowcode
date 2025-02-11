
package io.github.wujun728.sys.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.wujun728.sys.core.mybatis.dbid.SnowyDatabaseIdProvider;
import io.github.wujun728.sys.core.mybatis.fieldfill.CustomMetaObjectHandler;
import io.github.wujun728.sys.core.mybatis.sqlfilter.DemoProfileSqlInterceptor;

/**
 * mybatis扩展插件配置
 * @date 2020/3/18 10:49
 */
@Configuration
@MapperScan(basePackages = {"io.github.wujun728.**.mapper"})
public class MybatisConfig {

    /**
     * mybatis-plus分页插件
     *
     * @author xuyuxiang
     * @date 2020/3/31 15:42
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MARIADB));
        return interceptor;
    }

    /**
     * 演示环境的sql拦截器
     * <p>
     * 演示环境只开放查询操作，其他都不允许
     *
     *
     * @date 2020/5/5 12:24
     */
    @Bean
    public DemoProfileSqlInterceptor demoProfileSqlInterceptor() {
        return new DemoProfileSqlInterceptor();
    }

    /**
     * 自定义公共字段自动注入
     *
     * @author xuyuxiang
     * @date 2020/3/31 15:42
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new CustomMetaObjectHandler();
    }

    /**
     * 数据库id选择器
     *
     *
     * @date 2020/6/20 21:23
     */
    @Bean
    public SnowyDatabaseIdProvider snowyDatabaseIdProvider() {
        return new SnowyDatabaseIdProvider();
    }

}
