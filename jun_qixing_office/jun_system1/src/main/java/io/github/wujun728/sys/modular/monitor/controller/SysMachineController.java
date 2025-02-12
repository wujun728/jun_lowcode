
package io.github.wujun728.sys.modular.monitor.controller;

import io.github.wujun728.sys.modular.monitor.service.SysMachineService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import io.github.wujun728.core.annotion.BusinessLog;
import io.github.wujun728.core.annotion.Permission;
import io.github.wujun728.core.enums.LogAnnotionOpTypeEnum;
import io.github.wujun728.core.pojo.response.ResponseData;
import io.github.wujun728.core.pojo.response.SuccessResponseData;

import javax.annotation.Resource;

/**
 * 系统属性监控控制器
 * @date 2020/6/5 14:36
 */
@Controller
public class SysMachineController {

    @Resource
    private SysMachineService sysMachineService;

    /**
     * 系统属性监控页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @Permission
    @GetMapping("/sysMachine/index")
    public String index() {
        return "system/sysMachine/index.html";
    }

    /**
     * 系统属性监控
     *
     * @author xuyuxiang
     * @date 2020/6/5 14:38
     */
    @ResponseBody
    @GetMapping("/sysMachine/query")
    @BusinessLog(title = "系统属性监控_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData query() {
        return new SuccessResponseData(sysMachineService.query());
    }
}
