
package io.github.wujun728.sys.modular.user.enums;

import lombok.Getter;

/**
 * 性别枚举
 * @date 2020/5/28 10:51
 */
@Getter
public enum SysUserSexEnum {

    /**
     * 男
     */
    MAN(1, "男"),

    /**
     * 女
     */
    WOMAN(2, "女"),

    /**
     * 未知
     */
    NONE(3, "未知");

    private final Integer code;

    private final String message;

    SysUserSexEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
