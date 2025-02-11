
package io.github.wujun728.sys.modular.msg.enums;

import lombok.Getter;

/**
 * 系统消息用户读取状态
 * @date 2020/6/29 11:02
 */
@Getter
public enum SysMessageUserStatusEnum {

    /**
     * 未读
     */
    UNREAD(0, "未读"),

    /**
     * 已读
     */
    READ(1, "已读");

    private final Integer code;

    private final String message;

    SysMessageUserStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
