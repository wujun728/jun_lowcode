package io.github.wujun728.record.common.service;

import java.util.Map;

public interface DbCacheService {
    Map<String,Object> getData(String tableName,String key);
}
