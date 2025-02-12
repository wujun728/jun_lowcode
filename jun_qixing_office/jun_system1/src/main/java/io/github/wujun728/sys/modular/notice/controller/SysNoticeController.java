
package io.github.wujun728.sys.modular.notice.controller;

import io.github.wujun728.sys.modular.notice.entity.SysNotice;
import io.github.wujun728.sys.modular.notice.param.SysNoticeParam;
import io.github.wujun728.sys.modular.notice.result.SysNoticeReceiveResult;
import io.github.wujun728.sys.modular.notice.service.SysNoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import io.github.wujun728.core.annotion.BusinessLog;
import io.github.wujun728.core.annotion.Permission;
import io.github.wujun728.core.enums.LogAnnotionOpTypeEnum;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.core.pojo.response.ResponseData;
import io.github.wujun728.core.pojo.response.SuccessResponseData;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统通知公告控制器
 * @date 2020/6/28 17:18
 */
@Controller
public class SysNoticeController {

    @Resource
    private SysNoticeService sysNoticeService;

    /**
     * 系统通知公告页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @Permission
    @GetMapping("/sysNotice/index")
    public String index() {
        return "system/sysNotice/index.html";
    }

    /**
     * 系统通知公告表单页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @GetMapping("/sysNotice/form")
    public String form() {
        return "system/sysNotice/form.html";
    }

    /**
     * 已收公告页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @Permission
    @GetMapping("/sysNotice/receivedPage")
    public String receivedPage() {
        return "system/sysNotice/received.html";
    }

    /**
     * 系统通知公告详情页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @GetMapping("/sysNotice/detailPage")
    public String detailPage() {
        return "system/sysNotice/detail.html";
    }

    /**
     * 查询系统通知公告
     *
     * @author xuyuxiang
     * @date 2020/6/28 17:24
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysNotice/page")
    @BusinessLog(title = "系统通知公告_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<SysNotice> page(SysNoticeParam sysNoticeParam) {
        return sysNoticeService.page(sysNoticeParam);
    }

    /**
     * 查询我收到的系统通知公告
     *
     * @author xuyuxiang
     * @date 2020/6/29 11:59
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysNotice/received")
    @BusinessLog(title = "系统通知公告_已收", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<SysNoticeReceiveResult> received(SysNoticeParam sysNoticeParam) {
        return sysNoticeService.received(sysNoticeParam);
    }

    /**
     * 添加系统通知公告
     *
     * @author xuyuxiang
     * @date 2020/6/28 17:24
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysNotice/add")
    @BusinessLog(title = "系统通知公告_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(SysNoticeParam.add.class) SysNoticeParam sysNoticeParam) {
        sysNoticeService.add(sysNoticeParam);
        return new SuccessResponseData();
    }

    /**
     * 删除系统通知公告
     *
     * @author xuyuxiang
     * @date 2020/6/28 17:25
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysNotice/delete")
    @BusinessLog(title = "系统通知公告_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(SysNoticeParam.delete.class) List<SysNoticeParam> sysNoticeParamList) {
        sysNoticeService.delete(sysNoticeParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑系统通知公告
     *
     * @author xuyuxiang
     * @date 2020/6/28 17:25
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysNotice/edit")
    @BusinessLog(title = "系统通知公告_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(SysNoticeParam.edit.class) SysNoticeParam sysNoticeParam) {
        sysNoticeService.edit(sysNoticeParam);
        return new SuccessResponseData();
    }

    /**
     * 查看系统通知公告
     *
     * @author xuyuxiang
     * @date 2020/6/28 17:25
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysNotice/detail")
    @BusinessLog(title = "系统通知公告_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(SysNoticeParam.detail.class) SysNoticeParam sysNoticeParam) {
        return new SuccessResponseData(sysNoticeService.detail(sysNoticeParam));
    }

    /**
     * 修改状态
     *
     * @author xuyuxiang
     * @date 2020/6/29 9:44
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysNotice/changeStatus")
    @BusinessLog(title = "系统通知公告_修改状态", opType = LogAnnotionOpTypeEnum.CHANGE_STATUS)
    public ResponseData changeStatus(@RequestBody @Validated(SysNoticeParam.changeStatus.class) SysNoticeParam sysNoticeParam) {
        sysNoticeService.changeStatus(sysNoticeParam);
        return new SuccessResponseData();
    }
}
