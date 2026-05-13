package com.jqp.example.dict.presentation;

import com.jqp.ddd.presentation.BaseRestController;
import com.jqp.ddd.presentation.Response;
import com.jqp.example.dict.application.DictApplicationService;
import com.jqp.example.dict.domain.Dict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * DDD表示层 - 字典REST控制器
 * 提供字典的REST接口
 * 基础接口从BaseRestController自动继承
 *
 * @author JQP
 * @date 2026/02/28
 */
@RestController
@RequestMapping("/api/dict")
@Slf4j
public class DictRestController extends BaseRestController<Dict, DictApplicationService> {

    @Resource
    private DictApplicationService dictApplicationService;

    @Override
    protected DictApplicationService getApplicationService() {
        return dictApplicationService;
    }

    /**
     * 根据编码查询字典
     * GET /api/dict/code/{code}
     */
    @GetMapping("/code/{code}")
    public Response<Dict> getByCode(@PathVariable String code) {
        log.info("根据编码查询字典，code={}", code);
        try {
            Dict dict = dictApplicationService.queryByCode(code).orElse(null);
            if (dict == null) {
                return Response.fail("字典不存在");
            }
            return Response.ok(dict);
        } catch (Exception e) {
            log.error("查询字典失败", e);
            return Response.fail("查询失败");
        }
    }

    /**
     * 根据分类查询字典列表
     * GET /api/dict/category/{category}
     */
    @GetMapping("/category/{category}")
    public Response<List<Dict>> getByCategory(@PathVariable String category) {
        log.info("根据分类查询字典，category={}", category);
        try {
            List<Dict> dictList = dictApplicationService.queryByCategory(category);
            return Response.ok(dictList);
        } catch (Exception e) {
            log.error("查询字典列表失败", e);
            return Response.fail("查询失败");
        }
    }

    /**
     * 根据分类查询启用的字典列表
     * GET /api/dict/category-enabled/{category}
     */
    @GetMapping("/category-enabled/{category}")
    public Response<List<Dict>> getEnabledByCategory(@PathVariable String category) {
        log.info("根据分类查询启用的字典，category={}", category);
        try {
            List<Dict> dictList = dictApplicationService.queryEnabledByCategory(category);
            return Response.ok(dictList);
        } catch (Exception e) {
            log.error("查询启用字典列表失败", e);
            return Response.fail("查询失败");
        }
    }

    /**
     * 创建字典（覆盖父类方法，添加验证）
     * POST /api/dict
     */
    @PostMapping
    public Response<Dict> create(@RequestBody Dict entity) {
        log.info("创建字典，code={}", entity.getCode());
        try {
            Dict dict = dictApplicationService.createDict(entity);
            return Response.ok(dict, "创建成功");
        } catch (IllegalArgumentException e) {
            log.warn("创建字典验证失败，{}", e.getMessage());
            return Response.fail(e.getMessage());
        } catch (Exception e) {
            log.error("创建字典失败", e);
            return Response.fail("创建失败");
        }
    }

    /**
     * 修改字典（覆盖父类方法，添加验证）
     * PUT /api/dict
     */
    @PutMapping
    public Response<Dict> modify(@RequestBody Dict entity) {
        log.info("修改字典，id={}", entity.getId());
        try {
            if (entity.getId() == null) {
                return Response.fail("ID不能为空");
            }
            Dict dict = dictApplicationService.modifyDict(entity);
            return Response.ok(dict, "修改成功");
        } catch (IllegalArgumentException e) {
            log.warn("修改字典验证失败，{}", e.getMessage());
            return Response.fail(e.getMessage());
        } catch (Exception e) {
            log.error("修改字典失败", e);
            return Response.fail("修改失败");
        }
    }

    /**
     * 启用字典
     * POST /api/dict/{id}/enable
     */
    @PostMapping("/{id}/enable")
    public Response<Void> enable(@PathVariable Long id) {
        log.info("启用字典，id={}", id);
        try {
            dictApplicationService.enableDict(id);
            return Response.ok(null, "启用成功");
        } catch (IllegalArgumentException e) {
            return Response.fail(e.getMessage());
        } catch (Exception e) {
            log.error("启用字典失败", e);
            return Response.fail("启用失败");
        }
    }

    /**
     * 禁用字典
     * POST /api/dict/{id}/disable
     */
    @PostMapping("/{id}/disable")
    public Response<Void> disable(@PathVariable Long id) {
        log.info("禁用字典，id={}", id);
        try {
            dictApplicationService.disableDict(id);
            return Response.ok(null, "禁用成功");
        } catch (IllegalArgumentException e) {
            return Response.fail(e.getMessage());
        } catch (Exception e) {
            log.error("禁用字典失败", e);
            return Response.fail("禁用失败");
        }
    }

    /**
     * 检查编码唯一性
     * POST /api/dict/check-code
     */
    @PostMapping("/check-code")
    public Response<Map<String, Boolean>> checkCode(@RequestBody Map<String, Object> params) {
        log.info("检查字典编码唯一性，params={}", params);
        try {
            String code = (String) params.get("code");
            Long excludeId = params.get("excludeId") != null ? ((Number) params.get("excludeId")).longValue() : null;

            if (code == null || code.isEmpty()) {
                return Response.fail("编码不能为空");
            }

            boolean duplicate = dictApplicationService.getRepository().isCodeDuplicate(code, excludeId);
            return Response.ok(java.util.Map.of("duplicate", duplicate));
        } catch (Exception e) {
            log.error("检查编码唯一性失败", e);
            return Response.fail("检查失败");
        }
    }
}
