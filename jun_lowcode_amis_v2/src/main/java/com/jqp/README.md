# JQP 低代码平台 - 基础框架使用指南

## 概述

本框架提供了简洁高效的基础类，使开发者只需编写**最少的代码**就能完成功能开发。

### 核心设计理念
- **最少化DAO层**：数据库操作全部整合到BaseService中
- **开箱即用**：BaseController提供所有通用REST API接口
- **快速扩展**：只需实现Entity、Service、Controller三层

## 核心类说明

### 1. BaseData（基础实体类）
所有业务实体都应继承此类，自动获得以下字段：
```java
- id: Long              // 主键
- createTime: LocalDateTime  // 创建时间
- updateTime: LocalDateTime  // 更新时间
- deleted: Integer      // 逻辑删除标记（0-正常，1-删除）
```

### 2. Result（统一响应类）
所有REST API都返回此格式：
```json
{
    "status": 0,           // 0-成功，其他-失败
    "msg": "操作成功",      // 响应消息
    "data": {...}          // 响应数据
}
```

**常用方法**：
```java
Result.success(data)           // 成功响应
Result.success(data, "msg")    // 成功响应+自定义消息
Result.error("错误消息")        // 错误响应
```

### 3. BaseService（基础业务服务）
包含所有通用的数据操作方法，内部使用JdbcService完成数据库操作。

**常用方法**：
```java
list()                          // 获取所有数据
getById(Long id)                // 根据ID获取单条数据
page(pageNum, pageSize)         // 分页查询
page(pageNum, pageSize, sql)    // 带SQL条件的分页查询
save(T data)                    // 保存或更新数据
insert(T data)                  // 新增数据
update(T data)                  // 更新数据
delete(T data)                  // 删除数据
deleteById(Long id)             // 根据ID删除
deleteBySql(sql, values)        // 按SQL条件删除
findOne(sql, values)            // 查询单条数据
findList(sql, values)           // 查询多条数据
isRepeat(sql, params)           // 检查重复
```

### 4. BaseController（基础控制器）
提供以下REST API接口：
```
GET    /list                    获取所有数据
GET    /{id}                    根据ID获取单条数据
POST   /page                    分页查询
POST   /                        新增数据
PUT    /                        更新数据
DELETE /{id}                    删除单条数据
POST   /delete-batch            批量删除数据
```

## 快速开发流程

### 步骤1：创建实体类（Entity）

```java
package com.jqp.modules.demo.data;

import io.github.wujun728.record.common.BaseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Demo extends BaseData {
    private String name;
    private String code;
    private String description;
    // ... 其他业务字段
}
```

### 步骤2：创建Service服务

```java
package com.jqp.modules.demo.service;

import com.jqp.common.base.BaseService;
import com.jqp.modules.demo.data.Demo;
import org.springframework.stereotype.Service;

@Service
public class DemoService extends BaseService<Demo> {

    // 如果需要特殊业务逻辑，在此添加
    public Demo findByCode(String code) {
        String sql = "select * from demo where code = ?";
        return findOne(sql, code);
    }
}
```

### 步骤3：创建Controller控制器

```java
package com.jqp.modules.demo.controller;

import com.jqp.common.base.BaseController;
import com.jqp.modules.demo.data.Demo;
import com.jqp.modules.demo.service.DemoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
@RequestMapping("/api/demo")
public class DemoController extends BaseController<Demo, DemoService> {

    @Resource
    private DemoService demoService;

    @Override
    protected DemoService getService() {
        return demoService;
    }

    // 如果需要特殊业务接口，在此添加
}
```

**完整！** 现在你已经拥有以下API接口：
- `GET /api/demo/list` - 获取所有数据
- `GET /api/demo/{id}` - 获取单条数据
- `POST /api/demo/page` - 分页查询
- `POST /api/demo` - 新增数据
- `PUT /api/demo` - 更新数据
- `DELETE /api/demo/{id}` - 删除数据
- `POST /api/demo/delete-batch` - 批量删除

## 完整示例

### 查看 `/modules/dict` 目录下的完整示例实现：

- **Dict.java** - 实体类定义
- **DictService.java** - 业务服务（包含自定义业务方法）
- **DictController.java** - REST接口（包含自定义API）

### 数据库表结构示例

```sql
CREATE TABLE dict (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(100) NOT NULL UNIQUE COMMENT '字典编码',
    name VARCHAR(100) NOT NULL COMMENT '字典名称',
    category VARCHAR(50) NOT NULL COMMENT '字典分类',
    value VARCHAR(255) COMMENT '字典值',
    description VARCHAR(255) COMMENT '字典描述',
    status INT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    order_num INT DEFAULT 0 COMMENT '排序号',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '是否删除：0-否，1-是'
);
```

## API调用示例

### 获取所有数据
```bash
curl -X GET http://localhost:8080/api/dict/list
```

### 根据ID获取单条数据
```bash
curl -X GET http://localhost:8080/api/dict/1
```

### 分页查询
```bash
curl -X POST http://localhost:8080/api/dict/page \
  -H "Content-Type: application/json" \
  -d '{"pageNum": 1, "pageSize": 10}'
```

### 新增数据
```bash
curl -X POST http://localhost:8080/api/dict \
  -H "Content-Type: application/json" \
  -d '{
    "code": "ENABLE",
    "name": "启用状态",
    "category": "status",
    "value": "1",
    "status": 1,
    "orderNum": 1
  }'
```

### 更新数据
```bash
curl -X PUT http://localhost:8080/api/dict \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "code": "ENABLE",
    "name": "启用状态",
    "category": "status",
    "value": "1",
    "status": 1,
    "orderNum": 1
  }'
```

### 删除数据
```bash
curl -X DELETE http://localhost:8080/api/dict/1
```

### 批量删除
```bash
curl -X POST http://localhost:8080/api/dict/delete-batch \
  -H "Content-Type: application/json" \
  -d '{"ids": [1, 2, 3]}'
```

## BaseService高级用法

### 自定义SQL查询
```java
// 单条查询
Dict dict = dictService.findOne("select * from dict where code = ?", "ENABLE");

// 多条查询
List<Dict> list = dictService.findList(
    "select * from dict where category = ? order by order_num",
    "status"
);

// 分页查询
Map<String, Object> pageData = dictService.page(
    1, 10,
    "select * from dict where category = ? order by order_num",
    "status"
);
```

### 批量操作
```java
// 批量新增/更新
List<Dict> dictList = new ArrayList<>();
// 添加数据...
dictService.saveBatch(dictList);

// 批量删除
for (Long id : ids) {
    dictService.deleteById(id);
}
```

### 检查重复
```java
boolean repeat = dictService.isRepeat(
    "select id from dict where code = '$code' and id <> $id",
    Map.of("code", "ENABLE", "id", 1L)
);
```

## 注意事项

1. **表名与类名映射**：系统会自动根据实体类名推导表名（转小写）
2. **驼峰转下划线**：数据库字段名采用下划线命名，系统自动转换
3. **JdbcService注入**：BaseService中已自动注入JdbcService，无需手动注入
4. **事务管理**：复杂的事务操作需要在Service方法上添加`@Transactional`注解
5. **缓存管理**：如需缓存，可继承`AbstractCacheService`并实现load方法

## 常见问题

### Q: 如何自定义分页字段？
A: 在Service中重写page方法并传入自定义SQL条件：
```java
public Map<String, Object> customPage(int pageNum, int pageSize) {
    String sql = "where status = 1 order by create_time desc";
    return page(pageNum, pageSize, sql);
}
```

### Q: 如何添加复杂的业务逻辑？
A: 在Service类中添加新方法，可以调用BaseService提供的任何方法。

### Q: 如何实现缓存？
A: 创建新Service继承`AbstractCacheService<T>`而不是`BaseService<T>`，并实现load方法。

### Q: 如何进行权限验证？
A: 在Controller的对应方法上添加权限注解或在方法内调用权限检查。

## 相关文件位置

```
com.jqp.common.base/
├── BaseData.java              # 基础实体类
├── BaseService.java           # 基础业务层
├── BaseController.java        # 基础控制层
├── Result.java               # 统一响应类
└── CacheService.java         # 缓存服务接口

com.jqp.modules.dict/
├── data/Dict.java            # 字典实体
├── service/DictService.java  # 字典业务
└── controller/DictController.java  # 字典API
```

---

**快速开发 = 只需3个文件 + BaseService/BaseController的强大功能** ✨
