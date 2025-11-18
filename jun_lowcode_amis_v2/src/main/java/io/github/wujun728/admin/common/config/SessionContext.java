package io.github.wujun728.admin.common.config;

import io.github.wujun728.admin.db.service.JdbcService;
import io.github.wujun728.admin.page.service.PageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
@Log4j2
@Scope("request")
public class SessionContext {

    @Resource
    private JdbcService jdbcService;

    @Resource
    @Lazy
    private PageService pageService;

    private UserSession userSession;

    static final String SPLIT = "$_$";

    public static boolean hasButtonPermission(String code) {
        return true;
    }

    public static UserSession getSession() {
        UserSession userSession1 = new UserSession();
        userSession1.setEnterpriseId(0L);
        return userSession1;
    }

    public static String getTemplateValue(String value) {
        return value;
    }

    public static void putUserSessionParams(Map<String, Object> params) {
    }

    public static boolean hasUrlPermission(String uri) {
        return true;
    }
}
