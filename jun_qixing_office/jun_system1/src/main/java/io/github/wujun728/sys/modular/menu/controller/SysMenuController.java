
package io.github.wujun728.sys.modular.menu.controller;

import io.github.wujun728.core.pojo.base.param.BaseParam;
import io.github.wujun728.sys.modular.menu.entity.SysMenu;
import io.github.wujun728.sys.modular.menu.param.SysMenuParam;
import io.github.wujun728.sys.modular.menu.service.SysMenuService;
import io.github.wujun728.sys.modular.user.service.SysUserRoleService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import io.github.wujun728.sys.modular.role.service.SysRoleMenuService;
import io.github.wujun728.core.annotion.BusinessLog;
import io.github.wujun728.core.annotion.Permission;
import io.github.wujun728.core.context.login.LoginContextHolder;
import io.github.wujun728.core.enums.LogAnnotionOpTypeEnum;
import io.github.wujun728.core.pojo.node.LoginMenuTreeNode;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.core.pojo.response.ResponseData;
import io.github.wujun728.core.pojo.response.SuccessResponseData;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统菜单控制器
 * @date 2020/3/20 18:54
 */
@Controller
public class SysMenuController {

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    /**
     * 系统菜单页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @Permission
    @GetMapping("/sysMenu/index")
    public String index() {
        return "system/sysMenu/index.html";
    }

    /**
     * 系统菜单表单页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @GetMapping("/sysMenu/form")
    public String form() {
        return "system/sysMenu/form.html";
    }

    /**
     * 系统菜单列表（树）
     *
     * @author xuyuxiang
     * @date 2020/3/20 21:23
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysMenu/page")
    @BusinessLog(title = "系统菜单_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<SysMenu> page() {
        return sysMenuService.page();
    }

    /**
     * 添加系统菜单
     *
     * @author xuyuxiang
     * @date 2020/3/27 8:57
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysMenu/add")
    @BusinessLog(title = "系统菜单_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(BaseParam.add.class) SysMenuParam sysMenuParam) {
        sysMenuService.add(sysMenuParam);
        return new SuccessResponseData();
    }

    /**
     * 删除系统菜单
     *
     * @author xuyuxiang
     * @date 2020/3/27 8:58
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysMenu/delete")
    @BusinessLog(title = "系统菜单_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(SysMenuParam.delete.class) List<SysMenuParam> sysMenuParamList) {
        sysMenuService.delete(sysMenuParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑系统菜单
     *
     * @author xuyuxiang
     * @date 2020/3/27 8:59
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysMenu/edit")
    @BusinessLog(title = "系统菜单_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(SysMenuParam.edit.class) SysMenuParam sysMenuParam) {
        sysMenuService.edit(sysMenuParam);
        return new SuccessResponseData();
    }

    /**
     * 查看系统菜单
     *
     * @author xuyuxiang
     * @date 2020/3/27 9:01
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysMenu/detail")
    @BusinessLog(title = "系统菜单_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(SysMenuParam.detail.class) SysMenuParam sysMenuParam) {
        return new SuccessResponseData(sysMenuService.detail(sysMenuParam));
    }

    /**
     * 获取系统菜单树，用于新增，编辑时选择上级节点
     *
     * @author xuyuxiang
     * @date 2020/3/27 15:55
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysMenu/tree")
    @BusinessLog(title = "系统菜单_树", opType = LogAnnotionOpTypeEnum.TREE)
    public ResponseData tree(SysMenuParam sysMenuParam) {
        return new SuccessResponseData(sysMenuService.tree(sysMenuParam));
    }

    /**
     * 获取系统菜单树，用于给角色授权时选择
     *
     * @author xuyuxiang
     * @date 2020/4/5 15:00
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysMenu/treeForGrant")
    @BusinessLog(title = "系统菜单_授权树", opType = LogAnnotionOpTypeEnum.TREE)
    public ResponseData treeForGrant(SysMenuParam sysMenuParam) {
        return new SuccessResponseData(sysMenuService.treeForGrant(sysMenuParam));
    }

    /**
     * 根据系统切换菜单
     *
     * @author xuyuxiang
     * @date 2020/4/19 15:50
     */
    @ResponseBody
    @PostMapping("/sysMenu/change")
    @BusinessLog(title = "系统菜单_切换", opType = LogAnnotionOpTypeEnum.TREE)
    public ResponseData change(@RequestBody @Validated(SysMenuParam.change.class) SysMenuParam sysMenuParam) {
        Long sysLoginUserId = LoginContextHolder.me().getSysLoginUserId();
        List<Long> userRoleIdList = sysUserRoleService.getUserRoleIdList(sysLoginUserId);
        List<Long> menuIdList = sysRoleMenuService.getRoleMenuIdList(userRoleIdList);
        //转换成登录菜单
        List<SysMenu> sysMenuList = sysMenuService.getLoginMenus(sysLoginUserId, sysMenuParam.getApplication(), menuIdList);
        List<LoginMenuTreeNode> menuTreeNodeList = sysMenuService.convertSysMenuToLoginMenu(sysMenuList);
        return new SuccessResponseData(menuTreeNodeList);
    }
}
