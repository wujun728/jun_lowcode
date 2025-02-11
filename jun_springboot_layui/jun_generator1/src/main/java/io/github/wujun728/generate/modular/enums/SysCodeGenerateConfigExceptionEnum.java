
package io.github.wujun728.generate.modular.enums;

import io.github.wujun728.core.annotion.ExpEnumType;
import io.github.wujun728.core.exception.enums.abs.AbstractBaseExceptionEnum;
import io.github.wujun728.core.factory.ExpEnumCodeFactory;
import io.github.wujun728.generate.core.consts.GenExpEnumConstant;

/**
 * 代码生成详细配置
 *
 * @date 2021-02-06 20:19:49
 */
@ExpEnumType(module = GenExpEnumConstant.SNOWY_GEN_MODULE_EXP_CODE, kind = GenExpEnumConstant.GEN_CONFIG_EXCEPTION_ENUM)
public enum SysCodeGenerateConfigExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 数据不存在
     */
    NOT_EXIST(1, "此数据不存在");

    private final Integer code;

    private final String message;
        SysCodeGenerateConfigExceptionEnum(Integer code, String message) {
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
