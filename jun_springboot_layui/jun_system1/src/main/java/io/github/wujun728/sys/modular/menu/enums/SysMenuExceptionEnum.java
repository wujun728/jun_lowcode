
package io.github.wujun728.sys.modular.menu.enums;

import io.github.wujun728.core.annotion.ExpEnumType;
import io.github.wujun728.core.exception.enums.abs.AbstractBaseExceptionEnum;
import io.github.wujun728.core.factory.ExpEnumCodeFactory;
import io.github.wujun728.sys.core.consts.SysExpEnumConstant;

/**
 * 系统菜单相关异常枚举
 * @date 2020/3/26 10:12
 */
@ExpEnumType(module = SysExpEnumConstant.SNOWY_SYS_MODULE_EXP_CODE, kind = SysExpEnumConstant.SYS_MENU_EXCEPTION_ENUM)
public enum SysMenuExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 菜单不存在
     */
    MENU_NOT_EXIST(1, "菜单不存在"),

    /**
     * 菜单编码重复
     */
    MENU_CODE_REPEAT(2, "菜单编码重复，请检查code参数"),

    /**
     * 菜单名称重复
     */
    MENU_NAME_REPEAT(3, "菜单名称重复，请检查name参数"),

    /**
     * 路由地址为空
     */
    MENU_ROUTER_EMPTY(4, "路由地址为空，请检查router参数"),

    /**
     * 权限标识格式为空
     */
    MENU_PERMISSION_EMPTY(5, "权限标识为空，请检查permission参数"),

    /**
     * 权限标识格式错误
     */
    MENU_PERMISSION_ERROR(6, "权限标识格式错误，请检查permission参数"),

    /**
     * 权限不存在
     */
    MENU_PERMISSION_NOT_EXIST(7, "权限不存在，请检查permission参数"),

    /**
     * 不能移动根节点
     */
    CANT_MOVE_APP(8, "父节点不是根节点不能移动应用"),

    /**
     * 父级菜单不能为当前节点，请重新选择父级菜单
     */
    PID_CANT_EQ_ID(9, "父级菜单不能为当前节点，请重新选择父级菜单"),

    /**
     * 父节点不能为本节点的子节点，请重新选择父节点
     */
    PID_CANT_EQ_CHILD_ID(6, "父节点不能为本节点的子节点，请重新选择父节点");

    private final Integer code;

    private final String message;

    SysMenuExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return ExpEnumCodeFactory.getExpEnumCode(this.getClass(), code);
    }

    @Override
    public String getMessage() {
        return message;
    }

}
