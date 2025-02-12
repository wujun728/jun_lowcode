
package io.github.wujun728.core.consts;

/**
 * SpringSecurity相关常量
 * @date 2020/3/18 17:49
 */
public interface SpringSecurityConstant {

    /**
     * 放开权限校验的接口
     */
    String[] NONE_SECURITY_URL_PATTERNS = {

            //前端的
            "/assets/**",
            "/favicon.ico",

            //swagger相关的
            "/doc.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/v2/api-docs-ext",
            "/configuration/ui",
            "/configuration/security",

            //flowable工作流相关的
            "/designer/**",
            "/app/rest/**",

            //后端的
            "/",
            "/403",
            "/404",
            "/500",
            "/login",
            "/logout",
            "/getCaptcha",
            "/oauth/**",
            "/global/sessionError",
            "/error",
            "/global/error",

            //文件的
            "/sysFileInfo/upload",
            "/sysFileInfo/download",
            "/sysFileInfo/preview",
            "/sysFileInfo/onlineEditHtml",
            "/sysFileInfo/track",

            //druid的
            "/druid/**",

            //支付相关的
            "/aliPay/**",
            "/wxPay/**",

            //获取租户列表
            "/getTenantOpen",
            "/tenantInfo/listTenants",

            //WebSocket
            "/webSocket/**"
    };

}
