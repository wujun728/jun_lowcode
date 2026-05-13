package io.github.wujun728.record.common.service;

import io.github.wujun728.record.common.BaseData;
import io.github.wujun728.record.common.PageData;
import io.github.wujun728.record.common.PageParam;
import io.github.wujun728.record.common.Result;
import io.github.wujun728.record.db.service.JdbcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

/**
 * 通用业务服务抽象类
 * 所有业务服务类只需继承此类即可获得基本的CRUD功能
 */
public abstract class AbstractService<T extends BaseData> {

    @Autowired
    protected JdbcService jdbcService;

    /**
     * 获取实体类类型
     * @return 实体类类型
     */
    protected abstract Class<T> getEntityClass();

    /**
     * 新增数据
     * @param entity 实体对象
     */
    public void insert(T entity) {
        jdbcService.insert(entity);
    }

    /**
     * 修改数据
     * @param entity 实体对象
     */
    public void update(T entity) {
        jdbcService.update(entity);
    }

    /**
     * 保存或更新数据
     * @param entity 实体对象
     */
    public void saveOrUpdate(T entity) {
        jdbcService.saveOrUpdate(entity);
    }

    /**
     * 删除数据
     * @param id 数据ID
     */
    public void delete(Long id) {
        jdbcService.delete(id, getEntityClass());
    }

    /**
     * 根据ID查询数据
     * @param id 数据ID
     * @return 实体对象
     */
    public T getById(Long id) {
        return jdbcService.getById(getEntityClass(), id);
    }

    /**
     * 查询所有数据
     * @return 数据列表
     */
    public List<T> findAll() {
        return jdbcService.find(getEntityClass());
    }

    /**
     * 根据条件查询数据列表
     * @param field 字段名
     * @param value 字段值
     * @return 数据列表
     */
    public List<T> findByField(String field, Object value) {
        return jdbcService.find(getEntityClass(), field, value);
    }

    /**
     * 根据条件查询单条数据
     * @param field 字段名
     * @param value 字段值
     * @return 实体对象
     */
    public T findOneByField(String field, Object value) {
        return jdbcService.findOne(getEntityClass(), field, value);
    }

    /**
     * 根据条件查询数据列表
     * @param fields 字段名数组
     * @param args 字段值数组
     * @return 数据列表
     */
    public List<T> findByFields(String[] fields, Object[] args) {
        return jdbcService.find(getEntityClass(), fields, args);
    }

    /**
     * 根据条件查询单条数据
     * @param fields 字段名数组
     * @param args 字段值数组
     * @return 实体对象
     */
    public T findOneByFields(String[] fields, Object[] args) {
        return jdbcService.findOne(getEntityClass(), fields, args);
    }

    /**
     * 分页查询数据
     * @param pageParam 分页参数
     * @param sql SQL语句
     * @param args SQL参数
     * @return 分页数据
     */
    public Result<PageData<T>> query(PageParam pageParam, String sql, Object... args) {
        return jdbcService.query(pageParam, getEntityClass(), sql, args);
    }

    /**
     * 批量新增数据
     * @param entities 实体对象列表
     */
    public void batchInsert(List<T> entities) {
        jdbcService.bathSaveOrUpdate(entities);
    }

    /**
     * 批量修改数据
     * @param entities 实体对象列表
     */
    public void batchUpdate(List<T> entities) {
        jdbcService.bathSaveOrUpdate(entities);
    }

    /**
     * 批量删除数据
     * @param ids ID列表
     */
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            jdbcService.delete(id, getEntityClass());
        }
    }

    /**
     * 自定义SQL查询
     * @param sql SQL语句
     * @param args SQL参数
     * @return 数据列表
     */
    public List<T> customQuery(String sql, Object... args) {
        return jdbcService.find(sql, getEntityClass(), args);
    }

    /**
     * 自定义SQL查询单条数据
     * @param sql SQL语句
     * @param args SQL参数
     * @return 实体对象
     */
    public T customQueryOne(String sql, Object... args) {
        return jdbcService.findOne(sql, getEntityClass(), args);
    }
    
    /**
     * 导入数据
     * @param inputStream 输入流
     * @param dataHandler 数据处理函数
     * @return 导入结果
     */
    public Result<Boolean> importData(InputStream inputStream, Consumer<List<T>> dataHandler) {
        try {
            // 实现数据导入逻辑
            // 这里只是一个示例，实际实现需要根据具体的文件格式进行解析
            List<T> dataList = parseImportData(inputStream);
            dataHandler.accept(dataList);
            batchInsert(dataList);
            return Result.success(true);
        } catch (Exception e) {
            return Result.error("导入失败：" + e.getMessage());
        }
    }
    
    /**
     * 解析导入数据
     * @param inputStream 输入流
     * @return 解析后的数据列表
     * @throws Exception 解析异常
     */
    protected List<T> parseImportData(InputStream inputStream) throws Exception {
        // 这里只是一个示例，实际实现需要根据具体的文件格式进行解析
        throw new UnsupportedOperationException("导入数据解析方法需要在子类中实现");
    }
    
    /**
     * 导出数据
     * @param outputStream 输出流
     * @param sql SQL语句
     * @param args SQL参数
     */
    public void exportData(OutputStream outputStream, String sql, Object... args) {
        try {
            List<T> dataList = customQuery(sql, args);
            writeExportData(outputStream, dataList);
        } catch (Exception e) {
            throw new RuntimeException("导出数据失败：" + e.getMessage(), e);
        }
    }
    
    /**
     * 导出所有数据
     * @param outputStream 输出流
     */
    public void exportAllData(OutputStream outputStream) {
        try {
            List<T> dataList = findAll();
            writeExportData(outputStream, dataList);
        } catch (Exception e) {
            throw new RuntimeException("导出所有数据失败：" + e.getMessage(), e);
        }
    }
    
    /**
     * 写入导出数据
     * @param outputStream 输出流
     * @param dataList 数据列表
     * @throws Exception 写入异常
     */
    protected void writeExportData(OutputStream outputStream, List<T> dataList) throws Exception {
        // 这里只是一个示例，实际实现需要根据具体的文件格式进行写入
        throw new UnsupportedOperationException("导出数据写入方法需要在子类中实现");
    }
    
    /**
     * 下载数据
     * @param outputStream 输出流
     * @param sql SQL语句
     * @param args SQL参数
     */
    public void downloadData(OutputStream outputStream, String sql, Object... args) {
        exportData(outputStream, sql, args);
    }
    
    /**
     * 下载所有数据
     * @param outputStream 输出流
     */
    public void downloadAllData(OutputStream outputStream) {
        exportAllData(outputStream);
    }
}
