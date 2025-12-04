package io.github.wujun728.admin.rbac.service;

import io.github.wujun728.record.common.Result;

import java.util.Map;

public interface ApiService {
    Result<String> call(String api, Map<String,Object> context);
    Result call(String method,String api, Map<String,Object> context);
}
