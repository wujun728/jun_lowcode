package io.github.wujun728.admin.online.service;

import io.github.wujun728.record.common.service.CacheService;
import io.github.wujun728.admin.online.data.OnlineTable;

public interface OnlineTableService extends CacheService<OnlineTable> {
    void save(OnlineTable onlineTable);
    OnlineTable get(Long id);
    OnlineTable load(String tableName);
    void del(OnlineTable onlineTable);
}