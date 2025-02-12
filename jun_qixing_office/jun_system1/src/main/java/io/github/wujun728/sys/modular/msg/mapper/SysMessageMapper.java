
package io.github.wujun728.sys.modular.msg.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.wujun728.sys.modular.msg.entity.SysMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统消息Mapper接口
 * @date 2021-01-21 17:50:51
 */
public interface SysMessageMapper extends BaseMapper<SysMessage> {

    List<SysMessage> list(@Param("ew") QueryWrapper queryWrapper);
}
