package io.github.wujun728.sql;

import io.github.wujun728.sql.entity.DsConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DsConfig.class)
public class ApiEngineConfiguration {

    private final DsConfig dbConfig;

    public ApiEngineConfiguration(DsConfig config) {
        this.dbConfig = config;
    }

    @Bean
    @ConditionalOnMissingBean(ApiEngine.class)
    public ApiEngine Engine() {
        return new ApiEngine(dbConfig);
    }
}