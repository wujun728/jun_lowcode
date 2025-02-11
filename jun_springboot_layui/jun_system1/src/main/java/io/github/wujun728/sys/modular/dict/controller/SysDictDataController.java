
package io.github.wujun728.sys.modular.dict.controller;

import io.github.wujun728.sys.modular.dict.entity.SysDictData;
import io.github.wujun728.sys.modular.dict.param.SysDictDataParam;
import io.github.wujun728.sys.modular.dict.service.SysDictDataService;
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
 * 系统字典值控制器
 * @date 2020/3/31 20:49
 */
@Controller
public class SysDictDataController {

    @Resource
    private SysDictDataService sysDictDataService;

    /**
     * 字典值管理页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @GetMapping("/sysDictData/index")
    public String index() {
        return "system/sysDictData/index.html";
    }

    /**
     * 字典值表单页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @GetMapping("/sysDictData/form")
    public String form() {
        return "system/sysDictData/form.html";
    }

    /**
     * 查询系统字典值
     *
     * @author xuyuxiang
     * @date 2020/3/31 20:50
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysDictData/page")
    @BusinessLog(title = "系统字典值_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<SysDictData> page(SysDictDataParam sysDictDataParam) {
        return sysDictDataService.page(sysDictDataParam);
    }

    /**
     * 某个字典类型下所有的字典
     *
     * @author xuyuxiang
     * @date 2020/3/31 21:03
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysDictData/list")
    @BusinessLog(title = "系统字典值_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(@Validated(SysDictDataParam.list.class) SysDictDataParam sysDictDataParam) {
        return new SuccessResponseData(sysDictDataService.list(sysDictDataParam));
    }

    /**
     * 查看系统字典值
     *
     * @author xuyuxiang
     * @date 2020/3/31 20:51
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysDictData/detail")
    @BusinessLog(title = "系统字典值_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(SysDictDataParam.detail.class) SysDictDataParam sysDictDataParam) {
        return new SuccessResponseData(sysDictDataService.detail(sysDictDataParam));
    }

    /**
     * 添加系统字典值
     *
     * @author xuyuxiang
     * @date 2020/3/31 20:50
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysDictData/add")
    @BusinessLog(title = "系统字典值_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(SysDictDataParam.add.class) SysDictDataParam sysDictDataParam) {
        sysDictDataService.add(sysDictDataParam);
        return new SuccessResponseData();
    }

    /**
     * 删除系统字典值
     *
     * @author xuyuxiang
     * @date 2020/3/31 20:50
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysDictData/delete")
    @BusinessLog(title = "系统字典值_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(SysDictDataParam.delete.class) List<SysDictDataParam> sysDictDataParamList) {
        sysDictDataService.delete(sysDictDataParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑系统字典值
     *
     * @author xuyuxiang
     * @date 2020/3/31 20:51
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysDictData/edit")
    @BusinessLog(title = "系统字典值_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(SysDictDataParam.edit.class) SysDictDataParam sysDictDataParam) {
        sysDictDataService.edit(sysDictDataParam);
        return new SuccessResponseData();
    }

    /**
     * 修改状态
     *
     *
     * @date 2020/5/1 9:43
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysDictData/changeStatus")
    @BusinessLog(title = "系统字典值_修改状态", opType = LogAnnotionOpTypeEnum.CHANGE_STATUS)
    public ResponseData changeStatus(@RequestBody @Validated(SysDictDataParam.changeStatus.class) SysDictDataParam sysDictDataParam) {
        sysDictDataService.changeStatus(sysDictDataParam);
        return new SuccessResponseData();
    }

}
