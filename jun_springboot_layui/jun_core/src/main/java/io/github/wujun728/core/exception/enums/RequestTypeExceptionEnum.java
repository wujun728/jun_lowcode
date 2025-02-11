
package io.github.wujun728.core.exception.enums;

import io.github.wujun728.core.annotion.ExpEnumType;
import io.github.wujun728.core.consts.ExpEnumConstant;
import io.github.wujun728.core.exception.enums.abs.AbstractBaseExceptionEnum;
import io.github.wujun728.core.factory.ExpEnumCodeFactory;

/**
 * 请求类型相关异常枚举
 * @date 2020/4/2 15:42
 */
@ExpEnumType(module = ExpEnumConstant.SNOWY_CORE_MODULE_EXP_CODE, kind = ExpEnumConstant.REQUEST_TYPE_EXCEPTION_ENUM)
public enum RequestTypeExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 参数传递格式不支持
     */
    REQUEST_TYPE_IS_JSON(1, "参数传递格式不支持，请使用JSON格式传参"),

    /**
     * 请求JSON参数格式不正确
     */
    REQUEST_JSON_ERROR(2, "请求JSON参数格式不正确，请检查参数格式");

    private final Integer code;

    private final String message;

    RequestTypeExceptionEnum(Integer code, String message) {
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
