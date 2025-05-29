适用场景
接口中没有复杂逻辑，都是sql执行
需要多种数据源
引入依赖
<dependency>
    <groupId>com.gitee.freakchicken</groupId>
    <artifactId>dbApi-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
使用案例
新建一个springboot的web项目，pom.xml中引入依赖
<dependency>
    <groupId>com.gitee.freakchicken</groupId>
    <artifactId>dbApi-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
<!--需要引入数据库的jdbc驱动-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.34</version>
</dependency>
在resources目录下创建数据库地址配置ds.xml
<datasource>
    <ds id="mysql">
        <!--注意xml下&符号要写成&amp;-->
        <url>jdbc:mysql://localhost:3306/story?useSSL=false&amp;characterEncoding=UTF-8</url>
        <username>root</username>
        <password>root</password>
    </ds>

    <ds id="local_mysql">
        <url>jdbc:mysql://localhost:3306/story?useSSL=false&amp;characterEncoding=UTF-8</url>
        <username>root</username>
        <password>root</password>
    </ds>

</datasource>
在resources目录下创建sql脚本配置sql.xml
<sql>
    <select id="getUser" db="mysql">
        select * from user
        <where>
            <if test = "id != null">
                id &lt;= #{id}
            </if>
        </where>
    </select>

    <select id="getUserIn" db="local_mysql">
        select * from user where id in
        <foreach collection="ids" open="(" close=")" separator=",">
            #{item}
        </foreach>

    </select>

</sql>
在application.properties中配置xml地址
dbapi.config.datasource=classpath:ds.xml
dbapi.config.sql=classpath:sql.xml
新建controller，注入DBApi，通过DBApi就可以执行sql
@RestController
public class HomeController {

    @Autowired
    DBApi dbApi;

    @RequestMapping("/hello")
    public ResponseDto hello(@RequestBody Map<String,Object> map) {
        // 第一个参数是sql执行的所有参数，一定要封装成Map<String,Object>类型
        // 第二个参数是上一步sql.xml中sql脚本对应的id
        ResponseDto execute = dbApi.execute(map, "getUser");
        return execute;
    }

    @RequestMapping("/getUserIn")
    public ResponseDto getUserIn(@RequestBody Map<String,Object> map) {

        // 第一个参数是sql执行的所有参数，一定要封装成Map<String,Object>类型
        // 第二个参数是上一步sql.xml中sql脚本对应的id
        ResponseDto execute = dbApi.execute(map, "getUserIn");
        return execute;
    }
}
这样，对于sql执行类的http api就已经开发完成了，接下来启动springboot应用，访问接口看看结果（我用python访问的）
import json
import requests

data = {"ids": [1, 2, 3, 4, 5, 6]}
re = requests.post("http://localhost:8888/getUserIn", json.dumps(data), headers={"Content-Type": "application/json"})
print(re.text)


#执行结果：
#{"msg":"dbApi接口访问成功","data":[{"name":"tom","id":1,"age":12,"height":1.83},{"name":"lily","id":2,"age":45,"height":1.75},{"name":"sss","id":5,"age":2,"height":1.65},{"name":"sss","id":6,"age":34,"height":1.84}],"success":true}
import json
import requests

data = {"id":3}
re = requests.post("http://localhost:8888/hello", json.dumps(data),headers={"Content-Type": "application/json"})print(re.text)


#执行结果：
#{"msg":"dbApi接口访问成功","data":[{"name":"tom","id":1,"age":12,"height":1.83},{"name":"lily","id":2,"age":45,"height":1.75}],"success":true}
案例完整代码请查看 dbApi-spring-boot-starter-demo
xml配置详解
数据库配置
支持多数据源，使用ds标签来指定， ds标签有个id属性，值是任意字符串，这个id必须全局唯一，sql配置的时候会指定db属性，也就是指向这个id

<datasource>
    <ds id="">
        <url></url>
        <username></username>
        <password></password>
    </ds>
</datasource>
sql配置
类似mybatis的语法，使用 select、update、insert、delete标签， 标签上有id和db两个属性，id必须全局唯一，DBApi执行的时候根据这个id查找到sql内容，sql内容是动态sql，语法和mybatis一样 db属性指定了数据库地址的id，必须在数据库配置的xml中能找到，也就是这个sql使用db对应的数据库来执行
defaultDB配置默认db，如果配置了该选项，那么select、update、insert、delete标签可以不用加db属性