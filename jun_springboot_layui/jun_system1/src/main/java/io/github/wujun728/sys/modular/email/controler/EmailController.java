
package io.github.wujun728.sys.modular.email.controler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.mail.MailException;
import cn.hutool.log.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import io.github.wujun728.core.annotion.BusinessLog;
import io.github.wujun728.core.annotion.Permission;
import io.github.wujun728.core.context.requestno.RequestNoContext;
import io.github.wujun728.core.email.MailSender;
import io.github.wujun728.core.email.modular.model.SendMailParam;
import io.github.wujun728.core.enums.LogAnnotionOpTypeEnum;
import io.github.wujun728.core.exception.ServiceException;
import io.github.wujun728.core.pojo.response.ResponseData;
import io.github.wujun728.core.pojo.response.SuccessResponseData;
import io.github.wujun728.sys.modular.email.enums.SysEmailExceptionEnum;

import javax.annotation.Resource;

/**
 * 邮件发送控制器
 *
 * @date 2020/6/9 23:02
 */
@Controller
public class EmailController {

    private static final Log log = Log.get();

    @Resource
    private MailSender mailSender;

    /**
     * 邮件发送页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @Permission
    @GetMapping("/sysEmail/index")
    public String index() {
        return "system/sysEmail/index.html";
    }

    /**
     * 发送邮件
     *
     *, xuyuxiang
     * @date 2020/6/9 23:02
     */
    @PostMapping("/sysEmail/sendEmail")
    @ResponseBody
    @BusinessLog(title = "发送邮件", opType = LogAnnotionOpTypeEnum.OTHER)
    public ResponseData sendEmail(@RequestBody SendMailParam sendMailParam) {
        String to = sendMailParam.getTo();
        if (ObjectUtil.isEmpty(to)) {
            throw new ServiceException(SysEmailExceptionEnum.EMAIL_TO_EMPTY);
        }

        String title = sendMailParam.getTitle();
        if (ObjectUtil.isEmpty(title)) {
            throw new ServiceException(SysEmailExceptionEnum.EMAIL_TITLE_EMPTY);
        }

        String content = sendMailParam.getContent();
        if (ObjectUtil.isEmpty(content)) {
            throw new ServiceException(SysEmailExceptionEnum.EMAIL_CONTENT_EMPTY);
        }
        try {
            mailSender.sendMail(sendMailParam);
        } catch (MailException e) {
            log.error(">>> 邮件发送异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
            throw new ServiceException(SysEmailExceptionEnum.EMAIL_SEND_ERROR);
        }
        return new SuccessResponseData();
    }

    /**c
     * 发送邮件(html)
     *
     *, xuyuxiang
     * @date 2020/6/9 23:02
     */
    @PostMapping("/sysEmail/sendEmailHtml")
    @ResponseBody
    @BusinessLog(title = "发送邮件", opType = LogAnnotionOpTypeEnum.OTHER)
    public ResponseData sendEmailHtml(@RequestBody SendMailParam sendMailParam) {
        String to = sendMailParam.getTo();
        if (ObjectUtil.isEmpty(to)) {
            throw new ServiceException(SysEmailExceptionEnum.EMAIL_TO_EMPTY);
        }

        String title = sendMailParam.getTitle();
        if (ObjectUtil.isEmpty(title)) {
            throw new ServiceException(SysEmailExceptionEnum.EMAIL_TITLE_EMPTY);
        }

        String content = sendMailParam.getContent();
        if (ObjectUtil.isEmpty(content)) {
            throw new ServiceException(SysEmailExceptionEnum.EMAIL_CONTENT_EMPTY);
        }
        try {
            mailSender.sendMailHtml(sendMailParam);
        } catch (MailException e) {
            log.error(">>> 邮件发送异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
            throw new ServiceException(SysEmailExceptionEnum.EMAIL_SEND_ERROR);
        }
        return new SuccessResponseData();
    }
}
