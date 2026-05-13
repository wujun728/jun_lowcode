package io.github.wujun728.admin.online.service.impl;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import io.github.wujun728.record.common.BaseData;
import io.github.wujun728.record.db.service.JdbcService;
import io.github.wujun728.record.common.service.impl.AbstractCacheService;
import io.github.wujun728.admin.online.data.OnlineTable;
import io.github.wujun728.admin.online.data.OnlineTableColumn;
import io.github.wujun728.admin.online.service.OnlineTableService;
import io.github.wujun728.record.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service("onlineTableService")
public class OnlineTableServiceImpl extends AbstractCacheService<OnlineTable> implements OnlineTableService {

    @Resource
    private JdbcService jdbcService;

    // 序号比较器
    private static final Comparator<BaseData> SeqComparator = Comparator.comparingInt(data -> {
        Integer sort = (Integer) ReflectUtil.getFieldValue(data, "sort");
        return sort != null ? sort : 0;
    });

    @Override
    public OnlineTable load(String tableName) {
        OnlineTable onlineTable = jdbcService.findOne(OnlineTable.class, "name", tableName);
        this.get(onlineTable);
        return onlineTable;
    }

    @Override
    public void del(OnlineTable onlineTable) {
        if (onlineTable == null) {
            return;
        }
        jdbcService.delete(onlineTable);
        // 删除子表数据
        jdbcService.delete("delete from online_table_column where table_id = ?", onlineTable.getId());
        super.invalid(onlineTable.getName());
    }

    private void get(OnlineTable onlineTable) {
        if (onlineTable == null) {
            return;
        }
        List<OnlineTableColumn> columns = jdbcService.find(OnlineTableColumn.class, "tableId", onlineTable.getId());
        onlineTable.setColumns(columns);
    }

    @Override
    public void save(OnlineTable onlineTable) {
        // 检查是否是新记录（id为null或0）
        boolean isNewRecord = onlineTable.getId() == null || onlineTable.getId() == 0;
        
        OnlineTable oldTable = jdbcService.getById(OnlineTable.class, onlineTable.getId());
        if (oldTable != null && !onlineTable.getName().equals(oldTable.getName())) {
            super.invalid(oldTable.getName());
        }
        oldTable = load(onlineTable.getName());
        
        // 如果是新记录且id为空，生成唯一id
        if (isNewRecord) {
            // 使用时间戳+随机数生成唯一id
            onlineTable.setId(System.currentTimeMillis() + (long) (Math.random() * 1000));
        }
        
        // 保存主表数据：新记录调用insert，已有记录调用update
        if (isNewRecord) {
            jdbcService.insert(onlineTable);
        } else {
            jdbcService.update(onlineTable);
        }

        // 处理子表数据
        List<OnlineTableColumn> columns = onlineTable.getColumns();
        if (columns != null && !columns.isEmpty()) {
            // 排序
            Collections.sort(columns, SeqComparator);
            
            // 更新序号
            for (int i = 0; i < columns.size(); i++) {
                OnlineTableColumn column = columns.get(i);
                column.setSort(i + 1);
                column.setTableId(onlineTable.getId());
            }
            
            // 比较和更新子表数据
            compareAndUpdate(onlineTable, OnlineTableColumn.class, columns, oldTable == null ? null : oldTable.getColumns());
        } else {
            // 如果没有子表数据，删除所有子表记录
            jdbcService.delete("delete from online_table_column where table_id = ?", onlineTable.getId());
        }

        super.invalid(onlineTable.getName());
    }

    private void compareAndUpdate(OnlineTable onlineTable, Class<?> clz, List<?> objects, List<?> oldObjects) {
        boolean change = false;
        if (oldObjects == null || objects.size() != oldObjects.size()) {
            change = true;
        } else {
            for (int i = 0; i < objects.size(); i++) {
                Object o = objects.get(i);
                ReflectUtil.setFieldValue(o, "sort", i + 1);
                ReflectUtil.setFieldValue(o, "tableId", onlineTable.getId());
                Object oldObj = oldObjects.get(i);
                if (!o.equals(oldObj)) {
                    change = true;
                    break;
                }
            }
        }
        
        if (change) {
            // 删除旧数据
            jdbcService.delete(StrUtil.format("delete from {} where table_id = ? ", StringUtil.toTableName(clz.getSimpleName())), onlineTable.getId());
            
            // 保存新数据
            for (int i = 0; i < objects.size(); i++) {
                Object o = objects.get(i);
                ReflectUtil.setFieldValue(o, "sort", i + 1);
                ReflectUtil.setFieldValue(o, "tableId", onlineTable.getId());
                ReflectUtil.setFieldValue(o, "id", null);
                
                jdbcService.saveOrUpdate((BaseData) o);
            }
        }
    }

    @Override
    public OnlineTable get(Long id) {
        OnlineTable onlineTable = jdbcService.getById(OnlineTable.class, id);
        this.get(onlineTable);
        return onlineTable;
    }
}