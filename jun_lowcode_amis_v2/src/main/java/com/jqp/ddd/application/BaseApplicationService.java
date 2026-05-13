package com.jqp.ddd.application;

import com.jqp.ddd.domain.BaseEntity;
import com.jqp.ddd.domain.Repository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * DDD应用层 - 基础应用服务
 * 业务模块只需继承此类即可完成所有单表操作
 * 支持Spring和非Spring环境
 *
 * @param <T> 聚合根类型
 * @param <ID> 主键类型
 * @param <R> 仓储类型
 * @author JQP
 * @date 2026/02/28
 */
@Slf4j
public abstract class BaseApplicationService<T extends BaseEntity, ID, R extends Repository<T, ID>> {

    /**
     * 获取仓储实例
     * 子类必须实现此方法
     */
    protected abstract R getRepository();

    /**
     * 查询所有
     */
    public List<T> queryAll() {
        log.debug("查询所有数据，实体={}", getRepository().getEntityClass().getSimpleName());
        return getRepository().findAll();
    }

    /**
     * 根据ID查询
     */
    public Optional<T> queryById(ID id) {
        log.debug("根据ID查询，id={}", id);
        return getRepository().findById(id);
    }

    /**
     * 根据ID查询（返回实体或null）
     */
    public T getById(ID id) {
        return queryById(id).orElse(null);
    }

    /**
     * 分页查询
     */
    public Map<String, Object> pageQuery(int pageNum, int pageSize) {
        log.debug("分页查询，pageNum={}，pageSize={}", pageNum, pageSize);
        return getRepository().findPage(pageNum, pageSize);
    }

    /**
     * 条件分页查询
     */
    public Map<String, Object> pageQuery(int pageNum, int pageSize, String where, Object... args) {
        log.debug("条件分页查询，pageNum={}，pageSize={}，where={}", pageNum, pageSize, where);
        return getRepository().findPage(pageNum, pageSize, where, args);
    }

    /**
     * 条件查询列表
     */
    public List<T> queryByCondition(String where, Object... args) {
        log.debug("条件查询，where={}", where);
        return getRepository().findByWhere(where, args);
    }

    /**
     * 条件查询单个
     */
    public Optional<T> queryOneByCondition(String where, Object... args) {
        log.debug("条件查询单个，where={}", where);
        return getRepository().findOneByWhere(where, args);
    }

    /**
     * 自定义SQL查询
     */
    public List<T> querySql(String sql, Object... args) {
        log.debug("SQL查询，sql={}", sql);
        return getRepository().findBySql(sql, args);
    }

    /**
     * 创建（新增）
     */
    public T create(T entity) {
        log.debug("创建新实体，id={}", entity.getId());
        return getRepository().save(entity);
    }

    /**
     * 批量创建
     */
    public void createBatch(List<T> entities) {
        log.debug("批量创建，数量={}", entities.size());
        getRepository().saveAll(entities);
    }

    /**
     * 修改
     */
    public T modify(T entity) {
        log.debug("修改实体，id={}", entity.getId());
        return getRepository().save(entity);
    }

    /**
     * 删除
     */
    public boolean delete(ID id) {
        log.debug("删除实体，id={}", id);
        return getRepository().deleteById(id);
    }

    /**
     * 批量删除
     */
    public int deleteBatch(List<ID> ids) {
        log.debug("批量删除，数量={}", ids.size());
        return getRepository().deleteAllById(ids);
    }

    /**
     * 逻辑删除
     */
    public void softDelete(T entity) {
        log.debug("逻辑删除，id={}", entity.getId());
        getRepository().softDelete(entity);
    }

    /**
     * 批量逻辑删除
     */
    public void softDeleteBatch(List<T> entities) {
        log.debug("批量逻辑删除，数量={}", entities.size());
        getRepository().softDeleteAll(entities);
    }

    /**
     * 恢复删除
     */
    public void restore(T entity) {
        log.debug("恢复实体，id={}", entity.getId());
        getRepository().restore(entity);
    }

    /**
     * 获取总数
     */
    public long count() {
        return getRepository().count();
    }

    /**
     * 判断是否存在
     */
    public boolean exists(ID id) {
        return getRepository().existsById(id);
    }

    /**
     * 判断是否重复
     */
    public boolean isRepeat(String sql, Map<String, Object> params) {
        return getRepository().isRepeat(sql, params);
    }

    /**
     * 获取表名
     */
    public String getTableName() {
        return getRepository().getTableName();
    }
}
