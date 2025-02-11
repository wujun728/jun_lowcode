
package io.github.wujun728.sys.modular.role.controller;

import io.github.wujun728.sys.modular.role.entity.SysRole;
import io.github.wujun728.sys.modular.role.param.SysRoleParam;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import io.github.wujun728.core.annotion.BusinessLog;
import io.github.wujun728.core.annotion.DataScope;
import io.github.wujun728.core.annotion.Permission;
import io.github.wujun728.core.enums.LogAnnotionOpTypeEnum;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.core.pojo.response.ResponseData;
import io.github.wujun728.core.pojo.response.SuccessResponseData;
import io.github.wujun728.sys.modular.role.service.SysRoleService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统角色控制器
 * @date 2020/3/20 19:42
 */
@Controller
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 系统角色页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @Permission
    @GetMapping("/sysRole/index")
    public String index() {
        return "system/sysRole/index.html";
    }

    /**
     * 系统角色表单页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @GetMapping("/sysRole/form")
    public String form() {
        return "system/sysRole/form.html";
    }

    /**
     * 系统角色授权菜单页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @GetMapping("/sysRole/grantMenu")
    public String grantMenu() {
        return "system/sysRole/grantMenu.html";
    }

    /**
     * 系统角色授权数据页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @GetMapping("/sysRole/grantData")
    public String grantData() {
        return "system/sysRole/grantData.html";
    }

    /**
     * 查询系统角色
     *
     * @author xuyuxiang
     * @date 2020/3/28 14:45
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysRole/page")
    @BusinessLog(title = "系统角色_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<SysRole> page(SysRoleParam sysRoleParam) {
        return sysRoleService.page(sysRoleParam);
    }

    /**
     * 系统角色下拉（用于授权角色时选择）
     *
     * @author xuyuxiang
     * @date 2020/4/5 16:45
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysRole/dropDown")
    @BusinessLog(title = "系统角色_下拉", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData dropDown() {
        return new SuccessResponseData(sysRoleService.dropDown());
    }

    /**
     * 添加系统角色
     *
     * @author xuyuxiang
     * @date 2020/3/28 14:45
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysRole/add")
    @BusinessLog(title = "系统角色_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(SysRoleParam.add.class) SysRoleParam sysRoleParam) {
        sysRoleService.add(sysRoleParam);
        return new SuccessResponseData();
    }

    /**
     * 删除系统角色
     *
     * @author xuyuxiang
     * @date 2020/3/28 14:45
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysRole/delete")
    @BusinessLog(title = "系统角色_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(SysRoleParam.delete.class) List<SysRoleParam> sysRoleParamList) {
        sysRoleService.delete(sysRoleParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑系统角色
     *
     * @author xuyuxiang
     * @date 2020/3/28 14:46
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysRole/edit")
    @BusinessLog(title = "系统角色_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(SysRoleParam.edit.class) SysRoleParam sysRoleParam) {
        sysRoleService.edit(sysRoleParam);
        return new SuccessResponseData();
    }

    /**
     * 查看系统角色
     *
     * @author xuyuxiang
     * @date 2020/3/28 14:46
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysRole/detail")
    @BusinessLog(title = "系统角色_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(SysRoleParam.detail.class) SysRoleParam sysRoleParam) {
        return new SuccessResponseData(sysRoleService.detail(sysRoleParam));
    }

    /**
     * 授权菜单
     *
     * @author xuyuxiang
     * @date 2020/3/28 16:05
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysRole/grantMenu")
    @BusinessLog(title = "系统角色_授权菜单", opType = LogAnnotionOpTypeEnum.GRANT)
    public ResponseData grantMenu(@RequestBody @Validated(SysRoleParam.grantMenu.class) SysRoleParam sysRoleParam) {
        sysRoleService.grantMenu(sysRoleParam);
        return new SuccessResponseData();
    }

    /**
     * 授权数据
     *
     * @author xuyuxiang
     * @date 2020/3/28 16:05
     */
    @Permission
    @DataScope
    @ResponseBody
    @PostMapping("/sysRole/grantData")
    @BusinessLog(title = "系统角色_授权数据", opType = LogAnnotionOpTypeEnum.GRANT)
    public ResponseData grantData(@RequestBody @Validated(SysRoleParam.grantData.class) SysRoleParam sysRoleParam) {
        sysRoleService.grantData(sysRoleParam);
        return new SuccessResponseData();
    }

    /**
     * 拥有菜单
     *
     * @author xuyuxiang
     * @date 2020/3/28 14:46
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysRole/ownMenu")
    @BusinessLog(title = "系统角色_拥有菜单", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData ownMenu(@Validated(SysRoleParam.detail.class) SysRoleParam sysRoleParam) {
        return new SuccessResponseData(sysRoleService.ownMenu(sysRoleParam));
    }

    /**
     * 拥有数据
     *
     * @author xuyuxiang
     * @date 2020/3/28 14:46
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysRole/ownData")
    @BusinessLog(title = "系统角色_拥有数据", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData ownData(@Validated(SysRoleParam.detail.class) SysRoleParam sysRoleParam) {
        return new SuccessResponseData(sysRoleService.ownData(sysRoleParam));
    }

}
