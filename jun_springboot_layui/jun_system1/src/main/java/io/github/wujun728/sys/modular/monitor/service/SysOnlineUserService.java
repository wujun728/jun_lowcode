
package io.github.wujun728.sys.modular.monitor.service;

import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.sys.modular.monitor.param.SysOnlineUserParam;
import io.github.wujun728.sys.modular.monitor.result.SysOnlineUserResult;

/**
 * 系统在线用户service接口
 * @date 2020/4/7 17:06
 */
public interface SysOnlineUserService {

    /**
     * 系统在线用户分页查询
     *
     * @param sysOnlineUserParam 查询参数
     * @return 查询分页结果
     * @author xuyuxiang
     * @date 2020/4/7 17:09
     */
    PageResult<SysOnlineUserResult> page(SysOnlineUserParam sysOnlineUserParam);

    /**
     * 系统在线用户强退
     *
     * @param sysOnlineUserParam 操作参数
     * @author xuyuxiang
     * @date 2020/4/7 20:20
     */
    void forceExist(SysOnlineUserParam sysOnlineUserParam);
}
