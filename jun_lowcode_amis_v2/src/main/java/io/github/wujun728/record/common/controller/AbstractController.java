package io.github.wujun728.record.common.controller;

import io.github.wujun728.record.common.BaseData;
import io.github.wujun728.record.common.PageData;
import io.github.wujun728.record.common.PageParam;
import io.github.wujun728.record.common.Result;
import io.github.wujun728.record.common.service.AbstractService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 通用控制器抽象类
 * 所有控制器类只需继承此类即可获得基本的CRUD API功能
 * @param <T> 实体类类型
 */
public abstract class AbstractController<T extends BaseData> {

    /**
     * 获取业务服务对象
     * @return 业务服务对象
     */
    protected abstract AbstractService<T> getService();

    /**
     * 新增数据
     * @param entity 实体对象
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody T entity) {
        try {
            getService().insert(entity);
            return Result.success(true);
        } catch (Exception e) {
            return Result.error("新增失败：" + e.getMessage());
        }
    }

    /**
     * 修改数据
     * @param entity 实体对象
     * @return 操作结果
     */
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody T entity) {
        try {
            getService().update(entity);
            return Result.success(true);
        } catch (Exception e) {
            return Result.error("修改失败：" + e.getMessage());
        }
    }

    /**
     * 保存或更新数据
     * @param entity 实体对象
     * @return 操作结果
     */
    @PostMapping("/saveOrUpdate")
    public Result<Boolean> saveOrUpdate(@RequestBody T entity) {
        try {
            getService().saveOrUpdate(entity);
            return Result.success(true);
        } catch (Exception e) {
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID删除数据
     * @param id 数据ID
     * @return 操作结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        try {
            getService().delete(id);
            return Result.success(true);
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询数据
     * @param id 数据ID
     * @return 实体对象
     */
    @GetMapping("/get/{id}")
    public Result<T> getById(@PathVariable Long id) {
        try {
            T entity = getService().getById(id);
            return Result.success(entity);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询所有数据
     * @return 数据列表
     */
    @GetMapping("/list")
    public Result<List<T>> findAll() {
        try {
            List<T> list = getService().findAll();
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询数据
     * @param pageParam 分页参数
     * @param sql SQL语句
     * @param args SQL参数
     * @return 分页数据
     */
    @PostMapping("/page")
    public Result<PageData<T>> query(@RequestBody PageParam pageParam, @RequestParam(required = false) String sql, @RequestParam(required = false) Object... args) {
        try {
            Result<PageData<T>> result = getService().query(pageParam, sql, args);
            return result;
        } catch (Exception e) {
            return Result.error("分页查询失败：" + e.getMessage());
        }
    }

    /**
     * 批量新增数据
     * @param entities 实体对象列表
     * @return 操作结果
     */
    @PostMapping("/batch/add")
    public Result<Boolean> batchInsert(@RequestBody List<T> entities) {
        try {
            getService().batchInsert(entities);
            return Result.success(true);
        } catch (Exception e) {
            return Result.error("批量新增失败：" + e.getMessage());
        }
    }

    /**
     * 批量修改数据
     * @param entities 实体对象列表
     * @return 操作结果
     */
    @PutMapping("/batch/update")
    public Result<Boolean> batchUpdate(@RequestBody List<T> entities) {
        try {
            getService().batchUpdate(entities);
            return Result.success(true);
        } catch (Exception e) {
            return Result.error("批量修改失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除数据
     * @param ids ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch/delete")
    public Result<Boolean> batchDelete(@RequestBody List<Long> ids) {
        try {
            getService().batchDelete(ids);
            return Result.success(true);
        } catch (Exception e) {
            return Result.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 自定义查询
     * @param sql SQL语句
     * @param args SQL参数
     * @return 数据列表
     */
    @PostMapping("/custom/query")
    public Result<List<T>> customQuery(@RequestParam String sql, @RequestParam(required = false) Object... args) {
        try {
            List<T> list = getService().customQuery(sql, args);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("自定义查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 导入数据
     * @param file 上传的文件
     * @return 操作结果
     */
    @PostMapping("/import")
    public Result<Boolean> importData(@RequestParam("file") MultipartFile file) {
        try {
            Consumer<List<T>> dataHandler = (dataList) -> {
                // 这里可以添加数据验证和处理逻辑
            };
            return getService().importData(file.getInputStream(), dataHandler);
        } catch (Exception e) {
            return Result.error("导入失败：" + e.getMessage());
        }
    }
    
    /**
     * 导出数据
     * @param response HTTP响应
     * @param sql SQL语句
     * @param args SQL参数
     */
    @GetMapping("/export")
    public void exportData(HttpServletResponse response, @RequestParam(required = false) String sql, @RequestParam(required = false) Object... args) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=data.xlsx");
            getService().exportData(response.getOutputStream(), sql, args);
        } catch (Exception e) {
            try {
                response.setContentType("application/json");
                response.getWriter().write(Result.error("导出失败：" + e.getMessage()).toString());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    
    /**
     * 导出所有数据
     * @param response HTTP响应
     */
    @GetMapping("/export/all")
    public void exportAllData(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=data.xlsx");
            getService().exportAllData(response.getOutputStream());
        } catch (Exception e) {
            try {
                response.setContentType("application/json");
                response.getWriter().write(Result.error("导出失败：" + e.getMessage()).toString());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    
    /**
     * 下载数据
     * @param response HTTP响应
     * @param sql SQL语句
     * @param args SQL参数
     */
    @GetMapping("/download")
    public void downloadData(HttpServletResponse response, @RequestParam(required = false) String sql, @RequestParam(required = false) Object... args) {
        exportData(response, sql, args);
    }
    
    /**
     * 下载所有数据
     * @param response HTTP响应
     */
    @GetMapping("/download/all")
    public void downloadAllData(HttpServletResponse response) {
        exportAllData(response);
    }
}
