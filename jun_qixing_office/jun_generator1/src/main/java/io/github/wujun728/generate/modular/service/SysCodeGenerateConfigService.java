
package io.github.wujun728.generate.modular.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.generate.modular.entity.CodeGenerate;
import io.github.wujun728.generate.modular.entity.SysCodeGenerateConfig;
import io.github.wujun728.generate.modular.param.SysCodeGenerateConfigParam;
import io.github.wujun728.generate.modular.result.InforMationColumnsResult;

import java.util.List;

/**
 * 代码生成详细配置service接口
 *
 * @date 2021-02-06 20:19:49
 */
public interface SysCodeGenerateConfigService extends IService<SysCodeGenerateConfig> {

    /**
     * 代码生成详细配置列表
     *
     *
     * @date 2021-02-06 20:19:49
     */
    PageResult<SysCodeGenerateConfig> page(SysCodeGenerateConfigParam sysCodeGenerateConfigParam);

    /**
     * 代码生成详细配置列表
     *
     *
     * @date 2021-02-06 20:19:49
     */
    List<SysCodeGenerateConfig> list(SysCodeGenerateConfigParam sysCodeGenerateConfigParam);

    /**
     * 添加代码生成详细配置
     *
     *
     * @date 2021-02-06 20:19:49
     */
    void add(SysCodeGenerateConfigParam sysCodeGenerateConfigParam);

    /**
     * 添加代码生成详细配置列表
     *
     *
     * @date 2021-02-06 20:19:49
     */
    void addList(List<InforMationColumnsResult> inforMationColumnsResultList, CodeGenerate codeGenerate);

    /**
     * 删除代码生成详细配置
     *
     *
     * @date 2021-02-06 20:19:49
     */
    void delete(SysCodeGenerateConfigParam sysCodeGenerateConfigParam);

    /**
     * 编辑代码生成详细配置
     *
     *
     * @date 2021-02-06 20:19:49
     */
    void edit(List<SysCodeGenerateConfigParam> sysCodeGenerateConfigParamList);

    /**
     * 查看代码生成详细配置
     *
     *
     * @date 2021-02-06 20:19:49
     */
     SysCodeGenerateConfig detail(SysCodeGenerateConfigParam sysCodeGenerateConfigParam);
}
