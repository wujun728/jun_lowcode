
package io.github.wujun728.core.annotion;

import java.lang.annotation.*;

/**
 * 数据权限注解
 * @date 2020/3/28 17:16
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DataScope {
}
