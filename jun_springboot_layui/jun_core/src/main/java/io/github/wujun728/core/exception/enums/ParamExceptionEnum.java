
package io.github.wujun728.core.exception.enums;

import io.github.wujun728.core.annotion.ExpEnumType;
import io.github.wujun728.core.consts.ExpEnumConstant;
import io.github.wujun728.core.exception.enums.abs.AbstractBaseExceptionEnum;
import io.github.wujun728.core.factory.ExpEnumCodeFactory;

/**
 * 参数校验异常枚举
 * @date 2020/3/25 20:11
 */
@ExpEnumType(module = ExpEnumConstant.QIXING_CORE_MODULE_EXP_CODE, kind = ExpEnumConstant.PARAM_EXCEPTION_ENUM)
public enum ParamExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 参数错误
     */
    PARAM_ERROR(1, "参数错误");

    private final Integer code;

    private final String message;

    ParamExceptionEnum(Integer code, String message) {
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
