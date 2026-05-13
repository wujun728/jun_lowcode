package com.jqp.ddd.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * DDD仓储接口
 * 定义数据持久化操作的标准契约
 * 支持Spring和非Spring环境
 *
 * @param <T> 聚合根类型
 * @param <ID> 主键类型
 * @author JQP
 * @date 2026/02/28
 */
public interface Repository<T extends BaseEntity, ID> {

    /**
     * 保存或更新实体
     *
     * @param entity 实体对象
     * @return 保存后的实体
     */
    T save(T entity);

    /**
     * 批量保存或更新实体
     *
     * @param entities 实体列表
     */
    void saveAll(List<T> entities);

    /**
     * 根据ID查询实体
     *
     * @param id 主键ID
     * @return 实体对象（Optional包装）
     */
    Optional<T> findById(ID id);

    /**
     * 获取所有实体（不包含已删除的）
     *
     * @return 实体列表
     */
    List<T> findAll();

    /**
     * 分页查询实体
     *
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页结果（包含total和items）
     */
    Map<String, Object> findPage(int pageNum, int pageSize);

    /**
     * 带条件的分页查询
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param where WHERE子句（不包含where关键字）
     * @param args SQL参数
     * @return 分页结果
     */
    Map<String, Object> findPage(int pageNum, int pageSize, String where, Object... args);

    /**
     * 查询满足条件的实体列表
     *
     * @param where WHERE子句
     * @param args SQL参数
     * @return 实体列表
     */
    List<T> findByWhere(String where, Object... args);

    /**
     * 查询满足条件的单个实体
     *
     * @param where WHERE子句
     * @param args SQL参数
     * @return 实体对象（Optional包装）
     */
    Optional<T> findOneByWhere(String where, Object... args);

    /**
     * 执行自定义SQL查询
     *
     * @param sql SQL语句
     * @param args SQL参数
     * @return 实体列表
     */
    List<T> findBySql(String sql, Object... args);

    /**
     * 根据ID删除实体（物理删除）
     *
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean deleteById(ID id);

    /**
     * 删除实体（物理删除）
     *
     * @param entity 实体对象
     * @return 是否删除成功
     */
    boolean delete(T entity);

    /**
     * 批量删除实体
     *
     * @param ids ID列表
     * @return 删除的记录数
     */
    int deleteAllById(Iterable<ID> ids);

    /**
     * 逻辑删除实体
     *
     * @param entity 实体对象
     */
    void softDelete(T entity);

    /**
     * 逻辑删除多个实体
     *
     * @param entities 实体列表
     */
    void softDeleteAll(List<T> entities);

    /**
     * 恢复逻辑删除的实体
     *
     * @param entity 实体对象
     */
    void restore(T entity);

    /**
     * 获取数据总数
     *
     * @return 总数
     */
    long count();

    /**
     * 判断数据是否存在
     *
     * @param id 主键ID
     * @return 是否存在
     */
    boolean existsById(ID id);

    /**
     * 判断数据是否重复
     *
     * @param sql 查询SQL
     * @param params 参数
     * @return 是否重复
     */
    boolean isRepeat(String sql, Map<String, Object> params);

    /**
     * 获取实体类型
     */
    Class<T> getEntityClass();

    /**
     * 获取表名
     */
    String getTableName();
}
