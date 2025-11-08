package com.jqp.config;

import cn.hutool.extra.spring.SpringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.ssssssss.magicapi.core.service.MagicAPIService;

import java.util.HashMap;
import java.util.Map;

public class ApiInterceptor implements HandlerInterceptor {

    private String api;
    public ApiInterceptor(String api) {
        this.api = api;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MagicAPIService magicAPIService = SpringUtil.getBean(MagicAPIService.class);
        Map<String,Object> map = new HashMap<>();
        map.put("httpResponse", response);
        return magicAPIService.execute("get", api, map);
//        return false;
//        return true;
    }
}
