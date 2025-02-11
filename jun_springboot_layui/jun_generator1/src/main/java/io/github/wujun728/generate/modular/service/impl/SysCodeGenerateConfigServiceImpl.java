
package io.github.wujun728.generate.modular.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.wujun728.core.enums.YesOrNotEnum;
import io.github.wujun728.core.exception.ServiceException;
import io.github.wujun728.core.factory.PageFactory;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.generate.core.consts.GenConstant;
import io.github.wujun728.generate.core.enums.QueryTypeEnum;
import io.github.wujun728.generate.core.enums.TableFilteredFieldsEnum;
import io.github.wujun728.generate.core.tool.JavaEffTool;
import io.github.wujun728.generate.core.tool.JavaSqlTool;
import io.github.wujun728.generate.core.tool.NamingConTool;
import io.github.wujun728.generate.modular.entity.CodeGenerate;
import io.github.wujun728.generate.modular.entity.SysCodeGenerateConfig;
import io.github.wujun728.generate.modular.enums.SysCodeGenerateConfigExceptionEnum;
import io.github.wujun728.generate.modular.mapper.SysCodeGenerateConfigMapper;
import io.github.wujun728.generate.modular.param.SysCodeGenerateConfigParam;
import io.github.wujun728.generate.modular.result.InforMationColumnsResult;
import io.github.wujun728.generate.modular.service.SysCodeGenerateConfigService;

import java.util.List;

/**
 * 代码生成详细配置service接口实现类
 *
 * @date 2021-02-06 20:19:49
 */
@Service
public class SysCodeGenerateConfigServiceImpl extends ServiceImpl<SysCodeGenerateConfigMapper, SysCodeGenerateConfig> implements SysCodeGenerateConfigService {

    @Override
    public PageResult<SysCodeGenerateConfig> page(SysCodeGenerateConfigParam sysCodeGenerateConfigParam) {
        return new PageResult<>(this.page(PageFactory.defaultPage(), QueryWrapper(sysCodeGenerateConfigParam)));
    }

    @Override
    public List<SysCodeGenerateConfig> list(SysCodeGenerateConfigParam sysCodeGenerateConfigParam) {
        return this.list(QueryWrapper(sysCodeGenerateConfigParam));
    }

    private LambdaQueryWrapper<SysCodeGenerateConfig> QueryWrapper (SysCodeGenerateConfigParam sysCodeGenerateConfigParam) {
        LambdaQueryWrapper<SysCodeGenerateConfig> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(sysCodeGenerateConfigParam)) {

            // 根据代码生成主表ID 精准查询
            if (ObjectUtil.isNotEmpty(sysCodeGenerateConfigParam.getCodeGenId())) {
                queryWrapper.eq(SysCodeGenerateConfig::getCodeGenId, sysCodeGenerateConfigParam.getCodeGenId());
            }
        }
        return queryWrapper;
    }

    @Override
    public void add(SysCodeGenerateConfigParam sysCodeGenerateConfigParam) {
        SysCodeGenerateConfig sysCodeGenerateConfig = new SysCodeGenerateConfig();
        BeanUtil.copyProperties(sysCodeGenerateConfigParam, sysCodeGenerateConfig);
        this.save(sysCodeGenerateConfig);
    }

    @Override
    public void addList(List<InforMationColumnsResult> inforMationColumnsResultList, CodeGenerate codeGenerate) {
        for (InforMationColumnsResult inforMationColumnsResult : inforMationColumnsResultList) {
            SysCodeGenerateConfig sysCodeGenerateConfig = new SysCodeGenerateConfig();

            String YesOrNo = YesOrNotEnum.Y.getCode();
            if (ObjectUtil.isNotNull(inforMationColumnsResult.getColumnKey())
                    && inforMationColumnsResult.getColumnKey().equals(GenConstant.DB_TABLE_COM_KRY) ||
                    TableFilteredFieldsEnum.contains(inforMationColumnsResult.getColumnName())) {
                YesOrNo = YesOrNotEnum.N.getCode();
            }
            if (TableFilteredFieldsEnum.contains(inforMationColumnsResult.getColumnName())) {
                sysCodeGenerateConfig.setWhetherCommon(YesOrNotEnum.Y.getCode());
            } else {
                sysCodeGenerateConfig.setWhetherCommon(YesOrNotEnum.N.getCode());
            }

            sysCodeGenerateConfig.setCodeGenId(codeGenerate.getId());
            sysCodeGenerateConfig.setColumnName(inforMationColumnsResult.getColumnName());
            sysCodeGenerateConfig.setColumnComment(inforMationColumnsResult.getColumnComment());
            sysCodeGenerateConfig.setJavaName(NamingConTool.UnderlineToHump(inforMationColumnsResult.getColumnName(), codeGenerate.getTablePrefix()));
            sysCodeGenerateConfig.setJavaType(JavaSqlTool.sqlToJava(inforMationColumnsResult.getDataType()));
            sysCodeGenerateConfig.setWhetherRetract(YesOrNotEnum.N.getCode());

            sysCodeGenerateConfig.setWhetherRequired(YesOrNo);
            sysCodeGenerateConfig.setQueryWhether(YesOrNo);
            sysCodeGenerateConfig.setWhetherAddUpdate(YesOrNo);
            sysCodeGenerateConfig.setWhetherTable(YesOrNo);

            sysCodeGenerateConfig.setColumnKey(inforMationColumnsResult.getColumnKey());

            // 设置get set方法使用的名称
            String columnName = NamingConTool.UnderlineToHump(sysCodeGenerateConfig.getColumnName(),"");
            sysCodeGenerateConfig.setColumnKeyName(columnName.substring(0,1).toUpperCase() + columnName.substring(1,columnName.length()));

            sysCodeGenerateConfig.setDataType(inforMationColumnsResult.getDataType());
            sysCodeGenerateConfig.setEffectType(JavaEffTool.javaToEff(sysCodeGenerateConfig.getJavaType()));
            sysCodeGenerateConfig.setQueryType(QueryTypeEnum.eq.getCode());

            this.save(sysCodeGenerateConfig);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(SysCodeGenerateConfigParam sysCodeGenerateConfigParam) {
        LambdaQueryWrapper<SysCodeGenerateConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysCodeGenerateConfig::getCodeGenId, sysCodeGenerateConfigParam.getCodeGenId());
        this.remove(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(List<SysCodeGenerateConfigParam> sysCodeGenerateConfigParamList) {
        for (SysCodeGenerateConfigParam sysCodeGenerateConfigParam : sysCodeGenerateConfigParamList) {
            SysCodeGenerateConfig sysCodeGenerateConfig = this.querySysCodeGenerateConfig(sysCodeGenerateConfigParam);
            BeanUtil.copyProperties(sysCodeGenerateConfigParam, sysCodeGenerateConfig);
            this.updateById(sysCodeGenerateConfig);
        }
    }

    @Override
    public SysCodeGenerateConfig detail(SysCodeGenerateConfigParam sysCodeGenerateConfigParam) {
        return this.querySysCodeGenerateConfig(sysCodeGenerateConfigParam);
    }

    /**
     * 获取代码生成详细配置
     *
     *
     * @date 2021-02-06 20:19:49
     */
    private SysCodeGenerateConfig querySysCodeGenerateConfig(SysCodeGenerateConfigParam sysCodeGenerateConfigParam) {
        SysCodeGenerateConfig sysCodeGenerateConfig = this.getById(sysCodeGenerateConfigParam.getId());
        if (ObjectUtil.isNull(sysCodeGenerateConfig)) {
            throw new ServiceException(SysCodeGenerateConfigExceptionEnum.NOT_EXIST);
        }
        return sysCodeGenerateConfig;
    }
}
