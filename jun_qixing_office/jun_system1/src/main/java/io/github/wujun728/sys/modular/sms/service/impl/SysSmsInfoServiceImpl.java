
package io.github.wujun728.sys.modular.sms.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.wujun728.core.consts.CommonConstant;
import io.github.wujun728.core.context.constant.ConstantContextHolder;
import io.github.wujun728.core.exception.ServiceException;
import io.github.wujun728.core.factory.PageFactory;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.core.pojo.sms.AliyunSmsConfigs;
import io.github.wujun728.sys.modular.sms.entity.SysSms;
import io.github.wujun728.sys.modular.sms.enums.SmsSendExceptionEnum;
import io.github.wujun728.sys.modular.sms.enums.SmsSendStatusEnum;
import io.github.wujun728.sys.modular.sms.enums.SmsVerifyEnum;
import io.github.wujun728.sys.modular.sms.mapper.SysSmsMapper;
import io.github.wujun728.sys.modular.sms.param.SysSmsInfoParam;
import io.github.wujun728.sys.modular.sms.param.SysSmsSendParam;
import io.github.wujun728.sys.modular.sms.param.SysSmsVerifyParam;
import io.github.wujun728.sys.modular.sms.service.SysSmsInfoService;

import java.util.Date;
import java.util.List;

/**
 * 系统短信接口实现类
 *
 * @date 2018/7/5 13:44
 */

@Service
public class SysSmsInfoServiceImpl extends ServiceImpl<SysSmsMapper, SysSms> implements SysSmsInfoService {

    private static final Log log = Log.get();

    @Override
    public Long saveSmsInfo(SysSmsSendParam sysSmsSendParam, String validateCode) {

        if (ObjectUtil.isEmpty(sysSmsSendParam.getPhoneNumbers())) {
            throw new ServiceException(SmsSendExceptionEnum.PHONE_NUMBER_EMPTY);
        }
        if (ObjectUtil.isEmpty(validateCode)) {
            throw new ServiceException(SmsSendExceptionEnum.VALIDATE_CODE_EMPTY);
        }
        if (ObjectUtil.isEmpty(sysSmsSendParam.getTemplateCode())) {
            throw new ServiceException(SmsSendExceptionEnum.TEMPLATE_CODE_EMPTY);
        }

        //当前时间
        Date nowDate = new Date();

        //短信失效时间
        AliyunSmsConfigs aliyunSmsProperties = ConstantContextHolder.getAliyunSmsConfigs();
        long invalidateTime = nowDate.getTime() + aliyunSmsProperties.getInvalidateMinutes() * 60 * 1000;
        Date invalidate = new Date(invalidateTime);

        SysSms sysSms = new SysSms();
        sysSms.setCreateTime(nowDate);
        sysSms.setInvalidTime(invalidate);
        sysSms.setPhoneNumbers(sysSmsSendParam.getPhoneNumbers());
        sysSms.setStatus(SmsSendStatusEnum.WAITING.getCode());
        sysSms.setSource(sysSmsSendParam.getSmsSendSourceEnum().getCode());
        sysSms.setTemplateCode(sysSmsSendParam.getTemplateCode());
        sysSms.setValidateCode(validateCode);

        this.save(sysSms);

        log.info(">>> 发送短信，存储短信到数据库，数据为：" + JSON.toJSONString(sysSms));

        return sysSms.getId();
    }

    @Override
    public void updateSmsInfo(Long smsId, SmsSendStatusEnum smsSendStatusEnum) {
        SysSms sysSms = this.getById(smsId);
        sysSms.setStatus(smsSendStatusEnum.getCode());
        this.updateById(sysSms);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SmsVerifyEnum validateSmsInfo(SysSmsVerifyParam sysSmsVerifyParam) {

        if (ObjectUtil.isEmpty(sysSmsVerifyParam.getPhoneNumbers())) {
            throw new ServiceException(SmsSendExceptionEnum.PHONE_NUMBER_EMPTY);
        }
        if (ObjectUtil.isEmpty(sysSmsVerifyParam)) {
            throw new ServiceException(SmsSendExceptionEnum.VALIDATE_CODE_EMPTY);
        }
        if (ObjectUtil.isEmpty(sysSmsVerifyParam.getTemplateCode())) {
            throw new ServiceException(SmsSendExceptionEnum.TEMPLATE_CODE_EMPTY);
        }

        //查询有没有这条记录
        LambdaQueryWrapper<SysSms> smsQueryWrapper = new LambdaQueryWrapper<>();

        smsQueryWrapper.eq(SysSms::getPhoneNumbers, sysSmsVerifyParam.getPhoneNumbers())
                .and(f -> f.eq(SysSms::getSource, sysSmsVerifyParam.getSmsSendSourceEnum().getCode()))
                .and(f -> f.eq(SysSms::getTemplateCode, sysSmsVerifyParam.getTemplateCode()));

        smsQueryWrapper.orderByDesc(SysSms::getCreateTime);

        List<SysSms> sysSmsList = this.list(smsQueryWrapper);

        log.info(">>> 验证短信Provider接口，查询到sms记录：" + JSON.toJSONString(sysSmsList));

        //如果找不到记录，提示验证失败
        if (ObjectUtil.isEmpty(sysSmsList)) {
            log.info(">>> 验证短信Provider接口，找不到验证码记录，响应验证失败！");
            return SmsVerifyEnum.ERROR;
        } else {

            //获取最近发送的第一条
            SysSms sysSms = sysSmsList.get(0);

            //先判断状态是不是失效的状态
            if (SmsSendStatusEnum.INVALID.getCode().equals(sysSms.getStatus())) {
                log.info(">>> 验证短信Provider接口，短信状态是失效，响应验证失败！");
                return SmsVerifyEnum.ERROR;
            }

            //如果验证码和传过来的不一致
            if (!sysSmsVerifyParam.getCode().equals(sysSms.getValidateCode())) {
                log.info(">>> 验证短信Provider接口，验证手机号和验证码不一致，响应验证失败！");
                return SmsVerifyEnum.ERROR;
            }

            //判断是否超时
            Date invalidTime = sysSms.getInvalidTime();
            if (ObjectUtil.isEmpty(invalidTime) || new Date().after(invalidTime)) {
                log.info(">>> 验证短信Provider接口，验证码超时，响应验证失败！");
                return SmsVerifyEnum.EXPIRED;
            }

            //验证成功把短信设置成失效
            sysSms.setStatus(SmsSendStatusEnum.INVALID.getCode());
            this.updateById(sysSms);

            log.info(">>> 验证短信Provider接口，验证码验证成功！");
            return SmsVerifyEnum.SUCCESS;
        }
    }

    @Override
    public PageResult<SysSms> page(SysSmsInfoParam sysSmsInfoParam) {
        QueryWrapper<SysSms> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(sysSmsInfoParam)) {
            //根据手机号模糊查询
            if (ObjectUtil.isNotEmpty(sysSmsInfoParam.getPhoneNumbers())) {
                queryWrapper.lambda().like(SysSms::getPhoneNumbers, sysSmsInfoParam.getPhoneNumbers());
            }
            //根据发送状态查询（字典 0 未发送，1 发送成功，2 发送失败，3 失效）
            if (ObjectUtil.isNotEmpty(sysSmsInfoParam.getStatus())) {
                queryWrapper.lambda().eq(SysSms::getStatus, sysSmsInfoParam.getStatus());
            }
            //根据来源查询（字典 1 app， 2 pc， 3 其他）
            if (ObjectUtil.isNotEmpty(sysSmsInfoParam.getSource())) {
                queryWrapper.lambda().eq(SysSms::getSource, sysSmsInfoParam.getSource());
            }
            //排序
            if(ObjectUtil.isAllNotEmpty(sysSmsInfoParam.getSortBy(), sysSmsInfoParam.getOrderBy())) {
                queryWrapper.orderBy(true, sysSmsInfoParam.getOrderBy().equals(CommonConstant.ASC), StrUtil.toUnderlineCase(sysSmsInfoParam.getSortBy()));
            } else {
                //根据排序升序排列，序号越小越在前
                queryWrapper.lambda().orderByDesc(SysSms::getCreateTime);
            }
        }
        return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
    }


}
