
package io.github.wujun728.sys.modular.menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.wujun728.core.pojo.node.LoginMenuTreeNode;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.sys.modular.menu.entity.SysMenu;
import io.github.wujun728.sys.modular.menu.param.SysMenuParam;
import io.github.wujun728.sys.modular.menu.node.MenuBaseTreeNode;

import java.util.List;

/**
 * 系统菜单service接口
 * @date 2020/3/13 16:05
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 获取用户权限相关信息
     *
     * @param userId 用户id
     * @param menuIdList 菜单id集合
     * @return 权限集合
     * @author xuyuxiang
     * @date 2020/3/13 16:26
     */
    List<String> getLoginPermissions(Long userId, List<Long> menuIdList);

    /**
     * 获取用户菜单相关信息，前端使用
     *
     * @param userId  用户id
     * @param appCode 应用编码
     * @param menuIdList 菜单id集合
     * @return 菜单信息结果集
     *
     * @date 2020/4/17 17:48
     */
    List<SysMenu> getLoginMenus(Long userId, String appCode, List<Long> menuIdList);

    /**
     * 获取用户菜单所属的应用编码集合
     *
     * @param userId 用户id
     * @param roleIdList 角色id集合
     * @return 用户菜单所属的应用编码集合
     * @author xuyuxiang
     * @date 2020/3/21 11:01
     */
    List<String> getUserMenuAppCodeList(Long userId, List<Long> roleIdList);

    /**
     * 系统菜单列表（树表）
     *
     * @return 菜单树表列表
     * @author xuyuxiang
     * @date 2020/3/26 10:19
     */
    PageResult<SysMenu> page();

    /**
     * 添加系统菜单
     *
     * @param sysMenuParam 添加参数
     * @author xuyuxiang
     * @date 2020/3/27 9:03
     */
    void add(SysMenuParam sysMenuParam);

    /**
     * 删除系统菜单
     *
     * @param sysMenuParamList 删除参数
     * @author xuyuxiang
     * @date 2020/3/27 9:03
     */
    void delete(List<SysMenuParam> sysMenuParamList);

    /**
     * 编辑系统菜单
     *
     * @param sysMenuParam 编辑参数
     * @author xuyuxiang
     * @date 2020/3/27 9:03
     */
    void edit(SysMenuParam sysMenuParam);

    /**
     * 查看系统菜单
     *
     * @param sysMenuParam 查看参数
     * @return 系统菜单
     * @author xuyuxiang
     * @date 2020/3/27 9:03
     */
    SysMenu detail(SysMenuParam sysMenuParam);

    /**
     * 获取系统菜单树，用于新增，编辑时选择上级节点
     *
     * @param sysMenuParam 查询参数
     * @return 菜单树列表
     * @author xuyuxiang
     * @date 2020/3/27 15:56
     */
    List<MenuBaseTreeNode> tree(SysMenuParam sysMenuParam);

    /**
     * 获取系统菜单树，用于给角色授权时选择
     *
     * @param sysMenuParam 查询参数
     * @return 菜单树列表
     * @author xuyuxiang
     * @date 2020/4/5 15:01
     */
    List<MenuBaseTreeNode> treeForGrant(SysMenuParam sysMenuParam);

    /**
     * 根据应用编码判断该机构下是否有状态为正常的菜单
     *
     * @param appCode 应用编码
     * @return 该应用下是否有正常菜单，true是，false否
     * @author xuyuxiang
     * @date 2020/6/28 12:14
     */
    boolean hasMenu(String appCode);

    /**
     * 将SysMenu格式菜单转换为LoginMenuTreeNode菜单
     *
     * @author xuyuxiang
     * @date 2021/6/24 9:57
     * @param sysMenuList 菜单列表
     * @return LoginMenuTreeNode菜单集合
     */
    List<LoginMenuTreeNode> convertSysMenuToLoginMenu(List<SysMenu> sysMenuList);
}
