
package io.github.wujun728.sys.modular.log.enums;

import lombok.Getter;

/**
 * 访问日志类型枚举
 * @date 2020/3/12 15:50
 */
@Getter
public enum SysVisLogTypeEnum {

    /**
     * 登录日志
     */
    LOGIN(1, "登录"),

    /**
     * 退出日志
     */
    EXIT(2, "登出");

    private final Integer code;

    private final String message;

    SysVisLogTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
