
package io.github.wujun728.sys.modular.area.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.wujun728.sys.modular.area.entity.SysArea;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统区域mapper接口
 * @date 2020/3/13 16:12
 */
public interface SysAreaMapper extends BaseMapper<SysArea> {

    /**
     * 查询树表
     *
     * @param queryWrapper 查询条件
     * @return 树表
     * @author xuyuxiang
     * @date 2021/3/11 12:07
     */
    List<SysArea> list(@Param("ew") QueryWrapper queryWrapper);
}