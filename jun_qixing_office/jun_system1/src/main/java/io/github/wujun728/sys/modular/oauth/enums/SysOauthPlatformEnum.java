
package io.github.wujun728.sys.modular.oauth.enums;

import lombok.Getter;

/**
 * Oauth登录授权平台的枚举
 * @date 2020/7/28 16:46
 **/
@Getter
public enum SysOauthPlatformEnum {

    /**
     * 码云
     */
    GITEE("gitee", "码云"),

    /**
     * github
     */
    GITHUB("github", "github");

    private final String code;

    private final String message;

    SysOauthPlatformEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
