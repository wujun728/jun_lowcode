
package io.github.wujun728.core.tenant.exception;

import io.github.wujun728.core.exception.enums.abs.AbstractBaseExceptionEnum;
import io.github.wujun728.core.exception.ServiceException;

/**
 * 多租户的异常
 * @date 2020/9/3
 */
public class TenantException extends ServiceException {

    public TenantException(AbstractBaseExceptionEnum exception) {
        super(exception);
    }

}
