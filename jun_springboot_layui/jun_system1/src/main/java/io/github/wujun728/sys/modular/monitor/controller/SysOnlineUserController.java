
package io.github.wujun728.sys.modular.monitor.controller;

import io.github.wujun728.sys.modular.monitor.param.SysOnlineUserParam;
import io.github.wujun728.sys.modular.monitor.result.SysOnlineUserResult;
import io.github.wujun728.sys.modular.monitor.service.SysOnlineUserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import io.github.wujun728.core.annotion.BusinessLog;
import io.github.wujun728.core.annotion.Permission;
import io.github.wujun728.core.enums.LogAnnotionOpTypeEnum;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.core.pojo.response.ResponseData;
import io.github.wujun728.core.pojo.response.SuccessResponseData;

import javax.annotation.Resource;

/**
 * 在线用户控制器
 * @date 2020/4/7 16:57
 */
@Controller
public class SysOnlineUserController {

    @Resource
    private SysOnlineUserService sysOnlineUserService;

    /**
     * 在线用户管理页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @Permission
    @GetMapping("/sysOnlineUser/index")
    public String index() {
        return "system/sysOnlineUser/index.html";
    }

    /**
     * 在线用户分页查询
     *
     * @author xuyuxiang
     * @date 2020/4/7 16:58
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysOnlineUser/page")
    @BusinessLog(title = "系统在线用户_分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<SysOnlineUserResult> page(SysOnlineUserParam sysOnlineUserParam) {
        return sysOnlineUserService.page(sysOnlineUserParam);
    }

    /**
     * 在线用户强退
     *
     * @author xuyuxiang
     * @date 2020/4/7 17:36
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysOnlineUser/forceExist")
    @BusinessLog(title = "系统在线用户_强退", opType = LogAnnotionOpTypeEnum.FORCE)
    public ResponseData forceExist(@RequestBody @Validated(SysOnlineUserParam.force.class) SysOnlineUserParam sysOnlineUserParam) {
        sysOnlineUserService.forceExist(sysOnlineUserParam);
        return new SuccessResponseData();
    }
}
