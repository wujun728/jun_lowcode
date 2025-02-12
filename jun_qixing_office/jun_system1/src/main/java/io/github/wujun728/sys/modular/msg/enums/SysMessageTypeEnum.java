
package io.github.wujun728.sys.modular.msg.enums;

import lombok.Getter;

/**
 * 消息类型枚举
 *
 * @date 2018/5/6 12:30
 */
@Getter
public enum SysMessageTypeEnum {

    /**
     * 通知
     */
    NOTICE(1, "通知"),

    /**
     * 私信
     */
    PRIVATE_MSG(2, "私信"),

    /**
     * 待办
     */
    TODO(3, "待办");

    private final Integer code;

    private final String message;

    SysMessageTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
