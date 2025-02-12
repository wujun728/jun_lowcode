
package io.github.wujun728;

import cn.hutool.log.Log;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot方式启动类
 *
 * @date 2017/5/21 12:06
 */
@MapperScan(value = {"io.github.wujun.*.mapper","io.github.wujun728.*.mapper"}/*,annotationClass = Repository.class*/)
@SpringBootApplication(scanBasePackages = {"io.github.wujun","io.github.wujun728"})
public class QixingApplication {

    private static final Log log = Log.get();

    public static void main(String[] args) {
        SpringApplication.run(QixingApplication.class, args);
        log.info(">>> " + QixingApplication.class.getSimpleName() + " is success!");
    }

}
