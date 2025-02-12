
package io.github.wujun728.sys.modular.log.enums;

import lombok.Getter;

/**
 * 日志成功状态
 * @date 2020/3/12 15:53
 */
@Getter
public enum SysLogSuccessStatusEnum {

    /**
     * 失败
     */
    FAIL("N", "失败"),

    /**
     * 成功
     */
    SUCCESS("Y", "成功");

    private final String code;

    private final String message;

    SysLogSuccessStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
