
package io.github.wujun728.sys.modular;

import cn.hutool.core.util.ObjectUtil;
import io.github.wujun728.sys.modular.auth.service.AuthService;
import io.github.wujun728.sys.modular.msg.service.SysMessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import io.github.wujun728.core.context.login.LoginContextHolder;
import io.github.wujun728.core.exception.enums.AuthExceptionEnum;
import io.github.wujun728.core.pojo.login.SysLoginUser;
import io.github.wujun728.core.util.HttpServletUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 首页控制器
 * @date 2020/3/18 11:20
 */
@Controller
public class IndexController {

    @Resource
    private AuthService authService;

    @Resource
    private SysMessageService sysMessageService;

    /**
     * 访问首页，提示语
     *
     * @author xuyuxiang
     * @date 2020/4/8 19:27
     */
    @GetMapping("/")
    public String index(Model model) {
        //判断是否登录
        boolean hasLogin = LoginContextHolder.me().hasLogin();
        if(hasLogin) {
            SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserUpToDate();
            model.addAttribute("loginUser", sysLoginUser);
            model.addAttribute("hasUnreadMsg", sysMessageService.hasUnreadMsg());
            return "index.html";
        } else {
            HttpServletRequest request = HttpServletUtil.getRequest();
            String token = authService.getTokenFromRequest(request);
            if(ObjectUtil.isNotNull(token)) {
                model.addAttribute("tips", AuthExceptionEnum.LOGIN_EXPIRED.getMessage());
            }
            return "login/login.html";
        }
    }
}
