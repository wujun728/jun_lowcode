package com.jqp.test.user.domain;

import com.jqp.ddd.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户聚合根
 * 简单测试模块
 *
 * @author JQP
 * @date 2026/02/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    /** 用户名 */
    private String username;

    /** 邮箱 */
    private String email;

    /** 状态：0-禁用，1-启用 */
    private Integer status;

    /** 年龄 */
    private Integer age;

    // ============ 领域方法 ============

    /**
     * 激活用户
     */
    public void activate() {
        this.status = 1;
    }

    /**
     * 禁用用户
     */
    public void deactivate() {
        this.status = 0;
    }

    /**
     * 判断是否激活
     */
    public boolean isActive() {
        return status != null && status == 1;
    }

    /**
     * 更新用户信息
     */
    public void updateInfo(String username, String email, Integer age) {
        this.username = username;
        this.email = email;
        this.age = age;
    }
}
