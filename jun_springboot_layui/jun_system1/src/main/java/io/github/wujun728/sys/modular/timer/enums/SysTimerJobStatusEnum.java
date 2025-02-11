
package io.github.wujun728.sys.modular.timer.enums;

import lombok.Getter;

/**
 * 定时任务的状态
 *
 * @date 2020/6/30 20:44
 */
@Getter
public enum SysTimerJobStatusEnum {

    /**
     * 启动状态
     */
    RUNNING(1),

    /**
     * 停止状态
     */
    STOP(2);

    private final Integer code;

    SysTimerJobStatusEnum(int code) {
        this.code = code;
    }

}
