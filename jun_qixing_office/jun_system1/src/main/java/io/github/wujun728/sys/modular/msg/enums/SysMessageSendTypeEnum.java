
package io.github.wujun728.sys.modular.msg.enums;

import lombok.Getter;

/**
 * 消息发送类型枚举
 *
 * @date 2018/5/6 12:30
 */
@Getter
public enum SysMessageSendTypeEnum {

    /**
     * 直接发送
     */
    DIRECT(1, "直接发送"),

    /**
     * 定时发送
     */
    TIMER(2, "定时发送");

    private final Integer code;

    private final String message;

    SysMessageSendTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
