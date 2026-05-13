package com.jqp.ddd.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DDD领域模型 - 基础实体
 * 所有聚合根和实体都应该继承此类
 *
 * @author JQP
 * @date 2026/02/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {

    /** 主键ID */
    protected Long id;

    /** 创建时间 */
    protected LocalDateTime createTime;

    /** 更新时间 */
    protected LocalDateTime updateTime;

    /** 是否删除 (0-正常, 1-删除) */
    protected Integer deleted = 0;

    /**
     * 获取实体的唯一标识
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置实体的唯一标识
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 判断是否是新实体（未保存）
     */
    public boolean isNew() {
        return this.id == null;
    }

    /**
     * 判断实体是否已删除
     */
    public boolean isDeleted() {
        return deleted != null && deleted == 1;
    }

    /**
     * 标记实体为删除状态
     */
    public void markDeleted() {
        this.deleted = 1;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 标记实体为正常状态
     */
    public void markNotDeleted() {
        this.deleted = 0;
        this.updateTime = LocalDateTime.now();
    }
}
