
package io.github.wujun728.sys.config;

import com.ibeetl.starter.BeetlTemplateCustomize;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.wujun728.core.context.constant.ConstantContextHolder;
import io.github.wujun728.core.context.login.LoginContextHolder;

/**
 * Beetl模板引擎的自定义配置
 * @date 2020/7/9 11:43
 */
@Configuration
public class CustomBeetlConfig {

    /**
     * 自定义配置，注册功能包，前端使用
     *
     * @author xuyuxiang
     * @date 2021/4/8 12:30
     */
    @Bean
    public BeetlTemplateCustomize beetlTemplateCustomize(){
        return groupTemplate -> {
            //登陆上下文
            groupTemplate.registerFunctionPackage("loginContext", new LoginContextHolder().me());
            //常量上下文
            groupTemplate.registerFunctionPackage("constantContext", new ConstantContextHolder());
        };
    }

}
