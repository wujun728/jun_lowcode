
package io.github.wujun728.sys.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.mail.MailAccount;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.wujun728.core.context.constant.ConstantContextHolder;
import io.github.wujun728.core.email.MailSender;
import io.github.wujun728.core.email.modular.SimpleMailSender;
import io.github.wujun728.core.pojo.email.EmailConfigs;

/**
 * 邮件发送控制器
 *
 * @date 2020/6/6 22:27
 */
@Configuration
public class MailSenderConfig {

    /**
     * 邮件发射器
     *
     *
     * @date 2020/6/9 23:13
     */
    @Bean
    public MailSender mailSender() {
        EmailConfigs emailConfigs = ConstantContextHolder.getEmailConfigs();
        MailAccount mailAccount = new MailAccount();
        BeanUtil.copyProperties(emailConfigs, mailAccount);
        return new SimpleMailSender(mailAccount);
    }

}
