
package io.github.wujun728.sys.modular.timer.service;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.sys.modular.timer.entity.SysTimers;
import io.github.wujun728.sys.modular.timer.param.SysTimersParam;

import java.util.List;

/**
 * 定时任务 服务类
 *
 * @date 2020/6/30 18:26
 */
public interface SysTimersService extends IService<SysTimers> {

    /**
     * 分页查询定时任务
     *
     * @param sysTimersParam 查询参数
     * @return 查询分页结果
     *
     * @date 2020/6/30 18:26
     */
    PageResult<SysTimers> page(SysTimersParam sysTimersParam);

    /**
     * 查询所有定时任务
     *
     * @param sysTimersParam 查询参数
     * @return 定时任务列表
     *
     * @date 2020/6/30 18:26
     */
    List<SysTimers> list(SysTimersParam sysTimersParam);

    /**
     * 添加定时任务
     *
     * @param sysTimersParam 添加参数
     *
     * @date 2020/6/30 18:26
     */
    void add(SysTimersParam sysTimersParam);

    /**
     * 删除定时任务
     *
     * @param sysTimersParamList 删除参数
     *
     * @date 2020/6/30 18:26
     */
    void delete(List<SysTimersParam> sysTimersParamList);

    /**
     * 编辑定时任务
     *
     * @param sysTimersParam 编辑参数
     *
     * @date 2020/6/30 18:26
     */
    void edit(SysTimersParam sysTimersParam);

    /**
     * 查看详情定时任务
     *
     * @param sysTimersParam 查看参数
     * @return 定时任务
     *
     * @date 2020/6/30 18:26
     */
    SysTimers detail(SysTimersParam sysTimersParam);

    /**
     * 启动任务
     *
     * @param sysTimersParam 启动参数
     *
     * @date 2020/7/1 14:36
     */
    void start(SysTimersParam sysTimersParam);

    /**
     * 停止任务
     *
     * @param sysTimersParam 停止参数
     *
     * @date 2020/7/1 14:36
     */
    void stop(SysTimersParam sysTimersParam);

    /**
     * 获取所有可执行的任务列表
     *
     * @return TimerTaskRunner的所有子类名称集合，格式[{code:"a",value:"a"},{code:"b",value:"b"}]
     *
     * @date 2020/7/1 14:36
     */
    List<Dict> getActionClasses();

}
