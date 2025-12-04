package io.github.wujun728.admin.common.config;

import org.apache.ibatis.jdbc.ScriptRunner;

//@Component
//@ConditionalOnProperty(value="jqp.reset",havingValue = "true")
//@EnableAsync
//@Slf4j
//public class ResetDb {
//
//    @Resource
//    private JdbcTemplate jdbcTemplate;
//
//    @Resource
//    private DicCacheService dicCacheService;
//
//    {
//        log.info("重置Bean");
//    }
////    @Scheduled(fixedDelay = 3000)
////    @Scheduled(fixedRate = 3000)
////    @Scheduled(cron = "0 */5 * * * ?")
//    @Scheduled(cron = "0 0 1 * * ?")
////    @Async(value = "myAsync")
//    void resetDb() throws Exception {
//        log.info("开始还原数据库");
//        Connection conn = jdbcTemplate.getDataSource().getConnection();
//        ScriptRunner runner = new ScriptRunner(conn);
//        runner.setAutoCommit(true);
//        InputStream in = TestAmisController.class.getClassLoader().getResourceAsStream("db/jqp.sql");
//        runner.setFullLineDelimiter(false);
//        runner.setDelimiter(";"//语句结束符号设置
//        //runner.setLogWriter(null);//日志数据输出，这样就不会输出过程
//        runner.setSendFullScript(false);
//        runner.setAutoCommit(true);
//        runner.setStopOnError(true);
//        runner.runScript(new InputStreamReader(in, "utf8"));
//
//        log.info("结束还原数据库");
//        log.info("开始清理缓存---");
//        dicCacheService.clear();
//        AbstractCacheService.clearAll();
//        log.info("结束清理缓存---");
//    }
//
//}
