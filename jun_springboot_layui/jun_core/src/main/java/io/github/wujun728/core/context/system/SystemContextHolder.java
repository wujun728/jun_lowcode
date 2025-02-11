
package io.github.wujun728.core.context.system;

import cn.hutool.extra.spring.SpringUtil;

/**
 * 使用system模块相关功能的接口
 * @date 2020/5/6 14:56
 */
public class SystemContextHolder {

    public static SystemContext me() {
        return SpringUtil.getBean(SystemContext.class);
    }

}
