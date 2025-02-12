
package io.github.wujun728.sys.modular.sms.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import io.github.wujun728.sys.modular.sms.entity.SysSms;
import io.github.wujun728.sys.modular.sms.enums.SmsVerifyEnum;
import io.github.wujun728.sys.modular.sms.param.SysSmsInfoParam;
import io.github.wujun728.sys.modular.sms.param.SysSmsSendParam;
import io.github.wujun728.sys.modular.sms.param.SysSmsVerifyParam;
import io.github.wujun728.sys.modular.sms.service.SmsSenderService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import io.github.wujun728.core.annotion.BusinessLog;
import io.github.wujun728.core.annotion.Permission;
import io.github.wujun728.core.enums.LogAnnotionOpTypeEnum;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.core.pojo.response.ResponseData;
import io.github.wujun728.core.pojo.response.SuccessResponseData;
import io.github.wujun728.sys.modular.sms.service.SysSmsInfoService;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 短信发送控制器
 *
 * @date 2020/6/7 16:07
 */
@Controller
public class SmsSenderController {

    @Resource
    private SmsSenderService smsSenderService;

    @Resource
    private SysSmsInfoService sysSmsInfoService;

    /**
     * 短信页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @Permission
    @GetMapping("/sysSms/index")
    public String index() {
        return "system/sysSms/index.html";
    }

    /**
     * 发送记录查询
     *
     * @author xuyuxiang
     * @date 2020/7/2 12:03
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysSms/page")
    @BusinessLog(title = "短信发送记录查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<SysSms> page(SysSmsInfoParam sysSmsInfoParam) {
        return sysSmsInfoService.page(sysSmsInfoParam);
    }

    /**
     * 发送验证码短信
     *
     *
     * @date 2020/6/7 16:07
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysSms/sendLoginMessage")
    @BusinessLog(title = "发送验证码短信")
    public ResponseData sendLoginMessage(@RequestBody @Validated SysSmsSendParam sysSmsSendParam) {

        // 设置模板中的参数
        HashMap<String, Object> paramMap = CollectionUtil.newHashMap();
        paramMap.put("code", RandomUtil.randomNumbers(6));
        sysSmsSendParam.setParams(paramMap);

        return new SuccessResponseData(smsSenderService.sendShortMessage(sysSmsSendParam));
    }

    /**
     * 验证短信验证码
     *
     *
     * @date 2020/6/7 16:07
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysSms/validateMessage")
    @BusinessLog(title = "验证短信验证码")
    public ResponseData validateMessage(@RequestBody @Validated SysSmsVerifyParam sysSmsVerifyParam) {
        SmsVerifyEnum smsVerifyEnum = smsSenderService.verifyShortMessage(sysSmsVerifyParam);
        return new SuccessResponseData(smsVerifyEnum);
    }

}
