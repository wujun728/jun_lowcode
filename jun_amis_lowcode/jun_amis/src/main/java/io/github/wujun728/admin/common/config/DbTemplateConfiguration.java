package io.github.wujun728.admin.common.config;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbTemplateConfiguration {
    @Bean("dbVelocityEngine")
    public VelocityEngine velocityEngine(){
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        // ve.setProperty(Velocity.OUTPUT_ENCODING, charsetStr);
        ve.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, false); // 不使用缓存
        ve.setProperty(Velocity.RESOURCE_LOADER, "str");
        ve.setProperty("str.resource.loader.class", DbStringResourceLoader.class.getName());
        ve.init();
        return ve;
    }
}
