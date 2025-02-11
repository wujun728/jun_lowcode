
package io.github.wujun728.sys.modular.consts.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.sys.modular.consts.entity.SysConfig;
import io.github.wujun728.sys.modular.consts.param.SysConfigParam;

import java.util.List;

/**
 * 系统参数配置service接口
 * @date 2020/4/14 11:14
 */
public interface SysConfigService extends IService<SysConfig> {

    /**
     * 查询系统参数配置
     *
     * @param sysConfigParam 查询参数
     * @return 查询分页结果
     * @author xuyuxiang
     * @date 2020/4/14 11:14
     */
    PageResult<SysConfig> page(SysConfigParam sysConfigParam);

    /**
     * 查询系统参数配置
     *
     * @param sysConfigParam 查询参数
     * @return 系统参数配置列表
     * @author xuyuxiang
     * @date 2020/4/14 11:14
     */
    List<SysConfig> list(SysConfigParam sysConfigParam);

    /**
     * 查看系统参数配置
     *
     * @param sysConfigParam 查看参数
     * @return 系统参数配置
     * @author xuyuxiang
     * @date 2020/4/14 11:15
     */
    SysConfig detail(SysConfigParam sysConfigParam);

    /**
     * 添加系统参数配置
     *
     * @param sysConfigParam 添加参数
     * @author xuyuxiang
     * @date 2020/4/14 11:14
     */
    void add(SysConfigParam sysConfigParam);

    /**
     * 删除系统参数配置
     *
     * @param sysConfigParamList 删除参数
     * @author xuyuxiang
     * @date 2020/4/14 11:15
     */
    void delete(List<SysConfigParam> sysConfigParamList);

    /**
     * 编辑系统参数配置
     *
     * @param sysConfigParam 编辑参数
     * @author xuyuxiang
     * @date 2020/4/14 11:15
     */
    void edit(SysConfigParam sysConfigParam);

}
