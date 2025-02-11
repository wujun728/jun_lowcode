
package io.github.wujun728.sys.modular;

import io.github.wujun728.sys.modular.msg.entity.SysMessage;
import io.github.wujun728.sys.modular.msg.enums.SysMessageTypeEnum;
import io.github.wujun728.sys.modular.msg.enums.SysMessageUserStatusEnum;
import io.github.wujun728.sys.modular.msg.param.SysMessageParam;
import io.github.wujun728.sys.modular.msg.service.SysMessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import io.github.wujun728.core.context.login.LoginContextHolder;

import javax.annotation.Resource;
import java.util.List;

/**
 * 杂项页面控制器
 * @date 2020/11/9 11:32
 */
@Controller
public class OtherController {

    @Resource
    private SysMessageService messageService;

    /**
     * 欢迎页
     *
     * @author xuyuxiang
     * @date 2020/11/9 11:34
     */
    @GetMapping("/other/welcomeHtml")
    public String welcomeHtml() {
        return "other/welcome.html";
    }

    /**
     * 主题页
     *
     * @author xuyuxiang
     * @date 2020/11/9 11:34
     */
    @GetMapping("/other/themeHtml")
    public String themeHtml() {
        return "other/theme.html";
    }

    /**
     * 锁屏页
     *
     * @author xuyuxiang
     * @date 2020/11/9 11:34
     */
    @GetMapping("/other/lockScreenHtml")
    public String lockScreenHtml() {
        return "other/lock-screen.html";
    }

    /**
     * 便签页
     *
     * @author xuyuxiang
     * @date 2020/11/9 11:34
     */
    @GetMapping("/other/noteHtml")
    public String noteHtml() {
        return "other/note.html";
    }

    /**
     * 消息页
     *
     * @author xuyuxiang
     * @date 2020/11/9 11:34
     */
    @GetMapping("/other/messageHtml")
    public String messageHtml(Model model) {
        //消息类型（1通知 2私信 3待办）
        SysMessageParam sysMessageParam = new SysMessageParam();
        sysMessageParam.setStatus(SysMessageUserStatusEnum.UNREAD.getCode());
        sysMessageParam.setType(SysMessageTypeEnum.NOTICE.getCode());
        List<SysMessage> noticeMessageList = messageService.list(sysMessageParam);
        model.addAttribute("noticeMessageList", noticeMessageList);
        sysMessageParam.setType(SysMessageTypeEnum.PRIVATE_MSG.getCode());
        List<SysMessage> privateMessageList = messageService.list(sysMessageParam);
        model.addAttribute("privateMessageList", privateMessageList);
        sysMessageParam.setType(SysMessageTypeEnum.TODO.getCode());
        List<SysMessage> todoMessageList = messageService.list(sysMessageParam);
        model.addAttribute("todoMessageList", todoMessageList);
        return "other/message.html";
    }

    /**
     * 个人中心页
     *
     * @author xuyuxiang
     * @date 2020/11/9 11:34
     */
    @GetMapping("/other/userInfoHtml")
    public String userInfoHtml(Model model) {
        model.addAttribute("userInfo", LoginContextHolder.me().getSysLoginUserUpToDate());
        return "other/user-info.html";
    }

    /**
     * 修改密码页
     *
     * @author xuyuxiang
     * @date 2020/11/9 11:34
     */
    @GetMapping("/other/updatePasswordHtml")
    public String updatePasswordHtml() {
        return "other/update-password.html";
    }

    /**
     * 控制台
     *
     * @author xuyuxiang
     * @date 2020/11/9 11:34
     */
    @GetMapping("/other/consoleHtml")
    public String consoleHtml() {
        return "other/console.html";
    }

    /**
     * 分析页
     *
     * @author xuyuxiang
     * @date 2020/11/9 11:34
     */
    @GetMapping("/other/dashboardHtml")
    public String dashboardHtml() {
        return "other/dashboard.html";
    }

    /**
     * 工作台
     *
     * @author xuyuxiang
     * @date 2020/11/9 11:34
     */
    @GetMapping("/other/workplaceHtml")
    public String workplaceHtml() {
        return "other/workplace.html";
    }
}
