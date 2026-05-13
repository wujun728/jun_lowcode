package com.jqp.example.dict.domain;

import com.jqp.ddd.domain.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DDD领域层 - 字典仓储接口
 * 定义字典聚合根的持久化契约
 *
 * @author JQP
 * @date 2026/02/28
 */
public interface DictRepository extends Repository<Dict, Long> {

    /**
     * 根据编码查询字典
     */
    Optional<Dict> findByCode(String code);

    /**
     * 根据分类查询字典列表
     */
    List<Dict> findByCategory(String category);

    /**
     * 根据分类查询启用的字典列表
     */
    List<Dict> findEnabledByCategory(String category);

    /**
     * 判断编码是否重复
     */
    boolean isCodeDuplicate(String code, Long excludeId);
}
