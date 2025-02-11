
package io.github.wujun728.sys.modular.msg.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.github.wujun728.sys.modular.msg.entity.SysMessageUser;

import java.util.List;

/**
 * 消息人员关联表service接口
 * @date 2021-01-22 09:37:58
 */
public interface SysMessageUserService extends IService<SysMessageUser> {

    /**
     * 将消息设置为已读
     *
     * @author xuyuxiang
     * @date 2021/1/22 10:39
     */
    void read(Long messageId, Long userId);

    /**
     * 保存用户消息信息
     *
     * @param messageId 消息id
     * @param sender 发送人
     * @param noticeUserList 通知到的用户id集合
     * @author xuyuxiang
     * @date 2021/1/22 12:13
     */
    void saveMessageUser(Long messageId, Long sender, List<Long> noticeUserList);
}
