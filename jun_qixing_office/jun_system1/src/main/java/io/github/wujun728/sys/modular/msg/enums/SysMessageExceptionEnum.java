
package io.github.wujun728.sys.modular.msg.enums;

import io.github.wujun728.core.annotion.ExpEnumType;
import io.github.wujun728.core.exception.enums.abs.AbstractBaseExceptionEnum;
import io.github.wujun728.core.factory.ExpEnumCodeFactory;
import io.github.wujun728.sys.core.consts.SysExpEnumConstant;

/**
 * 消息表
 * @date 2021-01-21 17:50:51
 */
@ExpEnumType(module = SysExpEnumConstant.QIXING_SYS_MODULE_EXP_CODE, kind = SysExpEnumConstant.MSG_EXCEPTION_ENUM)
public enum SysMessageExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 数据不存在
     */
    NOT_EXIST(1, "消息不存在");

    private final Integer code;

    private final String message;
        SysMessageExceptionEnum(Integer code, String message) {
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
