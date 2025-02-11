
package io.github.wujun728.sys.modular.oauth.enums;

import io.github.wujun728.core.annotion.ExpEnumType;
import io.github.wujun728.core.exception.enums.abs.AbstractBaseExceptionEnum;
import io.github.wujun728.core.factory.ExpEnumCodeFactory;
import io.github.wujun728.sys.core.consts.SysExpEnumConstant;

/**
 * 系统角色相关异常枚举
 * @date 2020/3/28 14:47
 */
@ExpEnumType(module = SysExpEnumConstant.QIXING_SYS_MODULE_EXP_CODE, kind = SysExpEnumConstant.OAUTH_EXCEPTION_ENUM)
public enum SysOauthExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * Oauth登录开关未开启
     */
    OAUTH_DISABLED(1, "Oauth登录开关未开启，无法使用Oauth登录"),

    /**
     * 不支持该平台Oauth登录
     */
    OAUTH_NOT_SUPPORT(2, "不支持该平台Oauth登录");

    private final Integer code;

    private final String message;

    SysOauthExceptionEnum(Integer code, String message) {
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
