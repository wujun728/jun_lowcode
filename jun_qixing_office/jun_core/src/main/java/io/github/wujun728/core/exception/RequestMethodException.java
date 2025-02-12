
package io.github.wujun728.core.exception;

import io.github.wujun728.core.exception.enums.abs.AbstractBaseExceptionEnum;
import lombok.Getter;

/**
 * 请求方法异常
 * @date 2020/3/11 15:35
 */
@Getter
public class RequestMethodException extends RuntimeException {

    private final Integer code;

    private final String errorMessage;

    public RequestMethodException(AbstractBaseExceptionEnum exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.errorMessage = exception.getMessage();
    }
}
