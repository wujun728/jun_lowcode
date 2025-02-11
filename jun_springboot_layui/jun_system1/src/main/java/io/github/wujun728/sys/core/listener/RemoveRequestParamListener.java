
package io.github.wujun728.sys.core.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.web.context.support.ServletRequestHandledEvent;
import io.github.wujun728.core.context.param.RequestParamContext;

/**
 * 用来清除临时缓存的@RequestBody的请求参数
 * @date 2020/8/21 21:14
 */
public class RemoveRequestParamListener implements ApplicationListener<ServletRequestHandledEvent> {

    @Override
    public void onApplicationEvent(ServletRequestHandledEvent event) {
        RequestParamContext.clear();
    }

}
