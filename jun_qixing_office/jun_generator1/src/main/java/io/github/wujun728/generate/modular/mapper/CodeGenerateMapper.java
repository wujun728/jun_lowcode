
package io.github.wujun728.generate.modular.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.wujun728.generate.modular.entity.CodeGenerate;
import org.apache.ibatis.annotations.Param;
import io.github.wujun728.generate.modular.result.InforMationColumnsResult;
import io.github.wujun728.generate.modular.result.InformationResult;

import java.util.List;

/**
 * 代码生成基础配置
 *
 * @date 2020年12月16日21:07:28
 */
public interface CodeGenerateMapper extends BaseMapper<CodeGenerate> {

    /**
     * 查询指定库中所有表
     *
     *
     * @date 2020年12月17日20:06:05
     */
    List<InformationResult> selectInformationTable(@Param("dbName") String dbName);

    /**
     * 查询指定表中所有字段
     *
     *
     * @date 2020年12月17日20:06:05
     */
    List<InforMationColumnsResult> selectInformationColumns(@Param("dbName") String dbName, @Param("tableName") String tableName);
}
