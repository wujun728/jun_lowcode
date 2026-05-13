package com.jqp.example.dict.application;

import com.jqp.ddd.application.BaseApplicationService;
import com.jqp.example.dict.domain.Dict;
import com.jqp.example.dict.domain.DictRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * DDD应用层 - 字典应用服务
 * 处理字典相关的业务逻辑
 *
 * @author JQP
 * @date 2026/02/28
 */
@Service
@Slf4j
public class DictApplicationService extends BaseApplicationService<Dict, Long, DictRepository> {

    @Resource
    private DictRepository dictRepository;

    @Override
    protected DictRepository getRepository() {
        return dictRepository;
    }

    /**
     * 根据编码查询字典
     */
    public Optional<Dict> queryByCode(String code) {
        log.debug("根据编码查询字典，code={}", code);
        return dictRepository.findByCode(code);
    }

    /**
     * 根据分类查询字典列表
     */
    public List<Dict> queryByCategory(String category) {
        log.debug("根据分类查询字典，category={}", category);
        return dictRepository.findByCategory(category);
    }

    /**
     * 根据分类查询启用的字典列表
     */
    public List<Dict> queryEnabledByCategory(String category) {
        log.debug("根据分类查询启用的字典，category={}", category);
        return dictRepository.findEnabledByCategory(category);
    }

    /**
     * 创建字典（带验证）
     */
    public Dict createDict(Dict dict) {
        // 验证编码唯一性
        if (dictRepository.isCodeDuplicate(dict.getCode(), null)) {
            throw new IllegalArgumentException("字典编码已存在：" + dict.getCode());
        }
        log.info("创建字典，code={}", dict.getCode());
        return create(dict);
    }

    /**
     * 修改字典（带验证）
     */
    public Dict modifyDict(Dict dict) {
        // 验证编码唯一性（排除当前记录）
        if (dictRepository.isCodeDuplicate(dict.getCode(), dict.getId())) {
            throw new IllegalArgumentException("字典编码已存在：" + dict.getCode());
        }
        log.info("修改字典，id={}，code={}", dict.getId(), dict.getCode());
        return modify(dict);
    }

    /**
     * 启用字典
     */
    public void enableDict(Long id) {
        Optional<Dict> dictOpt = queryById(id);
        if (!dictOpt.isPresent()) {
            throw new IllegalArgumentException("字典不存在");
        }
        Dict dict = dictOpt.get();
        dict.enable();
        modify(dict);
        log.info("启用字典，id={}", id);
    }

    /**
     * 禁用字典
     */
    public void disableDict(Long id) {
        Optional<Dict> dictOpt = queryById(id);
        if (!dictOpt.isPresent()) {
            throw new IllegalArgumentException("字典不存在");
        }
        Dict dict = dictOpt.get();
        dict.disable();
        modify(dict);
        log.info("禁用字典，id={}", id);
    }
}
