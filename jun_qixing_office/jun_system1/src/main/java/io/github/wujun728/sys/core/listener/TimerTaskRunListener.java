
package io.github.wujun728.sys.core.listener;

import cn.hutool.cron.CronUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import io.github.wujun728.sys.modular.timer.entity.SysTimers;
import io.github.wujun728.sys.modular.timer.enums.SysTimerJobStatusEnum;
import io.github.wujun728.sys.modular.timer.param.SysTimersParam;
import io.github.wujun728.sys.modular.timer.service.SysTimersService;
import io.github.wujun728.sys.modular.timer.service.TimerExeService;

import java.util.List;

/**
 * 项目定时任务启动的listener
 *
 * @date 2020/7/1 15:30
 */
public class TimerTaskRunListener implements ApplicationListener<ApplicationStartedEvent>, Ordered {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {

        SysTimersService sysTimersService = SpringUtil.getBean(SysTimersService.class);
        TimerExeService timerExeService = SpringUtil.getBean(TimerExeService.class);

        // 获取所有开启状态的任务
        SysTimersParam sysTimersParam = new SysTimersParam();
        sysTimersParam.setJobStatus(SysTimerJobStatusEnum.RUNNING.getCode());
        List<SysTimers> list = sysTimersService.list(sysTimersParam);

        // 添加定时任务到调度器
        for (SysTimers sysTimers : list) {
            timerExeService.startTimer(String.valueOf(sysTimers.getId()), sysTimers.getCron(), sysTimers.getActionClass());
        }

        // 设置秒级别的启用
        CronUtil.setMatchSecond(true);

        // 启动定时器执行器
        CronUtil.start();
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
