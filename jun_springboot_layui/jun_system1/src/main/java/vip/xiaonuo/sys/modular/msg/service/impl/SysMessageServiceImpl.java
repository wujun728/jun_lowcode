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
package vip.xiaonuo.sys.modular.msg.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.xiaonuo.core.context.login.LoginContextHolder;
import vip.xiaonuo.core.exception.ServiceException;
import vip.xiaonuo.sys.modular.msg.entity.SysMessage;
import vip.xiaonuo.sys.modular.msg.entity.SysMessageUser;
import vip.xiaonuo.sys.modular.msg.enums.SysMessageExceptionEnum;
import vip.xiaonuo.sys.modular.msg.enums.SysMessageUserStatusEnum;
import vip.xiaonuo.sys.modular.msg.mapper.SysMessageMapper;
import vip.xiaonuo.sys.modular.msg.param.SysMessageParam;
import vip.xiaonuo.sys.modular.msg.service.SysMessageService;
import vip.xiaonuo.sys.modular.msg.service.SysMessageUserService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 系统消息service接口实现类
 *
 * @author xuyuxiang
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
