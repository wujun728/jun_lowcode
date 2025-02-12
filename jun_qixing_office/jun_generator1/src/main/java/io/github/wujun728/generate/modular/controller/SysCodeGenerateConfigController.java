
package io.github.wujun728.generate.modular.controller;

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
import io.github.wujun728.generate.modular.entity.SysCodeGenerateConfig;
import io.github.wujun728.generate.modular.param.SysCodeGenerateConfigParam;
import io.github.wujun728.generate.modular.service.SysCodeGenerateConfigService;

import javax.annotation.Resource;

/**
 * 代码生成详细配置控制器
 *
 * @date 2021-02-06 20:19:49
 */
@Controller
public class SysCodeGenerateConfigController {

    private static String PATH_PREFIX = "generate/";

    @Resource
    private SysCodeGenerateConfigService sysCodeGenerateConfigService;

    /**
     * 字段配置界面
     *
     * @auther yubaoshan
     * @date 12/15/20 11:20 PM
     */
    @GetMapping("/codeGenerate/indexConfig")
    public String index() {
        return PATH_PREFIX + "indexConfig.html";
    }

    /**
     * 编辑代码生成详细配置
     *
     *
     * @date 2021-02-06 20:19:49
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysCodeGenerateConfig/edit")
    @BusinessLog(title = "代码生成详细配置_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(SysCodeGenerateConfigParam.edit.class) SysCodeGenerateConfigParam sysCodeGenerateConfigParam) {
            sysCodeGenerateConfigService.edit(sysCodeGenerateConfigParam.getSysCodeGenerateConfigParamList());
        return new SuccessResponseData();
    }

    /**
     * 查看代码生成详细配置
     *
     *
     * @date 2021-02-06 20:19:49
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysCodeGenerateConfig/detail")
    @BusinessLog(title = "代码生成详细配置_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(SysCodeGenerateConfigParam.detail.class) SysCodeGenerateConfigParam sysCodeGenerateConfigParam) {
        return new SuccessResponseData(sysCodeGenerateConfigService.detail(sysCodeGenerateConfigParam));
    }

    /**
     * 代码生成详细配置列表
     *
     *
     * @date 2021-02-06 20:19:49
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysCodeGenerateConfig/list")
    @BusinessLog(title = "代码生成详细配置_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<SysCodeGenerateConfig> list(SysCodeGenerateConfigParam sysCodeGenerateConfigParam) {
        return sysCodeGenerateConfigService.page(sysCodeGenerateConfigParam);
    }

}
