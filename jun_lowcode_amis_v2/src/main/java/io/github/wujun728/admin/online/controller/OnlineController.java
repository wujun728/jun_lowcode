package io.github.wujun728.admin.online.controller;

import io.github.wujun728.admin.online.data.OnlineTable;
import io.github.wujun728.admin.online.service.OnlineTableService;
import io.github.wujun728.record.common.PageData;
import io.github.wujun728.record.common.PageParam;
import io.github.wujun728.record.common.Result;
import io.github.wujun728.record.db.service.JdbcService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/onlineTable"})
public class OnlineController {
    @Resource
    private JdbcService jdbcService;

    @Resource
    private OnlineTableService onlineTableService;

    // 查询列表
    @PostMapping("/query")
    public Result<PageData<OnlineTable>> query(@RequestBody PageParam pageParam) {
        // 由于OnlineTableService没有query方法，直接使用jdbcService
        String sql = "select * from online_table where 1=1 ";
        List<Object> values = new ArrayList<>();
        return jdbcService.query(pageParam, OnlineTable.class, sql, values.toArray());
    }

    // 获取详情
    @GetMapping("/get")
    public Result<OnlineTable> get(@RequestParam String id) {
        // 由于OnlineTableService的get方法需要Long类型，这里进行转换
        try {
            Long longId = Long.parseLong(id);
            OnlineTable onlineTable = onlineTableService.get(longId);
            return Result.success(onlineTable);
        } catch (NumberFormatException e) {
            return Result.error("无效的ID格式");
        }
    }

    // 新增或修改
    @PostMapping("/save")
    public Result<OnlineTable> save(@RequestBody OnlineTable onlineTable) {
        onlineTableService.save(onlineTable);
        return Result.success(onlineTable);
    }

    // 删除
    @PostMapping("/del")
    public Result del(@RequestBody Map<String, Object> params) {
        String id = (String) params.get("id");
        try {
            Long longId = Long.parseLong(id);
            OnlineTable onlineTable = new OnlineTable();
            onlineTable.setId(longId);
            onlineTableService.del(onlineTable);
            return Result.success();
        } catch (NumberFormatException e) {
            return Result.error("无效的ID格式");
        }
    }

    // 获取新表单
    @GetMapping("/getNew")
    public Result<OnlineTable> getNew() {
        OnlineTable onlineTable = new OnlineTable();
        // 设置默认值
        onlineTable.setFormLayout("1"); // 默认单列布局
        onlineTable.setVersion(1); // 默认版本号
        return Result.success(onlineTable);
    }

    // 复制
    @PostMapping("/copy")
    public Result<OnlineTable> copy(@RequestBody Map<String, Object> params) {
        String id = (String) params.get("id");
        try {
            Long longId = Long.parseLong(id);
            OnlineTable originalTable = onlineTableService.get(longId);
            if (originalTable == null) {
                return Result.error("记录不存在");
            }

            // 复制主表数据
            OnlineTable newTable = new OnlineTable();
            newTable.setName(originalTable.getName() + "_copy");
            newTable.setComments(originalTable.getComments() + "（复制）");
            newTable.setFormLayout(originalTable.getFormLayout());
            newTable.setTableType(originalTable.getTableType());
            newTable.setVersion(originalTable.getVersion());
            newTable.setStatus(originalTable.getStatus());
            newTable.setTree(originalTable.getTree());
            newTable.setTreePid(originalTable.getTreePid());
            newTable.setTreeLabel(originalTable.getTreeLabel());
            newTable.setColumns(originalTable.getColumns());

            // 保存新记录
            onlineTableService.save(newTable);
            return Result.success(newTable);
        } catch (NumberFormatException e) {
            return Result.error("无效的ID格式");
        }
    }
}