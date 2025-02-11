
package io.github.wujun728.sys.modular;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import io.github.wujun728.core.exception.enums.AuthExceptionEnum;
import io.github.wujun728.core.exception.enums.PermissionExceptionEnum;

/**
 * 错误页面控制器
 * @date 2020/3/18 11:20
 */
@Controller
public class ErrorController {

    /**
     * 跳转到404页面
     *
     * @author xuyuxiang
     * @date 2020/11/6 14:24
     */
    @GetMapping("/global/error")
    public String errorPage() {
        return "redirect:/404";
    }

    /**
     * 跳转到403页面
     *
     * @author xuyuxiang
     * @date 2020/11/6 14:24
     */
    @GetMapping("/403")
    public String noPermissionPage() {
        return "error/403.html";
    }

    /**
     * 跳转到404页面
     *
     * @author xuyuxiang
     * @date 2020/11/6 14:24
     */
    @GetMapping("/404")
    public String notFoundPage() {
        return "error/404.html";
    }

    /**
     * 跳转到500页面
     *
     * @author xuyuxiang
     * @date 2020/11/6 14:24
     */
    @GetMapping("/500")
    public String serverErrorPage() {
        return "error/500.html";
    }

    /**
     * 跳转到session超时页面
     *
     * @author xuyuxiang
     * @date 2020/11/6 14:24
     */
    @GetMapping(path = "/global/sessionError")
    public String sessionError(Model model, Integer errorCode) {
        if (ObjectUtil.isNotNull(errorCode)) {
            if (errorCode.equals(PermissionExceptionEnum.NO_PERMISSION.getCode())) {
                //如果是没权限，403页面
                return "redirect:/403";
            }
            if (errorCode.equals(PermissionExceptionEnum.URL_NOT_EXIST.getCode())) {
                //如果是资源路径不存在，404页面
                return "redirect:/404";
            }
            if (errorCode.equals(AuthExceptionEnum.NOT_LOGIN.getCode())) {
                //如果需要登录，登录页
                return "redirect:/";
            }
        } else {
            //登录过期，设置登录过期的提示消息
            model.addAttribute("tips", AuthExceptionEnum.LOGIN_EXPIRED.getMessage());
            return "login/login.html";
        }
        return null;
    }
}
