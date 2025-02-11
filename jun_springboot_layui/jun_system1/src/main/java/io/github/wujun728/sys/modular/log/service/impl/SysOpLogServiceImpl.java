
package io.github.wujun728.sys.modular.log.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import io.github.wujun728.core.consts.CommonConstant;
import io.github.wujun728.core.factory.PageFactory;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.sys.modular.log.entity.SysOpLog;
import io.github.wujun728.sys.modular.log.mapper.SysOpLogMapper;
import io.github.wujun728.sys.modular.log.param.SysOpLogParam;
import io.github.wujun728.sys.modular.log.service.SysOpLogService;

/**
 * 系统操作日志service接口实现类
 * @date 2020/3/12 14:22
 */
@Service
public class SysOpLogServiceImpl extends ServiceImpl<SysOpLogMapper, SysOpLog> implements SysOpLogService {

    @Override
    public PageResult<SysOpLog> page(SysOpLogParam sysOpLogParam) {
        QueryWrapper<SysOpLog> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(sysOpLogParam)) {
            //根据名称模糊查询
            if (ObjectUtil.isNotEmpty(sysOpLogParam.getName())) {
                queryWrapper.lambda().like(SysOpLog::getName, sysOpLogParam.getName());
            }
            //根据操作类型查询
            if (ObjectUtil.isNotEmpty(sysOpLogParam.getOpType())) {
                queryWrapper.lambda().eq(SysOpLog::getOpType, sysOpLogParam.getOpType());
            }
            //根据是否成功查询
            if (ObjectUtil.isNotEmpty(sysOpLogParam.getSuccess())) {
                queryWrapper.lambda().eq(SysOpLog::getSuccess, sysOpLogParam.getSuccess());
            }
            //排序
            if(ObjectUtil.isAllNotEmpty(sysOpLogParam.getSortBy(), sysOpLogParam.getOrderBy())) {
                queryWrapper.orderBy(true, sysOpLogParam.getOrderBy().equals(CommonConstant.ASC), StrUtil.toUnderlineCase(sysOpLogParam.getSortBy()));
            } else {
                queryWrapper.lambda().orderByDesc(SysOpLog::getOpTime);
            }
            // 根据时间范围查询
            if (ObjectUtil.isAllNotEmpty(sysOpLogParam.getSearchBeginTime(), sysOpLogParam.getSearchEndTime())) {
                queryWrapper.apply("date_format (op_time,'%Y-%m-%d') >= date_format('" + sysOpLogParam.getSearchBeginTime() + "','%Y-%m-%d')")
                        .apply("date_format (op_time,'%Y-%m-%d') <= date_format('" + sysOpLogParam.getSearchEndTime() + "','%Y-%m-%d')");
            }
        }
        Page<SysOpLog> page = this.page(PageFactory.defaultPage(), queryWrapper);
        return new PageResult<>(page);
    }

    @Override
    public void delete() {
        this.remove(new QueryWrapper<>());
    }
}
