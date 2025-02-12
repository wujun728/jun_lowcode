
package io.github.wujun728.sys.modular.log.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import io.github.wujun728.core.consts.CommonConstant;
import io.github.wujun728.core.factory.PageFactory;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.sys.modular.log.entity.SysVisLog;
import io.github.wujun728.sys.modular.log.mapper.SysVisLogMapper;
import io.github.wujun728.sys.modular.log.param.SysVisLogParam;
import io.github.wujun728.sys.modular.log.service.SysVisLogService;

/**
 * 系统访问日志service接口实现类
 * @date 2020/3/12 14:23
 */
@Service
public class SysVisLogServiceImpl extends ServiceImpl<SysVisLogMapper, SysVisLog> implements SysVisLogService {

    @Override
    public PageResult<SysVisLog> page(SysVisLogParam sysVisLogParam) {
        QueryWrapper<SysVisLog> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(sysVisLogParam)) {
            //根据名称模糊查询
            if (ObjectUtil.isNotEmpty(sysVisLogParam.getName())) {
                queryWrapper.lambda().like(SysVisLog::getName, sysVisLogParam.getName());
            }
            //跟据访问类型（字典 1登入 2登出）查询
            if (ObjectUtil.isNotEmpty(sysVisLogParam.getVisType())) {
                queryWrapper.lambda().eq(SysVisLog::getVisType, sysVisLogParam.getVisType());
            }
            //根据是否成功查询
            if (ObjectUtil.isNotEmpty(sysVisLogParam.getSuccess())) {
                queryWrapper.lambda().eq(SysVisLog::getSuccess, sysVisLogParam.getSuccess());
            }
            //排序
            if(ObjectUtil.isAllNotEmpty(sysVisLogParam.getSortBy(), sysVisLogParam.getOrderBy())) {
                queryWrapper.orderBy(true, sysVisLogParam.getOrderBy().equals(CommonConstant.ASC), StrUtil.toUnderlineCase(sysVisLogParam.getSortBy()));
            } else {
                queryWrapper.lambda().orderByDesc(SysVisLog::getVisTime);
            }
            // 根据时间范围查询
            if (ObjectUtil.isAllNotEmpty(sysVisLogParam.getSearchBeginTime(), sysVisLogParam.getSearchEndTime())) {
                queryWrapper.apply("date_format (vis_time,'%Y-%m-%d') >= date_format('" + sysVisLogParam.getSearchBeginTime() + "','%Y-%m-%d')")
                        .apply("date_format (vis_time,'%Y-%m-%d') <= date_format('" + sysVisLogParam.getSearchEndTime() + "','%Y-%m-%d')");
            }
        }
        return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
    }

    @Override
    public void delete() {
        this.remove(new QueryWrapper<>());
    }
}
