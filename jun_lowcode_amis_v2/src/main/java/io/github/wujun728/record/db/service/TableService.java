package io.github.wujun728.record.db.service;

import io.github.wujun728.record.common.PageData;
import io.github.wujun728.record.common.PageParam;
import io.github.wujun728.record.common.Result;
import io.github.wujun728.record.common.service.CacheService;
import io.github.wujun728.record.db.data.TableInfo;

import java.util.Map;

public interface TableService extends CacheService<Result<TableInfo>> {
    Result<PageData<TableInfo>> queryTable(PageParam pageParam);
    Result<TableInfo> tableInfo(String tableName);
    Result<Void> updateTable(TableInfo tableInfo);

    Result dropTable(String tableName);

    Map<String,String> generateCode(String tableName);
}
