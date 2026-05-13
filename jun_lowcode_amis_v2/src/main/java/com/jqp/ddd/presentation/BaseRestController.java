package com.jqp.ddd.presentation;

import com.jqp.ddd.application.BaseApplicationService;
import com.jqp.ddd.domain.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * DDD表示层 - 基础REST控制器
 * 业务模块只需继承此类即可获得完整的REST API
 *
 * @param <T> 实体类型
 * @param <S> 应用服务类型
 * @author JQP
 * @date 2026/02/28
 */
@Slf4j
public abstract class BaseRestController<T extends BaseEntity, S extends BaseApplicationService> {

    /**
     * 获取应用服务实例
     * 子类必须实现此方法
     */
    protected abstract S getApplicationService();

    /**
     * 查询所有
     * GET /
     */
    @GetMapping
    public Response<List<T>> list() {
        log.info("查询所有数据");
        try {
            List<T> data = getApplicationService().queryAll();
            return Response.ok(data);
        } catch (Exception e) {
            log.error("查询失败", e);
            return Response.fail("查询失败");
        }
    }

    /**
     * 根据ID查询
     * GET /{id}
     */
    @GetMapping("/{id}")
    public Response<T> getById(@PathVariable Long id) {
        log.info("根据ID查询，id={}", id);
        try {
            T data = (T) getApplicationService().getById(id);
            if (data == null) {
                return Response.fail("数据不存在");
            }
            return Response.ok(data);
        } catch (Exception e) {
            log.error("查询失败", e);
            return Response.fail("查询失败");
        }
    }

    /**
     * 分页查询
     * POST /page
     */
    @PostMapping("/page")
    public Response<Map<String, Object>> page(@RequestBody Map<String, Object> params) {
        log.info("分页查询，params={}", params);
        try {
            int pageNum = (int) params.getOrDefault("pageNum", 1);
            int pageSize = (int) params.getOrDefault("pageSize", 10);
            Map<String, Object> data = getApplicationService().pageQuery(pageNum, pageSize);
            return Response.ok(data);
        } catch (Exception e) {
            log.error("分页查询失败", e);
            return Response.fail("分页查询失败");
        }
    }

    /**
     * 新增
     * POST /
     */
    @PostMapping
    public Response<T> create(@RequestBody T entity) {
        log.info("新增数据");
        try {
            T data = (T) getApplicationService().create(entity);
            return Response.ok(data, "新增成功");
        } catch (Exception e) {
            log.error("新增失败", e);
            return Response.fail("新增失败");
        }
    }

    /**
     * 修改
     * PUT /
     */
    @PutMapping
    public Response<T> modify(@RequestBody T entity) {
        log.info("修改数据，id={}", entity.getId());
        try {
            if (entity.getId() == null) {
                return Response.fail("ID不能为空");
            }
            T data = (T) getApplicationService().modify(entity);
            return Response.ok(data, "修改成功");
        } catch (Exception e) {
            log.error("修改失败", e);
            return Response.fail("修改失败");
        }
    }

    /**
     * 删除
     * DELETE /{id}
     */
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        log.info("删除数据，id={}", id);
        try {
            getApplicationService().delete(id);
            return Response.ok(null, "删除成功");
        } catch (Exception e) {
            log.error("删除失败", e);
            return Response.fail("删除失败");
        }
    }

    /**
     * 批量删除
     * POST /delete-batch
     */
    @PostMapping("/delete-batch")
    public Response<Void> deleteBatch(@RequestBody Map<String, Object> params) {
        log.info("批量删除数据");
        try {
            List<Long> ids = (List<Long>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return Response.fail("ID列表不能为空");
            }
            getApplicationService().deleteBatch(ids);
            return Response.ok(null, "批量删除成功");
        } catch (Exception e) {
            log.error("批量删除失败", e);
            return Response.fail("批量删除失败");
        }
    }
}
