package com.jqp.test.user.presentation;

import com.jqp.ddd.presentation.BaseRestController;
import com.jqp.ddd.presentation.Response;
import com.jqp.test.user.application.UserApplicationService;
import com.jqp.test.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 用户REST接口
 *
 * @author JQP
 * @date 2026/02/28
 */
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserRestController extends BaseRestController<User, UserApplicationService> {

    @Resource
    private UserApplicationService userApplicationService;

    @Override
    protected UserApplicationService getApplicationService() {
        return userApplicationService;
    }

    /**
     * 根据用户名查询
     * GET /api/users/by-username/{username}
     */
    @GetMapping("/by-username/{username}")
    public Response<User> getByUsername(@PathVariable String username) {
        log.info("根据用户名查询用户，username={}", username);
        try {
            User user = userApplicationService.findByUsername(username).orElse(null);
            if (user == null) {
                return Response.fail("用户不存在");
            }
            return Response.ok(user);
        } catch (Exception e) {
            log.error("查询用户失败", e);
            return Response.fail("查询失败");
        }
    }

    /**
     * 根据邮箱查询
     * GET /api/users/by-email/{email}
     */
    @GetMapping("/by-email/{email}")
    public Response<User> getByEmail(@PathVariable String email) {
        log.info("根据邮箱查询用户，email={}", email);
        try {
            User user = userApplicationService.findByEmail(email).orElse(null);
            if (user == null) {
                return Response.fail("用户不存在");
            }
            return Response.ok(user);
        } catch (Exception e) {
            log.error("查询用户失败", e);
            return Response.fail("查询失败");
        }
    }

    /**
     * 获取所有激活用户
     * GET /api/users/active
     */
    @GetMapping("/active")
    public Response<List<User>> getActiveUsers() {
        log.info("获取所有激活用户");
        try {
            List<User> users = userApplicationService.getActiveUsers();
            return Response.ok(users);
        } catch (Exception e) {
            log.error("查询激活用户失败", e);
            return Response.fail("查询失败");
        }
    }

    /**
     * 按年龄范围查询
     * POST /api/users/by-age
     */
    @PostMapping("/by-age")
    public Response<List<User>> getUsersByAge(@RequestBody Map<String, Object> params) {
        log.info("按年龄范围查询用户，params={}", params);
        try {
            Integer minAge = ((Number) params.get("minAge")).intValue();
            Integer maxAge = ((Number) params.get("maxAge")).intValue();
            List<User> users = userApplicationService.getUsersByAge(minAge, maxAge);
            return Response.ok(users);
        } catch (Exception e) {
            log.error("按年龄查询失败", e);
            return Response.fail("查询失败");
        }
    }

    /**
     * 注册用户
     * POST /api/users/register
     */
    @PostMapping("/register")
    public Response<User> register(@RequestBody User user) {
        log.info("注册新用户，username={}", user.getUsername());
        try {
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                return Response.fail("用户名不能为空");
            }
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                return Response.fail("邮箱不能为空");
            }
            User newUser = userApplicationService.registerUser(user);
            return Response.ok(newUser, "注册成功");
        } catch (IllegalArgumentException e) {
            log.warn("注册失败：{}", e.getMessage());
            return Response.fail(e.getMessage());
        } catch (Exception e) {
            log.error("注册用户失败", e);
            return Response.fail("注册失败");
        }
    }

    /**
     * 激活用户
     * POST /api/users/{id}/activate
     */
    @PostMapping("/{id}/activate")
    public Response<Void> activate(@PathVariable Long id) {
        log.info("激活用户，id={}", id);
        try {
            userApplicationService.activateUser(id);
            return Response.ok(null, "激活成功");
        } catch (IllegalArgumentException e) {
            return Response.fail(e.getMessage());
        } catch (Exception e) {
            log.error("激活用户失败", e);
            return Response.fail("激活失败");
        }
    }

    /**
     * 禁用用户
     * POST /api/users/{id}/deactivate
     */
    @PostMapping("/{id}/deactivate")
    public Response<Void> deactivate(@PathVariable Long id) {
        log.info("禁用用户，id={}", id);
        try {
            userApplicationService.deactivateUser(id);
            return Response.ok(null, "禁用成功");
        } catch (IllegalArgumentException e) {
            return Response.fail(e.getMessage());
        } catch (Exception e) {
            log.error("禁用用户失败", e);
            return Response.fail("禁用失败");
        }
    }
}
