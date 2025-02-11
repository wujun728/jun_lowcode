
package io.github.wujun728.sys.modular.dict.controller;

import io.github.wujun728.sys.modular.dict.entity.SysDictType;
import io.github.wujun728.sys.modular.dict.param.SysDictTypeParam;
import io.github.wujun728.sys.modular.dict.service.SysDictTypeService;
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
 * 系统字典类型控制器
 *，xuyuxiang
 * @date 2020/3/31 20:49
 */
@Controller
public class SysDictTypeController {

    @Resource
    private SysDictTypeService sysDictTypeService;

    /**
     * 字典类型管理页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @Permission
    @GetMapping("/sysDictType/index")
    public String index() {
        return "system/sysDictType/index.html";
    }

    /**
     * 字典类型表单页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @GetMapping("/sysDictType/form")
    public String form() {
        return "system/sysDictType/form.html";
    }

    /**
     * 分页查询系统字典类型
     *
     * @author xuyuxiang，xuyuxiang
     * @date 2020/3/31 20:30
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysDictType/page")
    @BusinessLog(title = "系统字典类型_分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<SysDictType> page(SysDictTypeParam sysDictTypeParam) {
        return sysDictTypeService.page(sysDictTypeParam);
    }

    /**
     * 获取字典类型列表
     *
     * @author xuyuxiang，xuyuxiang
     * @date 2020/3/31 21:03
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysDictType/list")
    @BusinessLog(title = "系统字典类型_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(SysDictTypeParam sysDictTypeParam) {
        return new SuccessResponseData(sysDictTypeService.list(sysDictTypeParam));
    }

    /**
     * 获取字典类型下所有字典，举例，返回格式为：[{code:"M",value:"男"},{code:"F",value:"女"}]
     *
     * @author xuyuxiang，xuyuxiang yubaoshan
     * @date 2020/3/31 21:18
     */
    @ResponseBody
    @GetMapping("/sysDictType/dropDown")
    @BusinessLog(title = "系统字典类型_下拉", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData dropDown(@Validated(SysDictTypeParam.dropDown.class) SysDictTypeParam sysDictTypeParam) {
        return new SuccessResponseData(sysDictTypeService.dropDown(sysDictTypeParam));
    }

    /**
     * 查看系统字典类型
     *
     * @author xuyuxiang，xuyuxiang
     * @date 2020/3/31 20:31
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysDictType/detail")
    @BusinessLog(title = "系统字典类型_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(SysDictTypeParam.detail.class) SysDictTypeParam sysDictTypeParam) {
        return new SuccessResponseData(sysDictTypeService.detail(sysDictTypeParam));
    }

    /**
     * 添加系统字典类型
     *
     * @author xuyuxiang，xuyuxiang
     * @date 2020/3/31 20:30
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysDictType/add")
    @BusinessLog(title = "系统字典类型_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(SysDictTypeParam.add.class) SysDictTypeParam sysDictTypeParam) {
        sysDictTypeService.add(sysDictTypeParam);
        return new SuccessResponseData();
    }

    /**
     * 删除系统字典类型
     *
     * @author xuyuxiang，xuyuxiang
     * @date 2020/3/31 20:30
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysDictType/delete")
    @BusinessLog(title = "系统字典类型_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(SysDictTypeParam.delete.class) List<SysDictTypeParam> sysDictTypeParamList) {
        sysDictTypeService.delete(sysDictTypeParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑系统字典类型
     *
     * @author xuyuxiang，xuyuxiang
     * @date 2020/3/31 20:31
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysDictType/edit")
    @BusinessLog(title = "系统字典类型_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(SysDictTypeParam.edit.class) SysDictTypeParam sysDictTypeParam) {
        sysDictTypeService.edit(sysDictTypeParam);
        return new SuccessResponseData();
    }

    /**
     * 修改状态
     *
     *
     * @date 2020/4/30 22:20
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysDictType/changeStatus")
    @BusinessLog(title = "系统字典类型_修改状态", opType = LogAnnotionOpTypeEnum.CHANGE_STATUS)
    public ResponseData changeStatus(@RequestBody @Validated(SysDictTypeParam.changeStatus.class) SysDictTypeParam sysDictTypeParam) {
        sysDictTypeService.changeStatus(sysDictTypeParam);
        return new SuccessResponseData();
    }

    /**
     * 系统字典类型与字典值构造的树
     *
     *
     * @date 2020/4/30 22:20
     */
    @ResponseBody
    @GetMapping("/sysDictType/tree")
    @BusinessLog(title = "系统字典类型_树", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData tree() {
        return new SuccessResponseData(sysDictTypeService.tree());
    }
}
