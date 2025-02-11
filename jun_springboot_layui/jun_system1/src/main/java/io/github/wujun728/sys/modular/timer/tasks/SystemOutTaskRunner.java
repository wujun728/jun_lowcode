
package io.github.wujun728.sys.modular.timer.tasks;

import org.springframework.stereotype.Component;
import io.github.wujun728.core.timer.TimerTaskRunner;

/**
 * 这是一个定时任务的示例程序
 *
 * @date 2020/6/30 22:09
 */
@Component
public class SystemOutTaskRunner implements TimerTaskRunner {

    @Override
    public void action() {
        System.out.println("一直往南方开！一直往南方开！");
    }

}
