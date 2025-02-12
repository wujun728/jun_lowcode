
package io.github.wujun728.sys.modular.pos.controller;

import io.github.wujun728.sys.modular.pos.service.SysPosService;
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
import io.github.wujun728.sys.modular.pos.entity.SysPos;
import io.github.wujun728.sys.modular.pos.param.SysPosParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统职位控制器
 * @date 2020/3/20 19:44
 */
@Controller
public class SysPosController {

    @Resource
    private SysPosService sysPosService;

    /**
     * 系统职位页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @Permission
    @GetMapping("/sysPos/index")
    public String index() {
        return "system/sysPos/index.html";
    }

    /**
     * 系统职位表单页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @GetMapping("/sysPos/form")
    public String form() {
        return "system/sysPos/form.html";
    }

    /**
     * 查询系统职位
     *
     * @author xuyuxiang
     * @date 2020/3/26 10:20
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysPos/page")
    @BusinessLog(title = "系统职位_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<SysPos> page(SysPosParam sysPosParam) {
        return sysPosService.page(sysPosParam);
    }

    /**
     * 系统职位列表
     *
     *
     * @date 2020/6/21 23:38
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysPos/list")
    @BusinessLog(title = "系统职位_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(SysPosParam sysPosParam) {
        return new SuccessResponseData(sysPosService.list(sysPosParam));
    }

    /**
     * 添加系统职位
     *
     * @author xuyuxiang
     * @date 2020/3/26 19:03
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysPos/add")
    @BusinessLog(title = "系统职位_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(SysPosParam.add.class) SysPosParam sysPosParam) {
        sysPosService.add(sysPosParam);
        return new SuccessResponseData();
    }

    /**
     * 删除系统职位
     *
     * @author xuyuxiang
     * @date 2020/3/25 14:54
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysPos/delete")
    @BusinessLog(title = "系统职位_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(SysPosParam.delete.class) List<SysPosParam> sysPosParamList) {
        sysPosService.delete(sysPosParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑系统职位
     *
     * @author xuyuxiang
     * @date 2020/3/25 14:54
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysPos/edit")
    @BusinessLog(title = "系统职位_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(SysPosParam.edit.class) SysPosParam sysPosParam) {
        sysPosService.edit(sysPosParam);
        return new SuccessResponseData();
    }

    /**
     * 查看系统职位
     *
     * @author xuyuxiang
     * @date 2020/3/26 9:49
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysPos/detail")
    @BusinessLog(title = "系统职位_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(SysPosParam.detail.class) SysPosParam sysPosParam) {
        return new SuccessResponseData(sysPosService.detail(sysPosParam));
    }
}
