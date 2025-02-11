
package io.github.wujun728;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Qixing Web程序启动类
 * @date 2017-05-21 9:43
 */
public class QixingServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(QixingApplication.class);
    }

}
