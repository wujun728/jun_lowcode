
package io.github.wujun728.sys.modular.menu.enums;

import lombok.Getter;

/**
 * 菜单类型枚举
 * @date 2020/5/25 15:18
 */
@Getter
public enum SysMenuTypeEnum {

    /**
     * 目录
     */
    DIR(0, "目录"),

    /**
     * 菜单
     */
    MENU(1, "菜单"),

    /**
     * 按钮
     */
    BTN(2, "按钮");

    private final Integer code;

    private final String message;

    SysMenuTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
