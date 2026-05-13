package com.jqp.example.dict.domain;

import com.jqp.ddd.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DDD领域模型 - 字典聚合根
 * 代表一个字典的业务概念
 *
 * @author JQP
 * @date 2026/02/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Dict extends BaseEntity {

    /** 字典编码（唯一） */
    private String code;

    /** 字典名称 */
    private String name;

    /** 字典分类 */
    private String category;

    /** 字典值 */
    private String value;

    /** 字典描述 */
    private String description;

    /** 状态：0-禁用，1-启用 */
    private Integer status;

    /** 排序号 */
    private Integer orderNum;

    // ============ 领域方法 ============

    /**
     * 禁用字典
     */
    public void disable() {
        this.status = 0;
    }

    /**
     * 启用字典
     */
    public void enable() {
        this.status = 1;
    }

    /**
     * 判断是否启用
     */
    public boolean isEnabled() {
        return status != null && status == 1;
    }

    /**
     * 更新字典信息
     */
    public void updateInfo(String name, String value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }
}
