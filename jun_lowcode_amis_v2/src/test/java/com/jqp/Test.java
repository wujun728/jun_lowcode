package com.jqp;

import io.github.wujun728.db.record.Db;
import io.github.wujun728.db.utils.DataSourcePool;

import javax.sql.DataSource;

import static io.github.wujun728.db.record.Db.main;


public class Test {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3307/db_qixing_v2?serverTimezone=GMT%2B8&useSSL=false";
        String username = "root";
        String password = "mysqladmin";
        //String sqlId = Db.queryStr("select sql_text from api_sql  limit 1 ");
        DataSource dataSource = DataSourcePool.init("main",url,username,password);
        Db.init(main,dataSource);


        Boolean flag =   Db.deleteById("jqp_test_order", "id", "1444005147197440");
    }
}
