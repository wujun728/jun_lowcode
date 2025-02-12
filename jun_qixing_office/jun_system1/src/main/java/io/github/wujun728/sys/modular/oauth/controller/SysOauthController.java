
package io.github.wujun728.sys.modular.oauth.controller;

import io.github.wujun728.sys.modular.oauth.service.SysOauthService;
import me.zhyd.oauth.model.AuthCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import io.github.wujun728.core.consts.CommonConstant;
import io.github.wujun728.core.consts.SymbolConstant;
import io.github.wujun728.core.context.constant.ConstantContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Oauth登录控制器
 * @date 2020/7/28 16:38
 **/
@RestController
public class SysOauthController {

    @Resource
    private SysOauthService sysOauthService;

    /**
     * oauth登录
     *
     * @author xuyuxiang
     * @date 2020/7/29 12:18
     **/
    @GetMapping("/oauth/render/{source}")
    public void renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        String authorizeUrl = sysOauthService.getAuthorizeUrl(source);
        response.sendRedirect(authorizeUrl);
    }

    /**
     * oauth平台中配置的授权回调地址
     *
     * @author xuyuxiang
     * @date 2020/7/29 12:19
     **/
    @GetMapping("/oauth/callback/{source}")
    public void callback(@PathVariable("source") String source, AuthCallback callback, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = sysOauthService.callback(source, callback, request);
        String webUrl = ConstantContextHolder.getWebUrl();
        response.sendRedirect(webUrl + SymbolConstant.QUESTION_MARK + CommonConstant.TOKEN_NAME + SymbolConstant.EQUAL + token);
    }
}
