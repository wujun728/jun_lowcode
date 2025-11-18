package com.jqp;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class } )
@ComponentScan(value = {"com.jqp","com.ruoyi", "io.github.wujun728","com.jun.plugin"})
//@EnableSpringHttpSession
public class LowCodeAdminV2Application {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeSerializer);
    }

    public static void main(String[] args) {
        SpringApplication.run(LowCodeAdminV2Application.class, args);
    }

}
