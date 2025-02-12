
package io.github.wujun728.sys.modular.email.enums;

import io.github.wujun728.core.annotion.ExpEnumType;
import io.github.wujun728.core.exception.enums.abs.AbstractBaseExceptionEnum;
import io.github.wujun728.core.factory.ExpEnumCodeFactory;
import io.github.wujun728.sys.core.consts.SysExpEnumConstant;

/**
 * 系统应用相关异常枚举
 * @date 2020/3/26 10:11
 */
@ExpEnumType(module = SysExpEnumConstant.QIXING_SYS_MODULE_EXP_CODE, kind = SysExpEnumConstant.EMAIL_EXCEPTION_ENUM)
public enum SysEmailExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 收件人为空
     */
    EMAIL_TO_EMPTY(1, "收件人为空，请检查to参数"),

    /**
     * 标题为空
     */
    EMAIL_TITLE_EMPTY(2, "标题为空，请检查title参数"),

    /**
     * 内容为空
     */
    EMAIL_CONTENT_EMPTY(3, "内容为空，请检查content参数"),

    /**
     * 邮件发送失败
     */
    EMAIL_SEND_ERROR(4, "邮件发送失败，请检查发送参数");

    private final Integer code;

    private final String message;

    SysEmailExceptionEnum(Integer code, String message) {
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
