
package io.github.wujun728.sys.modular.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.sys.modular.log.entity.SysOpLog;
import io.github.wujun728.sys.modular.log.param.SysOpLogParam;

/**
 * 系统操作日志service接口
 * @date 2020/3/12 14:21
 */
public interface SysOpLogService extends IService<SysOpLog> {

    /**
     * 查询系统操作日志
     *
     * @param sysOpLogParam 查询参数
     * @return 查询分页结果
     * @author xuyuxiang
     * @date 2020/3/30 10:32
     */
    PageResult<SysOpLog> page(SysOpLogParam sysOpLogParam);

    /**
     * 清空系统操作日志
     *
     * @author xuyuxiang
     * @date 2020/6/1 11:05
     */
    void delete();
}
