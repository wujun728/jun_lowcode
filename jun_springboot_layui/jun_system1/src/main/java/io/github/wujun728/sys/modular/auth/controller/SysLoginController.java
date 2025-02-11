
package io.github.wujun728.sys.modular.auth.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.URLUtil;
import io.github.wujun728.sys.modular.auth.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import io.github.wujun728.core.consts.CommonConstant;
import io.github.wujun728.core.context.constant.ConstantContextHolder;
import io.github.wujun728.core.context.login.LoginContextHolder;
import io.github.wujun728.core.exception.AuthException;
import io.github.wujun728.core.exception.enums.AuthExceptionEnum;
import io.github.wujun728.core.pojo.response.ResponseData;
import io.github.wujun728.core.pojo.response.SuccessResponseData;
import io.github.wujun728.core.util.HttpServletUtil;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录控制器
 * @date 2020/3/11 12:20
 */
@Controller
public class SysLoginController {

    @Resource
    private AuthService authService;

    /**
     * 获取是否开启租户的标识
     *
     * @author xuyuxiang
     * @date 2020/9/4
     */
    @GetMapping("/getTenantOpen")
    public ResponseData getTenantOpen() {
        return new SuccessResponseData(ConstantContextHolder.getTenantOpenFlag());
    }

    /**
     * 登录页面，GET请求
     *
     * @author xuyuxiang
     * @date 2020/11/17 10:09
     */
    @GetMapping("/login")
    public String loginPage() {
        //判断是否登录
        boolean hasLogin = LoginContextHolder.me().hasLogin();
        if(hasLogin) {
            return "redirect:/";
        } else {
            return "login/login.html";
        }
    }

    /**
     * 账号密码登录接口，POST请求
     *
     * @author xuyuxiang
     * @date 2020/3/11 15:52
     */
    @ResponseBody
    @PostMapping("/login")
    public ResponseData login(@RequestBody Dict dict) {
        String account = dict.getStr("account");
        String password = dict.getStr("password");
        String tenantCode = dict.getStr("tenantCode");
        String captcha = dict.getStr("captcha");
        //如果系统开启了多租户开关，则添加租户的临时缓存
        if (ConstantContextHolder.getTenantOpenFlag()) {
            authService.cacheTenantInfo(tenantCode);
        }
        //如果开启了验证码，则先校验验证码
        if (ConstantContextHolder.getCaptchaFlag()) {
            String sessionCaptcha = Convert.toStr(HttpServletUtil.getRequest().getSession().getAttribute(CommonConstant.CAPTCHA_SESSION_KEY));
            if (ObjectUtil.isEmpty(captcha) || !captcha.equalsIgnoreCase(sessionCaptcha)) {
                throw new AuthException(AuthExceptionEnum.VALID_CODE_ERROR);
            }
            HttpServletUtil.getRequest().getSession().removeAttribute(CommonConstant.CAPTCHA_SESSION_KEY);
        }
        String token = authService.login(account, password);
        return new SuccessResponseData(token);
    }

    /**
     * 退出登录页面，GET请求
     *
     * @author xuyuxiang
     * @date 2020/3/16 15:02
     */
    @GetMapping("/logout")
    public String logoutPage() {
        authService.logout();
        return "redirect:/login";
    }

    /**
     * 退出登录接口，POST请求
     *
     * @author xuyuxiang
     * @date 2020/3/16 15:02
     */
    @ResponseBody
    @PostMapping("/logout")
    public ResponseData logout() {
        authService.logout();
        return new SuccessResponseData();
    }

    /**
     * 获取当前登录用户信息
     *
     * @author xuyuxiang
     * @date 2020/3/23 17:57
     */
    @ResponseBody
    @GetMapping("/getLoginUser")
    public ResponseData getLoginUser() {
        return new SuccessResponseData(LoginContextHolder.me().getSysLoginUserUpToDate());
    }

    /**
     * 获取验证码
     *
     * @author xuyuxiang
     * @date 2020/3/23 17:57
     */
    @ResponseBody
    @GetMapping("/getCaptcha")
    public void getCaptcha() throws IOException {
        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(300, 100, 4, 4);
        HttpSession session = HttpServletUtil.getRequest().getSession();
        session.setAttribute(CommonConstant.CAPTCHA_SESSION_KEY, captcha.getCode());
        HttpServletResponse response = HttpServletUtil.getResponse();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLUtil.encode("captcha.jpg") + "\"");
        response.addHeader("Content-Length", "" + captcha.getImageBytes().length);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setContentType("application/octet-stream;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        captcha.write(outputStream);
    }
}
