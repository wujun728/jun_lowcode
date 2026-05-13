package com.jqp.ddd.infrastructure.jdbc;

import com.jqp.ddd.domain.BaseEntity;
import com.jqp.ddd.domain.Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 通用JDBC仓储实现
 * 支持Spring和非Spring环境
 * 提供CRUD和复杂查询能力
 *
 * @param <T> 实体类型
 * @param <ID> 主键类型
 * @author JQP
 * @date 2026/02/28
 */
@Slf4j
public abstract class SimpleJdbcRepository<T extends BaseEntity, ID> implements Repository<T, ID> {

    private DataSource dataSource;

    /**
     * Spring环境：自动注入DataSource
     */
    @Autowired(required = false)
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 获取DataSource（支持Spring和非Spring环境）
     */
    protected DataSource getDataSource() {
        if (dataSource != null) {
            return dataSource;
        }
        return JdbcConfig.getDataSource();
    }

    /**
     * 获取实体类
     */
    @Override
    public Class<T> getEntityClass() {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    /**
     * 获取表名（实体类名转小写）
     */
    @Override
    public String getTableName() {
        String className = getEntityClass().getSimpleName();
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    /**
     * 保存或更新实体
     */
    @Override
    public T save(T entity) {
        if (entity.isNew()) {
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());
            insert(entity);
        } else {
            entity.setUpdateTime(LocalDateTime.now());
            update(entity);
        }
        return entity;
    }

    /**
     * 批量保存
     */
    @Override
    public void saveAll(List<T> entities) {
        for (T entity : entities) {
            save(entity);
        }
    }

    /**
     * 根据ID查询
     */
    @Override
    public Optional<T> findById(ID id) {
        String sql = "select * from " + getTableName() + " where id = ? and deleted = 0";
        return findOneByWhere("id = ? and deleted = 0", id);
    }

    /**
     * 查询所有（不包含已删除）
     */
    @Override
    public List<T> findAll() {
        String sql = "select * from " + getTableName() + " where deleted = 0";
        return executeQuery(sql, rs -> mapResultSetToEntity(rs));
    }

    /**
     * 分页查询
     */
    @Override
    public Map<String, Object> findPage(int pageNum, int pageSize) {
        return findPage(pageNum, pageSize, "deleted = 0", (Object[]) null);
    }

    /**
     * 带条件的分页查询
     */
    @Override
    public Map<String, Object> findPage(int pageNum, int pageSize, String where, Object... args) {
        long total = countByWhere(where, args);
        int offset = (pageNum - 1) * pageSize;

        String sql = "select * from " + getTableName() + " where " + where + " limit " + offset + ", " + pageSize;
        List<T> items = executeQuery(sql, rs -> mapResultSetToEntity(rs), args);

        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("items", items);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }

    /**
     * 按WHERE条件查询列表
     */
    @Override
    public List<T> findByWhere(String where, Object... args) {
        String sql = "select * from " + getTableName() + " where " + where;
        return executeQuery(sql, rs -> mapResultSetToEntity(rs), args);
    }

    /**
     * 按WHERE条件查询单个
     */
    @Override
    public Optional<T> findOneByWhere(String where, Object... args) {
        String sql = "select * from " + getTableName() + " where " + where + " limit 1";
        List<T> results = executeQuery(sql, rs -> mapResultSetToEntity(rs), args);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    /**
     * 执行自定义SQL查询
     */
    @Override
    public List<T> findBySql(String sql, Object... args) {
        return executeQuery(sql, rs -> mapResultSetToEntity(rs), args);
    }

    /**
     * 根据ID删除
     */
    @Override
    public boolean deleteById(ID id) {
        String sql = "delete from " + getTableName() + " where id = ?";
        return executeUpdate(sql, id) > 0;
    }

    /**
     * 删除实体
     */
    @Override
    public boolean delete(T entity) {
        return deleteById((ID) entity.getId());
    }

    /**
     * 批量删除
     */
    @Override
    public int deleteAllById(Iterable<ID> ids) {
        List<ID> idList = new ArrayList<>();
        ids.forEach(idList::add);
        if (idList.isEmpty()) return 0;

        String placeholders = idList.stream().map(id -> "?").collect(Collectors.joining(","));
        String sql = "delete from " + getTableName() + " where id in (" + placeholders + ")";
        return executeUpdate(sql, idList.toArray());
    }

    /**
     * 逻辑删除
     */
    @Override
    public void softDelete(T entity) {
        entity.markDeleted();
        update(entity);
    }

    /**
     * 批量逻辑删除
     */
    @Override
    public void softDeleteAll(List<T> entities) {
        for (T entity : entities) {
            softDelete(entity);
        }
    }

    /**
     * 恢复删除
     */
    @Override
    public void restore(T entity) {
        entity.markNotDeleted();
        update(entity);
    }

    /**
     * 获取总数
     */
    @Override
    public long count() {
        String sql = "select count(*) from " + getTableName() + " where deleted = 0";
        return countByWhere("deleted = 0");
    }

    /**
     * 判断是否存在
     */
    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    /**
     * 判断是否重复
     */
    @Override
    public boolean isRepeat(String sql, Map<String, Object> params) {
        return executeQuery(sql, rs -> rs.next() && rs.getLong(1) > 0, params.values().toArray()).get(0);
    }

    // ============ 私有方法 ============

    /**
     * 插入记录
     */
    private void insert(T entity) {
        Map<String, Object> values = entityToMap(entity);
        List<String> columns = new ArrayList<>(values.keySet());
        String placeholders = columns.stream().map(c -> "?").collect(Collectors.joining(","));
        String sql = "insert into " + getTableName() + " (" + String.join(",", columns) + ") values (" + placeholders + ")";
        executeUpdate(sql, columns.stream().map(values::get).toArray());
        log.debug("插入记录成功，表={}", getTableName());
    }

    /**
     * 更新记录
     */
    private void update(T entity) {
        Map<String, Object> values = entityToMap(entity);
        values.remove("id");
        List<String> columns = new ArrayList<>(values.keySet());
        String setClause = columns.stream().map(c -> c + "=?").collect(Collectors.joining(","));
        String sql = "update " + getTableName() + " set " + setClause + " where id = ?";

        Object[] args = new Object[columns.size() + 1];
        for (int i = 0; i < columns.size(); i++) {
            args[i] = values.get(columns.get(i));
        }
        args[columns.size()] = entity.getId();

        executeUpdate(sql, args);
        log.debug("更新记录成功，表={}，id={}", getTableName(), entity.getId());
    }

    /**
     * 执行查询
     */
    private <R> List<R> executeQuery(String sql, QueryResultMapper<R> mapper, Object... args) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                List<R> results = new ArrayList<>();
                while (rs.next()) {
                    results.add(mapper.map(rs));
                }
                return results;
            }
        } catch (SQLException e) {
            log.error("查询失败，sql={}", sql, e);
            throw new RuntimeException("数据库查询失败", e);
        }
    }

    /**
     * 执行更新
     */
    private int executeUpdate(String sql, Object... args) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("更新失败，sql={}", sql, e);
            throw new RuntimeException("数据库更新失败", e);
        }
    }

    /**
     * 统计记录数
     */
    private long countByWhere(String where, Object... args) {
        String sql = "select count(*) as cnt from " + getTableName() + " where " + where;
        List<Long> results = executeQuery(sql, rs -> rs.getLong("cnt"), args);
        return results.isEmpty() ? 0 : results.get(0);
    }

    /**
     * 获取连接
     */
    private Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    /**
     * 将ResultSet映射到实体对象
     */
    @SuppressWarnings("unchecked")
    private T mapResultSetToEntity(ResultSet rs) throws SQLException {
        T entity = getEntityClass().getDeclaredConstructor().newInstance();
        entity.setId(rs.getLong("id"));
        entity.setDeleted(rs.getInt("deleted"));

        Timestamp createTime = rs.getTimestamp("create_time");
        if (createTime != null) {
            entity.setCreateTime(createTime.toLocalDateTime());
        }

        Timestamp updateTime = rs.getTimestamp("update_time");
        if (updateTime != null) {
            entity.setUpdateTime(updateTime.toLocalDateTime());
        }

        return entity;
    }

    /**
     * 将实体转换为Map
     */
    private Map<String, Object> entityToMap(T entity) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", entity.getId());
        map.put("deleted", entity.getDeleted());
        map.put("create_time", entity.getCreateTime());
        map.put("update_time", entity.getUpdateTime());
        return map;
    }

    /**
     * ResultSet映射器接口
     */
    @FunctionalInterface
    private interface QueryResultMapper<R> {
        R map(ResultSet rs) throws SQLException;
    }
}
