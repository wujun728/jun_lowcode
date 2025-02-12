
package io.github.wujun728.sys.modular.msg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.wujun728.sys.modular.msg.entity.SysMessage;
import io.github.wujun728.sys.modular.msg.param.SysMessageParam;

import java.util.List;

/**
 * 系统消息service接口
 * @date 2021-01-21 17:50:51
 */
public interface SysMessageService extends IService<SysMessage> {

    /**
     * 系统消息列表
     *
     * @param sysMessageParam 查询参数
     * @author xuyuxiang
     * @date 2021-01-21 17:50:51
     */
    List<SysMessage> list(SysMessageParam sysMessageParam);

    /**
     * 读取系统消息
     *
     * @param sysMessageParamList 读取参数
     * @author xuyuxiang
     * @date 2021/1/22 10:37
     */
    void detail(List<SysMessageParam> sysMessageParamList);

    /**
     * 保存消息到消息表
     *
     * @author xuyuxiang
     * @date 2021/1/22 12:03
     */
    void saveMessage(String title, String content, Integer type, Integer sendType, Long sender, String businessData, List<Long> noticeUserList);

    /**
     * 判断当前用户是否有未读消息
     *
     * @author xuyuxiang
     * @date 2021/1/22 16:25 
     */
    boolean hasUnreadMsg();
}
