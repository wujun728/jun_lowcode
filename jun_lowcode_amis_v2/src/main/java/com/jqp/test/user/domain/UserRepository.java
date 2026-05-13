package com.jqp.test.user.domain;

import com.jqp.ddd.domain.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户仓储接口
 *
 * @author JQP
 * @date 2026/02/28
 */
public interface UserRepository extends Repository<User, Long> {

    /**
     * 根据用户名查询
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查询
     */
    Optional<User> findByEmail(String email);

    /**
     * 查询激活的用户
     */
    List<User> findActiveUsers();

    /**
     * 根据年龄范围查询
     */
    List<User> findByAgeRange(Integer minAge, Integer maxAge);
}
