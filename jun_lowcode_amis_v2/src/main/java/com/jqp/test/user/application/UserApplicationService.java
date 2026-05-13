package com.jqp.test.user.application;

import com.jqp.ddd.application.BaseApplicationService;
import com.jqp.test.user.domain.User;
import com.jqp.test.user.domain.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 用户应用服务
 *
 * @author JQP
 * @date 2026/02/28
 */
@Service
@Slf4j
public class UserApplicationService extends BaseApplicationService<User, Long, UserRepository> {

    @Resource
    private UserRepository userRepository;

    @Override
    protected UserRepository getRepository() {
        return userRepository;
    }

    /**
     * 根据用户名查询用户
     */
    public Optional<User> findByUsername(String username) {
        log.debug("根据用户名查询用户，username={}", username);
        return userRepository.findByUsername(username);
    }

    /**
     * 根据邮箱查询用户
     */
    public Optional<User> findByEmail(String email) {
        log.debug("根据邮箱查询用户，email={}", email);
        return userRepository.findByEmail(email);
    }

    /**
     * 获取所有激活的用户
     */
    public List<User> getActiveUsers() {
        log.debug("获取所有激活的用户");
        return userRepository.findActiveUsers();
    }

    /**
     * 根据年龄范围查询用户
     */
    public List<User> getUsersByAge(Integer minAge, Integer maxAge) {
        log.debug("根据年龄范围查询用户，minAge={}，maxAge={}", minAge, maxAge);
        return userRepository.findByAgeRange(minAge, maxAge);
    }

    /**
     * 创建用户（带验证）
     */
    public User registerUser(User user) {
        // 验证用户名是否已存在
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("用户名已存在：" + user.getUsername());
        }

        // 验证邮箱是否已存在
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("邮箱已存在：" + user.getEmail());
        }

        log.info("注册新用户，username={}", user.getUsername());
        user.activate();  // 新用户默认激活
        return create(user);
    }

    /**
     * 激活用户
     */
    public void activateUser(Long userId) {
        User user = queryById(userId).orElseThrow(() ->
            new IllegalArgumentException("用户不存在，id=" + userId));

        user.activate();
        modify(user);
        log.info("激活用户，id={}", userId);
    }

    /**
     * 禁用用户
     */
    public void deactivateUser(Long userId) {
        User user = queryById(userId).orElseThrow(() ->
            new IllegalArgumentException("用户不存在，id=" + userId));

        user.deactivate();
        modify(user);
        log.info("禁用用户，id={}", userId);
    }
}
