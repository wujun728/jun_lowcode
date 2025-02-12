
package io.github.wujun728.sys.modular.notice.enums;

import lombok.Getter;

/**
 * 通知公告用户读取状态
 * @date 2020/6/29 11:02
 */
@Getter
public enum SysNoticeUserStatusEnum {

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

    SysNoticeUserStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
