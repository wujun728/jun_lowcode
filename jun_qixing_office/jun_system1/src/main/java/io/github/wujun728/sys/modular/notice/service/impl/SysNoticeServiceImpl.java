
package io.github.wujun728.sys.modular.notice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.wujun728.core.consts.CommonConstant;
import io.github.wujun728.core.context.login.LoginContextHolder;
import io.github.wujun728.core.exception.ServiceException;
import io.github.wujun728.core.factory.PageFactory;
import io.github.wujun728.core.pojo.login.SysLoginUser;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.sys.modular.WebSocketController;
import io.github.wujun728.sys.modular.msg.enums.SysMessageTypeEnum;
import io.github.wujun728.sys.modular.msg.service.SysMessageService;
import io.github.wujun728.sys.modular.notice.entity.SysNotice;
import io.github.wujun728.sys.modular.notice.entity.SysNoticeUser;
import io.github.wujun728.sys.modular.notice.enums.SysNoticeExceptionEnum;
import io.github.wujun728.sys.modular.notice.enums.SysNoticeStatusEnum;
import io.github.wujun728.sys.modular.notice.enums.SysNoticeUserStatusEnum;
import io.github.wujun728.sys.modular.notice.mapper.SysNoticeMapper;
import io.github.wujun728.sys.modular.notice.param.SysNoticeParam;
import io.github.wujun728.sys.modular.notice.result.SysNoticeDetailResult;
import io.github.wujun728.sys.modular.notice.result.SysNoticeReceiveResult;
import io.github.wujun728.sys.modular.notice.service.SysNoticeService;
import io.github.wujun728.sys.modular.notice.service.SysNoticeUserService;
import io.github.wujun728.sys.modular.user.service.SysUserService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统通知公告service接口实现类
 * @date 2020/6/28 17:20
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeService {

    @Resource
    private SysNoticeUserService sysNoticeUserService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysMessageService sysMessageService;

    @Override
    public PageResult<SysNotice> page(SysNoticeParam sysNoticeParam) {
        QueryWrapper<SysNotice> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(sysNoticeParam)) {
            //根据标题或内容模糊查询
            if (ObjectUtil.isNotEmpty(sysNoticeParam.getSearchValue())) {
                queryWrapper.lambda().and(q -> q.like(SysNotice::getTitle, sysNoticeParam.getSearchValue())
                        .or().like(SysNotice::getContent, sysNoticeParam.getSearchValue()));
            }
            //根据类型查询
            if (ObjectUtil.isNotEmpty(sysNoticeParam.getType())) {
                queryWrapper.lambda().eq(SysNotice::getType, sysNoticeParam.getType());
            }
            //排序
            if(ObjectUtil.isAllNotEmpty(sysNoticeParam.getSortBy(), sysNoticeParam.getOrderBy())) {
                queryWrapper.orderBy(true, sysNoticeParam.getOrderBy().equals(CommonConstant.ASC), StrUtil.toUnderlineCase(sysNoticeParam.getSortBy()));
            }
        }
        //查询未删除的
        queryWrapper.lambda().ne(SysNotice::getStatus, SysNoticeStatusEnum.DELETED.getCode());
        return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(SysNoticeParam sysNoticeParam) {
        //校验参数，检查状态是否正确
        checkParam(sysNoticeParam, true);
        SysNotice sysNotice = new SysNotice();
        BeanUtil.copyProperties(sysNoticeParam, sysNotice);
        SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUser();
        sysNotice.setPublicUserId(sysLoginUser.getId());
        sysNotice.setPublicUserName(sysLoginUser.getName());
        sysNotice.setPublicOrgId(sysLoginUser.getLoginEmpInfo().getOrgId());
        sysNotice.setPublicOrgName(sysLoginUser.getLoginEmpInfo().getOrgName());
        //如果是发布，则设置发布时间
        if (SysNoticeStatusEnum.PUBLIC.getCode().equals(sysNotice.getStatus())) {
            sysNotice.setPublicTime(new Date());
        }
        this.save(sysNotice);
        //通知到的人
        List<Long> noticeUserIdList = sysNoticeParam.getNoticeUserIdList();
        Integer noticeUserStatus = SysNoticeUserStatusEnum.UNREAD.getCode();
        sysNoticeUserService.add(sysNotice.getId(), noticeUserIdList, noticeUserStatus);
        //给通知到的人发消息
        //websocket推送
        WebSocketController.sendMessageBatch(sysNotice.getTitle(), noticeUserIdList);
        //推送到消息表
        //此处将notice的Id作为businessData传入，前端可获取此id跳转到通知详情页
        Map<String, String> businessData = CollectionUtil.newHashMap();
        businessData.put(CommonConstant.ID, Convert.toStr(sysNotice.getId()));
        sysMessageService.saveMessage(sysNotice.getTitle(), sysNotice.getContent(),
                SysMessageTypeEnum.NOTICE.getCode(), -1, -1L, JSONUtil.toJsonStr(businessData), noticeUserIdList);
    }

    @Override
    public void delete(List<SysNoticeParam> sysNoticeParamList) {
        sysNoticeParamList.forEach(sysNoticeParam -> {
            SysNotice sysNotice = this.querySysNotice(sysNoticeParam);
            Integer status = sysNotice.getStatus();
            if (!SysNoticeStatusEnum.DRAFT.getCode().equals(status) && !SysNoticeStatusEnum.CANCEL.getCode().equals(status)) {
                throw new ServiceException(SysNoticeExceptionEnum.NOTICE_CANNOT_DELETE);
            }
            sysNotice.setStatus(SysNoticeStatusEnum.DELETED.getCode());
            this.updateById(sysNotice);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(SysNoticeParam sysNoticeParam) {
        SysNotice sysNotice = this.querySysNotice(sysNoticeParam);
        //校验参数，检查状态是否正确
        checkParam(sysNoticeParam, true);
        //非草稿状态
        Integer status = sysNotice.getStatus();
        if (!SysNoticeStatusEnum.DRAFT.getCode().equals(status)) {
            throw new ServiceException(SysNoticeExceptionEnum.NOTICE_CANNOT_EDIT);
        }
        BeanUtil.copyProperties(sysNoticeParam, sysNotice);
        //如果是发布，则设置发布时间
        if (SysNoticeStatusEnum.PUBLIC.getCode().equals(sysNotice.getStatus())) {
            sysNotice.setPublicTime(new Date());
        }
        this.updateById(sysNotice);
        //通知到的人
        List<Long> noticeUserIdList = sysNoticeParam.getNoticeUserIdList();
        Integer noticeUserStatus = SysNoticeUserStatusEnum.UNREAD.getCode();
        sysNoticeUserService.edit(sysNotice.getId(), noticeUserIdList, noticeUserStatus);
    }

    @Override
    public SysNoticeDetailResult detail(SysNoticeParam sysNoticeParam) {
        SysNotice sysNotice = this.querySysNotice(sysNoticeParam);
        Long id = sysNotice.getId();
        //获取通知到的用户
        List<SysNoticeUser> sysNoticeUserList = sysNoticeUserService.getSysNoticeUserListByNoticeId(id);
        List<Long> noticeUserIdList = CollectionUtil.newArrayList();
        List<Dict> noticeUserReadInfoList = CollectionUtil.newArrayList();
        SysNoticeDetailResult sysNoticeResult = new SysNoticeDetailResult();
        BeanUtil.copyProperties(sysNotice, sysNoticeResult);
        if (ObjectUtil.isNotEmpty(sysNoticeUserList)) {
            sysNoticeUserList.forEach(sysNoticeUser -> {
                //遍历通知到的用户，并构造
                noticeUserIdList.add(sysNoticeUser.getUserId());
                Dict dict = Dict.create();
                dict.put("userId", sysNoticeUser.getUserId());
                dict.put("userName", sysUserService.getNameByUserId(sysNoticeUser.getUserId()));
                dict.put("readStatus", sysNoticeUser.getStatus());
                dict.put("readTime", sysNoticeUser.getReadTime());
                noticeUserReadInfoList.add(dict);
            });
        }
        sysNoticeResult.setNoticeUserIdList(noticeUserIdList);
        sysNoticeResult.setNoticeUserReadInfoList(noticeUserReadInfoList);
        //如果该条通知公告为已发布，则将当前用户的该条通知公告设置为已读
        if (sysNotice.getStatus().equals(SysNoticeStatusEnum.PUBLIC.getCode())) {
            sysNoticeUserService.read(sysNotice.getId(),
                    LoginContextHolder.me().getSysLoginUserId(), SysNoticeUserStatusEnum.READ.getCode());
        }
        return sysNoticeResult;
    }

    @Override
    public void changeStatus(SysNoticeParam sysNoticeParam) {
        SysNotice sysNotice = this.querySysNotice(sysNoticeParam);
        //校验参数，检查状态是否正确
        checkParam(sysNoticeParam, false);
        sysNotice.setStatus(sysNoticeParam.getStatus());
        //如果是撤回，则设置撤回时间
        if (SysNoticeStatusEnum.CANCEL.getCode().equals(sysNotice.getStatus())) {
            sysNotice.setCancelTime(new Date());
        } else if (SysNoticeStatusEnum.PUBLIC.getCode().equals(sysNotice.getStatus())) {
            //如果是发布，则设置发布时间
            sysNotice.setPublicTime(new Date());
        }
        this.updateById(sysNotice);
    }

    @Override
    public PageResult<SysNoticeReceiveResult> received(SysNoticeParam sysNoticeParam) {
        QueryWrapper<SysNoticeReceiveResult> queryWrapper = new QueryWrapper<>();
        //查询当前用户的
        queryWrapper.eq("sys_notice_user.user_id", LoginContextHolder.me().getSysLoginUserId());
        if (ObjectUtil.isNotNull(sysNoticeParam)) {
            //根据标题或内容模糊查询
            if (ObjectUtil.isNotEmpty(sysNoticeParam.getSearchValue())) {
                queryWrapper.and(q -> q.like("sys_notice.title", sysNoticeParam.getSearchValue())
                        .or().like("sys_notice.content", sysNoticeParam.getSearchValue()));
            }
            //根据类型查询
            if (ObjectUtil.isNotEmpty(sysNoticeParam.getType())) {
                queryWrapper.eq("sys_notice.type", sysNoticeParam.getType());
            }
            //排序
            if(ObjectUtil.isAllNotEmpty(sysNoticeParam.getSortBy(), sysNoticeParam.getOrderBy())) {
                queryWrapper.orderBy(true, sysNoticeParam.getOrderBy().equals(CommonConstant.ASC), StrUtil.toUnderlineCase(sysNoticeParam.getSortBy()));
            }
        }
        //查询未删除的、非草稿的
        queryWrapper.in("sys_notice.status", SysNoticeStatusEnum.PUBLIC.getCode(), SysNoticeStatusEnum.CANCEL.getCode());
        return new PageResult<>(this.baseMapper.page(PageFactory.defaultPage(), queryWrapper));
    }

    /**
     * 校验参数，判断状态是否正确
     *
     * @author xuyuxiang
     * @date 2020/6/29 10:06
     */
    private void checkParam(SysNoticeParam sysNoticeParam, boolean isAddOrEdit) {
        //保存或编辑时，传递的状态参数应为草稿，或发布
        Integer status = sysNoticeParam.getStatus();
        if (isAddOrEdit) {
            if (!SysNoticeStatusEnum.DRAFT.getCode().equals(status) &&
                    !SysNoticeStatusEnum.PUBLIC.getCode().equals(status)) {
                throw new ServiceException(SysNoticeExceptionEnum.NOTICE_STATUS_ERROR);
            }
        } else {
            //修改状态时，传递的状态参数应为撤回或删除或发布
            if (!SysNoticeStatusEnum.CANCEL.getCode().equals(status) &&
                    !SysNoticeStatusEnum.DELETED.getCode().equals(status) &&
                    !SysNoticeStatusEnum.PUBLIC.getCode().equals(status)) {
                throw new ServiceException(SysNoticeExceptionEnum.NOTICE_STATUS_ERROR);
            }
        }

    }

    /**
     * 获取系统通知公告
     *
     * @author xuyuxiang
     * @date 2020/6/29 9:58
     */
    private SysNotice querySysNotice(SysNoticeParam sysNoticeParam) {
        SysNotice sysNotice = this.getById(sysNoticeParam.getId());
        if (ObjectUtil.isNull(sysNotice)) {
            throw new ServiceException(SysNoticeExceptionEnum.NOTICE_NOT_EXIST);
        }
        return sysNotice;
    }

}
