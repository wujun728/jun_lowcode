package com.jqp.example.dict.infrastructure;

import com.jqp.ddd.infrastructure.jdbc.SimpleJdbcRepository;
import com.jqp.example.dict.domain.Dict;
import com.jqp.example.dict.domain.DictRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * DDD基础设施层 - 字典仓储实现
 * 使用JDBC实现字典的持久化
 * 支持Spring和非Spring环境
 *
 * @author JQP
 * @date 2026/02/28
 */
@Repository
public class DictRepositoryImpl extends SimpleJdbcRepository<Dict, Long> implements DictRepository {

    /**
     * 根据编码查询字典
     */
    @Override
    public Optional<Dict> findByCode(String code) {
        return findOneByWhere("code = ? and deleted = 0", code);
    }

    /**
     * 根据分类查询字典列表
     */
    @Override
    public List<Dict> findByCategory(String category) {
        return findByWhere("category = ? and deleted = 0 order by order_num asc", category);
    }

    /**
     * 根据分类查询启用的字典列表
     */
    @Override
    public List<Dict> findEnabledByCategory(String category) {
        return findByWhere("category = ? and status = 1 and deleted = 0 order by order_num asc", category);
    }

    /**
     * 判断编码是否重复
     */
    @Override
    public boolean isCodeDuplicate(String code, Long excludeId) {
        String where;
        if (excludeId != null) {
            where = "code = ? and id <> ? and deleted = 0";
            List<Dict> results = findByWhere(where, code, excludeId);
            return !results.isEmpty();
        } else {
            where = "code = ? and deleted = 0";
            List<Dict> results = findByWhere(where, code);
            return !results.isEmpty();
        }
    }
}
