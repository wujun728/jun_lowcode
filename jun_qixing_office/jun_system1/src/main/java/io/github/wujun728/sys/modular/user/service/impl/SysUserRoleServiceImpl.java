
package io.github.wujun728.sys.modular.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import io.github.wujun728.sys.modular.role.service.SysRoleService;
import io.github.wujun728.sys.modular.user.entity.SysUserRole;
import io.github.wujun728.sys.modular.user.mapper.SysUserRoleMapper;
import io.github.wujun728.sys.modular.user.param.SysUserParam;
import io.github.wujun728.sys.modular.user.service.SysUserRoleService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统用户角色service接口实现类
 * @date 2020/3/13 15:48
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Resource
    private SysRoleService sysRoleService;

    @Override
    public List<Long> getUserRoleIdList(Long userId) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, userId);
        return this.list(queryWrapper).stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
    }

    @Override
    public void grantRole(SysUserParam sysUserParam) {
        Long userId = sysUserParam.getId();
        //删除所拥有角色
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, userId);
        this.remove(queryWrapper);
        //授权角色
        sysUserParam.getGrantRoleIdList().forEach(roleId -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(roleId);
            this.save(sysUserRole);
        });
    }

    @Override
    public List<Long> getUserRoleDataScopeIdList(Long userId, Long orgId) {
        List<Long> roleIdList = CollectionUtil.newArrayList();

        // 获取用户所有角色
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, userId);
        this.list(queryWrapper).forEach(sysUserRole -> roleIdList.add(sysUserRole.getRoleId()));

        // 获取这些角色对应的数据范围
        if (ObjectUtil.isNotEmpty(roleIdList)) {
            return sysRoleService.getUserDataScopeIdList(roleIdList, orgId);
        }
        return CollectionUtil.newArrayList();
    }

    @Override
    public void deleteUserRoleListByRoleId(Long roleId) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getRoleId, roleId);
        this.remove(queryWrapper);
    }

    @Override
    public void deleteUserRoleListByUserId(Long userId) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, userId);
        this.remove(queryWrapper);
    }
}
