
package io.github.wujun728.sys.modular.msg.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import io.github.wujun728.sys.modular.msg.entity.SysMessageUser;
import io.github.wujun728.sys.modular.msg.enums.SysMessageUserStatusEnum;
import io.github.wujun728.sys.modular.msg.mapper.SysMessageUserMapper;
import io.github.wujun728.sys.modular.msg.service.SysMessageUserService;

import java.util.List;

/**
 * 消息人员关联表service接口实现类
 * @date 2021-01-22 09:37:58
 */
@Service
public class SysMessageUserServiceImpl extends ServiceImpl<SysMessageUserMapper, SysMessageUser> implements SysMessageUserService {

    @Override
    public void read(Long messageId, Long userId) {
        LambdaUpdateWrapper<SysMessageUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysMessageUser::getMessageId, messageId).eq(SysMessageUser::getReceiverId, userId)
                .set(SysMessageUser::getStatus, SysMessageUserStatusEnum.READ.getCode());
        this.update(updateWrapper);
    }

    @Override
    public void saveMessageUser(Long messageId, Long sender, List<Long> noticeUserList) {
        noticeUserList.forEach(receiver -> {
            SysMessageUser sysMessageUser = new SysMessageUser();
            sysMessageUser.setMessageId(messageId);
            sysMessageUser.setSenderId(sender);
            sysMessageUser.setReceiverId(receiver);
            sysMessageUser.setStatus(SysMessageUserStatusEnum.UNREAD.getCode());
            this.save(sysMessageUser);
        });
    }
}
