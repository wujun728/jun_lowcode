/*
Copyright [2020] [https://www.xiaonuo.vip]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：

1.请不要删除和修改根目录下的LICENSE文件。
2.请不要删除和修改Snowy源码头部的版权声明。
3.请保留源码和相关描述文件的项目出处，作者声明等。
4.分发源码时候，请注明软件出处 https://gitee.com/xiaonuobase/snowy-layui
5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/xiaonuobase/snowy-layui
6.若您的项目无法满足以上几点，可申请商业授权，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.sys.modular;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vip.xiaonuo.core.context.login.LoginContextHolder;
import vip.xiaonuo.sys.modular.msg.entity.SysMessage;
import vip.xiaonuo.sys.modular.msg.enums.SysMessageTypeEnum;
import vip.xiaonuo.sys.modular.msg.enums.SysMessageUserStatusEnum;
import vip.xiaonuo.sys.modular.msg.param.SysMessageParam;
import vip.xiaonuo.sys.modular.msg.service.SysMessageService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 杂项页面控制器
 *
 * @author xuyuxiang
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
