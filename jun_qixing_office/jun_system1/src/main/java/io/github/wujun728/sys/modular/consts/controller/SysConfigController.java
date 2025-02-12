
package io.github.wujun728.sys.modular.consts.controller;

import io.github.wujun728.sys.modular.consts.entity.SysConfig;
import io.github.wujun728.sys.modular.consts.param.SysConfigParam;
import io.github.wujun728.sys.modular.consts.service.SysConfigService;
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
 * 参数配置控制器
 *
 * @date 2020/4/13 22:46
 */
@Controller
public class SysConfigController {

    @Resource
    private SysConfigService sysConfigService;

    /**
     * 系统配置页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @Permission
    @GetMapping("/sysConfig/index")
    public String index() {
        return "system/sysConfig/index.html";
    }

    /**
     * 系统配置表单页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @GetMapping("/sysConfig/form")
    public String form() {
        return "system/sysConfig/form.html";
    }

    /**
     * 分页查询配置列表
     *
     * @author xuyuxiang
     * @date 2020/4/14 11:10
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysConfig/page")
    @BusinessLog(title = "系统参数配置_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<SysConfig> page(SysConfigParam sysConfigParam) {
        return sysConfigService.page(sysConfigParam);
    }

    /**
     * 系统参数配置列表
     *
     * @author xuyuxiang
     * @date 2020/4/14 11:10
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysConfig/list")
    @BusinessLog(title = "系统参数配置_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(SysConfigParam sysConfigParam) {
        return new SuccessResponseData(sysConfigService.list(sysConfigParam));
    }

    /**
     * 查看系统参数配置
     *
     * @author xuyuxiang
     * @date 2020/4/14 11:12
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysConfig/detail")
    @BusinessLog(title = "系统参数配置_详情", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(SysConfigParam.detail.class) SysConfigParam sysConfigParam) {
        return new SuccessResponseData(sysConfigService.detail(sysConfigParam));
    }

    /**
     * 添加系统参数配置
     *
     * @author xuyuxiang
     * @date 2020/4/14 11:11
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysConfig/add")
    @BusinessLog(title = "系统参数配置_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(SysConfigParam.add.class) SysConfigParam sysConfigParam) {
        sysConfigService.add(sysConfigParam);
        return new SuccessResponseData();
    }

    /**
     * 删除系统参数配置
     *
     * @author xuyuxiang
     * @date 2020/4/14 11:11
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysConfig/delete")
    @BusinessLog(title = "系统参数配置_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(SysConfigParam.delete.class) List<SysConfigParam> sysConfigParamList) {
        sysConfigService.delete(sysConfigParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑系统参数配置
     *
     * @author xuyuxiang
     * @date 2020/4/14 11:11
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysConfig/edit")
    @BusinessLog(title = "系统参数配置_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(SysConfigParam.edit.class) SysConfigParam sysConfigParam) {
        sysConfigService.edit(sysConfigParam);
        return new SuccessResponseData();
    }

}


