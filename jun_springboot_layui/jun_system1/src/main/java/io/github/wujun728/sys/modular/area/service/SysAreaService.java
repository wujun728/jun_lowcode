
package io.github.wujun728.sys.modular.area.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.sys.modular.area.entity.SysArea;
import io.github.wujun728.sys.modular.area.param.SysAreaParam;

/**
 * 系统字典值service接口
 * @date 2020/3/13 16:10
 */
public interface SysAreaService extends IService<SysArea> {

    /**
     * 系统区域列表（树表）
     *
     * @param sysAreaParam 查询参数
     * @return 区域树表列表
     * @author xuyuxiang
     * @date 2020/3/26 10:19
     */
    PageResult<SysArea> page(SysAreaParam sysAreaParam);
}
