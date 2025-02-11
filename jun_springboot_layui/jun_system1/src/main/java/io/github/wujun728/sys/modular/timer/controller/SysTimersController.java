
package io.github.wujun728.sys.modular.timer.controller;

import cn.hutool.core.lang.Dict;
import io.github.wujun728.sys.modular.timer.entity.SysTimers;
import io.github.wujun728.sys.modular.timer.param.SysTimersParam;
import io.github.wujun728.sys.modular.timer.service.SysTimersService;
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
import java.util.List;

/**
 * 定时任务控制器
 *
 * @date 2020/6/30 18:26
 */
@Controller
public class SysTimersController {

    @Resource
    private SysTimersService sysTimersService;

    /**
     * 定时任务页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @Permission
    @GetMapping("/sysTimers/index")
    public String index() {
        return "system/sysTimers/index.html";
    }

    /**
     * 定时任务表单页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @GetMapping("/sysTimers/form")
    public String form() {
        return "system/sysTimers/form.html";
    }

    /**
     * 分页查询定时任务
     *
     *
     * @date 2020/6/30 18:26
     */
    @GetMapping("/sysTimers/page")
    @ResponseBody
    @BusinessLog(title = "定时任务_分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<SysTimers> page(SysTimersParam sysTimersParam) {
        return sysTimersService.page(sysTimersParam);
    }

    /**
     * 获取全部定时任务
     *
     *
     * @date 2020/6/30 18:26
     */
    @GetMapping("/sysTimers/list")
    @ResponseBody
    @BusinessLog(title = "定时任务_查询所有", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(SysTimersParam sysTimersParam) {
        return new SuccessResponseData(sysTimersService.list(sysTimersParam));
    }

    /**
     * 查看详情定时任务
     *
     *
     * @date 2020/6/30 18:26
     */
    @GetMapping("/sysTimers/detail")
    @ResponseBody
    @BusinessLog(title = "定时任务_查看详情", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(SysTimersParam.detail.class) SysTimersParam sysTimersParam) {
        return new SuccessResponseData(sysTimersService.detail(sysTimersParam));
    }

    /**
     * 添加定时任务
     *
     *
     * @date 2020/6/30 18:26
     */
    @PostMapping("/sysTimers/add")
    @ResponseBody
    @BusinessLog(title = "定时任务_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(SysTimersParam.add.class) SysTimersParam sysTimersParam) {
        sysTimersService.add(sysTimersParam);
        return new SuccessResponseData();
    }

    /**
     * 删除定时任务
     *
     *
     * @date 2020/6/30 18:26
     */
    @PostMapping("/sysTimers/delete")
    @ResponseBody
    @BusinessLog(title = "定时任务_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(SysTimersParam.delete.class) List<SysTimersParam> sysTimersParamList) {
        sysTimersService.delete(sysTimersParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑定时任务
     *
     *
     * @date 2020/6/30 18:26
     */
    @PostMapping("/sysTimers/edit")
    @ResponseBody
    @BusinessLog(title = "定时任务_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(SysTimersParam.edit.class) SysTimersParam sysTimersParam) {
        sysTimersService.edit(sysTimersParam);
        return new SuccessResponseData();
    }

    /**
     * 获取系统的所有任务列表
     *
     *
     * @date 2020/7/1 14:34
     */
    @GetMapping("/sysTimers/getActionClasses")
    @ResponseBody
    @BusinessLog(title = "定时任务_任务列表", opType = LogAnnotionOpTypeEnum.OTHER)
    public ResponseData getActionClasses() {
        List<Dict> actionClasses = sysTimersService.getActionClasses();
        return new SuccessResponseData(actionClasses);
    }

    /**
     * 启动定时任务
     *
     *
     * @date 2020/7/1 14:34
     */
    @PostMapping("/sysTimers/start")
    @ResponseBody
    @BusinessLog(title = "定时任务_启动", opType = LogAnnotionOpTypeEnum.OTHER)
    public ResponseData start(@RequestBody @Validated(SysTimersParam.start.class) SysTimersParam sysTimersParam) {
        sysTimersService.start(sysTimersParam);
        return new SuccessResponseData();
    }

    /**
     * 停止定时任务
     *
     *
     * @date 2020/7/1 14:34
     */
    @PostMapping("/sysTimers/stop")
    @ResponseBody
    @BusinessLog(title = "定时任务_关闭", opType = LogAnnotionOpTypeEnum.OTHER)
    public ResponseData stop(@RequestBody @Validated(SysTimersParam.stop.class) SysTimersParam sysTimersParam) {
        sysTimersService.stop(sysTimersParam);
        return new SuccessResponseData();
    }

}
