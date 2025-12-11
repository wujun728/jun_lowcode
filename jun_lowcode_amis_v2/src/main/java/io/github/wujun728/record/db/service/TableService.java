package io.github.wujun728.record.db.service;

import io.github.wujun728.record.common.PageData;
import io.github.wujun728.record.common.PageParam;
import io.github.wujun728.record.common.Result;
import io.github.wujun728.record.common.service.CacheService;
import io.github.wujun728.record.db.data.ClassInfo;

import java.util.Map;

public interface TableService extends CacheService<Result<ClassInfo>> {
    Result<PageData<ClassInfo>> queryTable(PageParam pageParam);
    Result<ClassInfo> tableInfo(String tableName);
    Result<Void> updateTable(ClassInfo tableInfo);

    Result dropTable(String tableName);

    Map<String,String> generateCode(String tableName);
}
