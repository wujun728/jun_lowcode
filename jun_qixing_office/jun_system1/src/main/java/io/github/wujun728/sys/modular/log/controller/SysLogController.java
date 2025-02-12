
package io.github.wujun728.sys.modular.log.controller;

import io.github.wujun728.sys.modular.log.service.SysOpLogService;
import io.github.wujun728.sys.modular.log.service.SysVisLogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import io.github.wujun728.core.annotion.BusinessLog;
import io.github.wujun728.core.annotion.Permission;
import io.github.wujun728.core.enums.LogAnnotionOpTypeEnum;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.core.pojo.response.ResponseData;
import io.github.wujun728.core.pojo.response.SuccessResponseData;
import io.github.wujun728.sys.modular.log.entity.SysOpLog;
import io.github.wujun728.sys.modular.log.entity.SysVisLog;
import io.github.wujun728.sys.modular.log.param.SysOpLogParam;
import io.github.wujun728.sys.modular.log.param.SysVisLogParam;

import javax.annotation.Resource;

/**
 * 系统日志控制器
 * @date 2020/3/19 21:14
 */
@Controller
public class SysLogController {

    @Resource
    private SysVisLogService sysVisLogService;

    @Resource
    private SysOpLogService sysOpLogService;

    /**
     * 访问日志页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @Permission
    @GetMapping("/sysVisLog/index")
    public String visLogIndex() {
        return "system/sysVisLog/index.html";
    }

    /**
     * 访问日志详情页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @GetMapping("/sysVisLog/detail")
    public String visLogDetail() {
        return "system/sysVisLog/detail.html";
    }

    /**
     * 操作日志页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @Permission
    @GetMapping("/sysOpLog/index")
    public String opLogIndex() {
        return "system/sysOpLog/index.html";
    }

    /**
     * 操作日志详情页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @GetMapping("/sysOpLog/detail")
    public String opLogDetail() {
        return "system/sysOpLog/detail.html";
    }

    /**
     * 查询访问日志
     *
     * @author xuyuxiang
     * @date 2020/3/20 18:52
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysVisLog/page")
    public PageResult<SysVisLog> visLogPage(SysVisLogParam visLogParam) {
        return sysVisLogService.page(visLogParam);
    }

    /**
     * 查询操作日志
     *
     * @author xuyuxiang
     * @date 2020/3/20 18:52
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysOpLog/page")
    public PageResult<SysOpLog> opLogPage(SysOpLogParam sysOpLogParam) {
        return sysOpLogService.page(sysOpLogParam);
    }

    /**
     * 清空访问日志
     *
     * @author xuyuxiang
     * @date 2020/3/23 16:28
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysVisLog/delete")
    @BusinessLog(title = "访问日志_清空", opType = LogAnnotionOpTypeEnum.CLEAN)
    public ResponseData visLogDelete() {
        sysVisLogService.delete();
        return new SuccessResponseData();
    }

    /**
     * 清空操作日志
     *
     * @author xuyuxiang
     * @date 2020/3/23 16:28
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysOpLog/delete")
    @BusinessLog(title = "操作日志_清空", opType = LogAnnotionOpTypeEnum.CLEAN)
    public ResponseData opLogDelete() {
        sysOpLogService.delete();
        return new SuccessResponseData();
    }
}
