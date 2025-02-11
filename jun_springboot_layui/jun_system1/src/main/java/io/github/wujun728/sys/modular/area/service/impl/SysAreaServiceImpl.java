
package io.github.wujun728.sys.modular.area.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.sys.modular.area.entity.SysArea;
import io.github.wujun728.sys.modular.area.mapper.SysAreaMapper;
import io.github.wujun728.sys.modular.area.param.SysAreaParam;
import io.github.wujun728.sys.modular.area.service.SysAreaService;

/**
 * 系统区域service接口实现类
 * @date 2020/3/13 16:11
 */
@Service
public class SysAreaServiceImpl extends ServiceImpl<SysAreaMapper, SysArea> implements SysAreaService {

    @Override
    public PageResult<SysArea> page(SysAreaParam sysAreaParam) {
        QueryWrapper<SysArea> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isNotNull(sysAreaParam)) {
            if(ObjectUtil.isNotEmpty(sysAreaParam.getParentCode())) {
                queryWrapper.lambda().eq(SysArea::getParentCode, sysAreaParam.getParentCode());
            } else {
                queryWrapper.lambda().eq(SysArea::getParentCode, "0");
            }
        }
        //处理结果
        PageResult<SysArea> pageResult = new PageResult<>();
        pageResult.setData(this.baseMapper.list(queryWrapper));
        return pageResult;
    }
}
