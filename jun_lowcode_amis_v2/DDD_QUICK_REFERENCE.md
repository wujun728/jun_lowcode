# DDD 框架 - 快速参考卡片

## 项目结构

```
com/jqp/ddd/
├── domain/
│   ├── BaseEntity.java           聚合根基类
│   └── Repository<T,ID>          仓储接口
│
├── infrastructure/
│   ├── jdbc/
│   │   ├── JdbcConfig.java       非Spring配置
│   │   └── SimpleJdbcRepository<T,ID>  通用JDBC实现
│   └── impl/
│       └── YourRepositoryImpl     具体仓储实现
│
├── application/
│   ├── BaseApplicationService<T,ID,R>  业务服务基类
│   └── YourApplicationService    具体应用服务
│
└── presentation/
    ├── Response<T>               统一响应
    ├── BaseRestController<T,S>   REST基类
    └── YourRestController        具体REST接口
```

## 5分钟快速开始

### 第1步：定义Entity
```java
@Data @EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    private String username;
    private String email;
}
```

### 第2步：定义Repository接口
```java
public interface UserRepository extends Repository<User, Long> {
    Optional<User> findByUsername(String username);
}
```

### 第3步：实现Repository
```java
@Repository
public class UserRepositoryImpl extends SimpleJdbcRepository<User, Long> implements UserRepository {
    @Override
    public Optional<User> findByUsername(String username) {
        return findOneByWhere("username = ? and deleted = 0", username);
    }
}
```

### 第4步：创建ApplicationService
```java
@Service
public class UserApplicationService extends BaseApplicationService<User, Long, UserRepository> {
    @Resource private UserRepository userRepository;
    @Override protected UserRepository getRepository() { return userRepository; }
}
```

### 第5步：创建RestController
```java
@RestController @RequestMapping("/api/users")
public class UserRestController extends BaseRestController<User, UserApplicationService> {
    @Resource private UserApplicationService service;
    @Override protected UserApplicationService getApplicationService() { return service; }
}
```

**完成！** 自动获得所有CRUD接口 ✅

---

## 核心方法速查

### Repository方法
```java
Optional<T> findById(ID id)                     单个查询
List<T> findAll()                               查询所有
Map<String, Object> findPage(pageNum, pageSize) 分页查询
List<T> findByWhere(where, args)                条件查询
T save(T entity)                                保存
boolean deleteById(ID id)                       删除
void softDelete(T entity)                       逻辑删除
```

### ApplicationService方法
```java
Optional<T> queryById(ID id)                    单个查询
List<T> queryAll()                              查询所有
Map<String, Object> pageQuery(p, s)             分页查询
T create(T entity)                              创建
T modify(T entity)                              修改
boolean delete(ID id)                           删除
void softDelete(T entity)                       逻辑删除
```

### RestController接口
```
GET    /                    查询所有
GET    /{id}                单个查询
POST   /page                分页查询
POST   /                    创建
PUT    /                    修改
DELETE /{id}                删除
POST   /delete-batch        批量删除
```

---

## 常用代码片段

### 自定义查询方法
```java
// 在Repository中
public List<User> findByStatus(Integer status) {
    return findByWhere("status = ? and deleted = 0", status);
}

// 在ApplicationService中
public List<User> queryActiveUsers() {
    return getRepository().findByStatus(1);
}

// 在RestController中
@GetMapping("/active")
public Response<List<User>> getActiveUsers() {
    return Response.ok(userService.queryActiveUsers());
}
```

### 业务规则实现
```java
// 在Entity中
public class User extends BaseEntity {
    private Integer status;

    public void activate() {
        this.status = 1;
        this.updateTime = LocalDateTime.now();
    }

    public boolean isActive() {
        return status != null && status == 1;
    }
}

// 在ApplicationService中
@Transactional
public void activateUser(Long id) {
    User user = queryById(id).orElseThrow();
    user.activate();
    modify(user);
}
```

### 数据验证
```java
// 在ApplicationService中
public void createUser(User user) {
    if (getRepository().findByUsername(user.getUsername()).isPresent()) {
        throw new IllegalArgumentException("用户名已存在");
    }
    create(user);
}

// 在RestController中
@PostMapping
public Response<User> create(@RequestBody User user) {
    try {
        return Response.ok(userService.createUser(user));
    } catch (IllegalArgumentException e) {
        return Response.fail(e.getMessage());
    }
}
```

---

## Spring环境配置

### application.yml
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/test
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    show-sql: false
```

### 启动类
```java
@SpringBootApplication
@ComponentScan(basePackages = {"com.jqp.ddd", "com.example"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

---

## 非Spring环境配置

### 初始化代码
```java
import com.jqp.ddd.infrastructure.jdbc.JdbcConfig;
import com.zaxxer.hikari.*;

public class Application {
    public static void main(String[] args) {
        // 创建数据源
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        config.setUsername("root");
        config.setPassword("password");
        HikariDataSource dataSource = new HikariDataSource(config);

        // 配置
        JdbcConfig.setDataSource(dataSource);

        // 使用
        UserRepositoryImpl userRepo = new UserRepositoryImpl();
        List<User> users = userRepo.findAll();
    }
}
```

---

## Response对象使用

```java
// 成功响应
Response.ok(data)                   // 返回数据
Response.ok(data, "自定义消息")      // 返回数据和消息
Response.ok()                       // 成功但无数据

// 失败响应
Response.fail("错误消息")            // 返回错误
Response.fail(404, "未找到")         // 自定义错误码

// JSON响应示例
{
    "code": 0,
    "message": "成功",
    "data": {...}
}
```

---

## BaseEntity字段

```java
protected Long id                           主键
protected LocalDateTime createTime          创建时间（自动赋值）
protected LocalDateTime updateTime          更新时间（自动赋值）
protected Integer deleted = 0               删除标记（0-正常，1-删除）

// 实用方法
public boolean isNew()                      是否新实体
public boolean isDeleted()                  是否已删除
public void markDeleted()                   标记为删除
public void markNotDeleted()                标记为正常
```

---

## SimpleJdbcRepository支持的操作

| 操作 | 方法 | 说明 |
|-----|------|------|
| 查询单个 | `Optional<T> findById(ID)` | 根据主键查询 |
| 查询所有 | `List<T> findAll()` | 查询所有未删除数据 |
| 分页查询 | `Map findPage(p, s)` | 基础分页 |
| 条件查询 | `List<T> findByWhere(where, args)` | WHERE条件查询 |
| 单个条件查询 | `Optional<T> findOneByWhere(where, args)` | 返回第一条 |
| 自定义SQL | `List<T> findBySql(sql, args)` | 执行自定义SQL |
| 新增 | `T save(T)` | 新增或更新 |
| 批量新增 | `void saveAll(List<T>)` | 批量保存 |
| 物理删除 | `boolean deleteById(ID)` | 彻底删除 |
| 批量物理删除 | `int deleteAllById(List<ID>)` | 批量删除 |
| 逻辑删除 | `void softDelete(T)` | 标记为删除 |
| 恢复 | `void restore(T)` | 恢复已删除数据 |
| 统计 | `long count()` | 获取总数 |
| 判断存在 | `boolean existsById(ID)` | 是否存在 |
| 重复检查 | `boolean isRepeat(sql, params)` | 检查数据重复 |

---

## 事务处理

```java
@Service
public class UserApplicationService extends BaseApplicationService<User, Long, UserRepository> {

    @Resource
    private OrderApplicationService orderService;

    // 单个服务事务
    @Transactional
    public void updateUser(User user) {
        modify(user);
    }

    // 多服务事务
    @Transactional
    public void createUserWithOrder(User user, Order order) {
        create(user);
        orderService.create(order);
        // 如果任何一步失败，全部回滚
    }
}
```

---

## 错误处理

```java
@RestController
public class UserRestController extends BaseRestController<User, UserApplicationService> {

    @PostMapping("/register")
    public Response<User> register(@RequestBody User user) {
        try {
            // 参数验证
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                return Response.fail("用户名不能为空");
            }

            // 调用业务服务
            User result = userService.createUser(user);
            return Response.ok(result, "注册成功");

        } catch (IllegalArgumentException e) {
            // 业务异常
            return Response.fail(e.getMessage());
        } catch (Exception e) {
            // 系统异常
            log.error("注册失败", e);
            return Response.fail("系统错误");
        }
    }
}
```

---

## 数据库表模板

```sql
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100),
    status INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

## 常见错误

| 错误 | 原因 | 解决方案 |
|-----|------|---------|
| DataSource未配置 | 非Spring环境未调用JdbcConfig.setDataSource | 调用JdbcConfig.setDataSource(ds) |
| 表名不匹配 | Entity类名与表名不对应 | 表名应为Entity简单类名转小写 |
| 字段名不匹配 | 驼峰字段名与下划线表字段名不对应 | 驼峰自动转下划线 |
| 无法插入数据 | id为主键不能手动指定 | insert时不指定id，由数据库自动生成 |

---

## 性能优化

### 查询优化
```java
// 不好：查询所有字段
List<User> users = userRepo.findAll();

// 更好：只查询需要的字段（使用自定义SQL）
List<User> users = userRepo.findBySql(
    "select id, username, email from user where deleted = 0"
);
```

### 批量操作
```java
// 不好：逐个保存
for (User user : users) {
    userRepo.save(user);
}

// 更好：批量保存
userRepo.saveAll(users);
```

### 分页查询
```java
// 总是使用分页，避免一次加载大量数据
Map<String, Object> pageData = userService.pageQuery(1, 10);
```

---

**记住：DDD让你的代码更优雅！** ✨
