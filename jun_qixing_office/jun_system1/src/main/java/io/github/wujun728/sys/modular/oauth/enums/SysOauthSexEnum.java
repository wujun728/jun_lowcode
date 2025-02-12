
package io.github.wujun728.sys.modular.oauth.enums;

import lombok.Getter;

/**
 * Oauth用户性别枚举
 * @date 2020/7/29 10:32
 **/
@Getter
public enum SysOauthSexEnum {

    /**
     * 男
     */
    MAN("1", "男"),

    /**
     * 女
     */
    WOMAN("0", "女"),

    /**
     * 未知
     */
    NONE("-1", "未知");

    private final String code;

    private final String message;

    SysOauthSexEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
