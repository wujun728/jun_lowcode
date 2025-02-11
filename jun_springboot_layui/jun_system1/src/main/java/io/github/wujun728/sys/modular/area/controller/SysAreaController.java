
package io.github.wujun728.sys.modular.area.controller;

import io.github.wujun728.sys.modular.area.entity.SysArea;
import io.github.wujun728.sys.modular.area.param.SysAreaParam;
import io.github.wujun728.sys.modular.area.service.SysAreaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import io.github.wujun728.core.annotion.BusinessLog;
import io.github.wujun728.core.annotion.Permission;
import io.github.wujun728.core.enums.LogAnnotionOpTypeEnum;
import io.github.wujun728.core.pojo.page.PageResult;

import javax.annotation.Resource;

/**
 * 系统区域控制器
 * @date 2020/3/31 20:49
 */
@Controller
public class SysAreaController {

    @Resource
    private SysAreaService sysAreaService;

    /**
     * 系统区域管理页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @Permission
    @GetMapping("/sysArea/index")
    public String index() {
        return "system/sysArea/index.html";
    }

    /**
     * 系统区域选择页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @Permission
    @GetMapping("/sysArea/select")
    public String select() {
        return "system/sysArea/select.html";
    }

    /**
     * 系统区域列表（树）
     *
     * @author xuyuxiang
     * @date 2020/3/20 21:23
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysArea/page")
    @BusinessLog(title = "系统区域_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<SysArea> page(SysAreaParam sysAreaParam) {
        return sysAreaService.page(sysAreaParam);
    }
}
