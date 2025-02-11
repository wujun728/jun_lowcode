
package io.github.wujun728.sys.modular.msg.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.wujun728.core.context.login.LoginContextHolder;
import io.github.wujun728.core.exception.ServiceException;
import io.github.wujun728.sys.modular.msg.entity.SysMessage;
import io.github.wujun728.sys.modular.msg.entity.SysMessageUser;
import io.github.wujun728.sys.modular.msg.enums.SysMessageExceptionEnum;
import io.github.wujun728.sys.modular.msg.enums.SysMessageUserStatusEnum;
import io.github.wujun728.sys.modular.msg.mapper.SysMessageMapper;
import io.github.wujun728.sys.modular.msg.param.SysMessageParam;
import io.github.wujun728.sys.modular.msg.service.SysMessageService;
import io.github.wujun728.sys.modular.msg.service.SysMessageUserService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 系统消息service接口实现类
 * @date 2021-01-21 17:50:51
 */
@Service
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessage> implements SysMessageService {

    @Resource
    private SysMessageUserService sysMessageUserService;

    @Override
    public List<SysMessage> list(SysMessageParam sysMessageParam) {
        QueryWrapper<SysMessage> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(sysMessageParam)) {
            //根据消息类别查询，字典（1通知 2私信 3待办）
            if(ObjectUtil.isNotEmpty(sysMessageParam.getType())) {
                queryWrapper.eq("sys_message.type", sysMessageParam.getType());
            }
            //根据发送类别查询，字典（1直接发送 2定时发送）
            if(ObjectUtil.isNotEmpty(sysMessageParam.getSendType())) {
                queryWrapper.eq("sys_message.send_type", sysMessageParam.getSendType());
            }
            //根据是否已读查询，状态（字典 0未读 1已读）
            if(ObjectUtil.isNotEmpty(sysMessageParam.getStatus())) {
                queryWrapper.eq("sys_message_user.status", sysMessageParam.getStatus());
            }
            //根据发送人id查询，系统发送则为-1
            if(ObjectUtil.isNotEmpty(sysMessageParam.getSenderId())) {
                queryWrapper.eq("sys_message_user.sender_id", sysMessageParam.getSenderId());
            }
        }
        //查询当前用户的
        queryWrapper.eq("sys_message_user.receiver_id", LoginContextHolder.me().getSysLoginUserId());
        return this.baseMapper.list(queryWrapper);
    }

    @Override
    public void detail(List<SysMessageParam> sysMessageParamList) {
        sysMessageParamList.forEach(sysMessageParam -> {
            SysMessage sysMessage = this.querySysMessage(sysMessageParam);
            sysMessageUserService.read(sysMessage.getId(), LoginContextHolder.me().getSysLoginUserId());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveMessage(String title, String content, Integer type, Integer sendType, Long sender, String businessData, List<Long> noticeUserList) {
        SysMessage sysMessage = new SysMessage();
        sysMessage.setTitle(title);
        sysMessage.setContent(content);
        sysMessage.setType(type);
        sysMessage.setSendType(sendType);
        sysMessage.setBusinessData(businessData);
        sysMessage.setSendTime(new Date());
        this.save(sysMessage);
        sysMessageUserService.saveMessageUser(sysMessage.getId(), sender, noticeUserList);
    }

    @Override
    public boolean hasUnreadMsg() {
        LambdaQueryWrapper<SysMessageUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMessageUser::getReceiverId, LoginContextHolder.me().getSysLoginUserId());
        queryWrapper.eq(SysMessageUser::getStatus, SysMessageUserStatusEnum.UNREAD.getCode());
        return sysMessageUserService.list(queryWrapper).size() > 0;
    }

    /**
     * 获取消息表
     *
     * @author xuyuxiang
     * @date 2021-01-21 17:50:51
     */
    private SysMessage querySysMessage(SysMessageParam sysMessageParam) {
        SysMessage sysMessage = this.getById(sysMessageParam.getId());
        if (ObjectUtil.isNull(sysMessage)) {
            throw new ServiceException(SysMessageExceptionEnum.NOT_EXIST);
        }
        return sysMessage;
    }
}
