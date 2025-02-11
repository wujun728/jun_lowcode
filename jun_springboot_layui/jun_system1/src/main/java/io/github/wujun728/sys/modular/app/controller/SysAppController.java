
package io.github.wujun728.sys.modular.app.controller;

import io.github.wujun728.sys.modular.app.entity.SysApp;
import io.github.wujun728.sys.modular.app.param.SysAppParam;
import io.github.wujun728.sys.modular.app.service.SysAppService;
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
 * 系统应用控制器
 * @date 2020/3/20 21:25
 */
@Controller
public class SysAppController {

    @Resource
    private SysAppService sysAppService;

    /**
     * 系统应用页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @Permission
    @GetMapping("/sysApp/index")
    public String index() {
        return "system/sysApp/index.html";
    }

    /**
     * 系统应用表单页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @GetMapping("/sysApp/form")
    public String form() {
        return "system/sysApp/form.html";
    }

    /**
     * 查询系统应用
     *
     * @author xuyuxiang
     * @date 2020/3/20 21:23
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysApp/page")
    @BusinessLog(title = "系统应用_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<SysApp> page(SysAppParam sysAppParam) {
        return sysAppService.page(sysAppParam);
    }

    /**
     * 添加系统应用
     *
     * @author xuyuxiang
     * @date 2020/3/25 14:44
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysApp/add")
    @BusinessLog(title = "系统应用_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(SysAppParam.add.class) SysAppParam sysAppParam) {
        sysAppService.add(sysAppParam);
        return new SuccessResponseData();
    }

    /**
     * 删除系统应用
     *
     * @author xuyuxiang
     * @date 2020/3/25 14:54
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysApp/delete")
    @BusinessLog(title = "系统应用_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(SysAppParam.delete.class) List<SysAppParam> sysAppParamList) {
        sysAppService.delete(sysAppParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑系统应用
     *
     * @author xuyuxiang
     * @date 2020/3/25 14:54
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysApp/edit")
    @BusinessLog(title = "系统应用_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(SysAppParam.edit.class) SysAppParam sysAppParam) {
        sysAppService.edit(sysAppParam);
        return new SuccessResponseData();
    }

    /**
     * 查看系统应用
     *
     * @author xuyuxiang
     * @date 2020/3/26 9:49
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysApp/detail")
    @BusinessLog(title = "系统应用_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(SysAppParam.detail.class) SysAppParam sysAppParam) {
        return new SuccessResponseData(sysAppService.detail(sysAppParam));
    }

    /**
     * 系统应用列表
     *
     * @author xuyuxiang
     * @date 2020/4/19 14:55
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysApp/list")
    @BusinessLog(title = "系统应用_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(SysAppParam sysAppParam) {
        return new SuccessResponseData(sysAppService.list(sysAppParam));
    }

    /**
     * 设为默认应用
     *
     * @author xuyuxiang
     * @date 2020/6/29 16:49
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysApp/setAsDefault")
    @BusinessLog(title = "系统应用_设为默认应用", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData setAsDefault(@RequestBody @Validated(SysAppParam.detail.class) SysAppParam sysAppParam) {
        sysAppService.setAsDefault(sysAppParam);
        return new SuccessResponseData();
    }
}
