
package io.github.wujun728.sys.modular.pos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.sys.modular.pos.entity.SysPos;
import io.github.wujun728.sys.modular.pos.param.SysPosParam;

import java.util.List;

/**
 * 系统职位service接口
 * @date 2020/3/13 16:00
 */
public interface SysPosService extends IService<SysPos> {

    /**
     * 查询系统职位
     *
     * @param sysPosParam 查询参数
     * @return 查询分页结果
     * @author xuyuxiang
     * @date 2020/3/26 10:19
     */
    PageResult<SysPos> page(SysPosParam sysPosParam);

    /**
     * 系统职位列表
     *
     * @param sysPosParam 查询参数
     * @return 职位列表
     *
     * @date 2020/6/21 23:44
     */
    List<SysPos> list(SysPosParam sysPosParam);

    /**
     * 添加系统职位
     *
     * @param sysPosParam 添加参数
     * @author xuyuxiang
     * @date 2020/3/25 14:57
     */
    void add(SysPosParam sysPosParam);

    /**
     * 删除系统职位
     *
     * @param sysPosParamList 删除参数
     * @author xuyuxiang
     * @date 2020/3/25 14:57
     */
    void delete(List<SysPosParam> sysPosParamList);

    /**
     * 编辑系统职位
     *
     * @param sysPosParam 编辑参数
     * @author xuyuxiang
     * @date 2020/3/25 14:58
     */
    void edit(SysPosParam sysPosParam);

    /**
     * 查看系统职位
     *
     * @param sysPosParam 查看参数
     * @return 系统职位
     * @author xuyuxiang
     * @date 2020/3/26 9:50
     */
    SysPos detail(SysPosParam sysPosParam);
}
