
package io.github.wujun728.sys.modular.msg.controller;

import io.github.wujun728.sys.modular.msg.param.SysMessageParam;
import io.github.wujun728.sys.modular.msg.service.SysMessageService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import io.github.wujun728.core.annotion.BusinessLog;
import io.github.wujun728.core.enums.LogAnnotionOpTypeEnum;
import io.github.wujun728.core.pojo.response.ResponseData;
import io.github.wujun728.core.pojo.response.SuccessResponseData;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统消息控制器
 * @date 2021-01-21 17:50:51
 */
@Controller
public class SysMessageController {

    private String PATH_PREFIX = "sysMessage/";

    @Resource
    private SysMessageService sysMessageService;

    /**
     * 查询系统消息列表
     *
     * @param sysMessageParam 查询参数
     * @author xuyuxiang
     * @date 2021/1/21 17:55
     */
    @ResponseBody
    @GetMapping("/sysMessage/list")
    @BusinessLog(title = "系统消息_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(SysMessageParam sysMessageParam) {
        return new SuccessResponseData(sysMessageService.list(sysMessageParam));
    }

    /**
     * 读取系统消息
     *
     * @author xuyuxiang
     * @date 2020/6/28 17:25
     */
    @ResponseBody
    @PostMapping("/sysMessage/read")
    @BusinessLog(title = "系统消息_读取", opType = LogAnnotionOpTypeEnum.DETAIL)
    public void read(@RequestBody @Validated(SysMessageParam.detail.class) List<SysMessageParam> sysMessageParamList) {
       sysMessageService.detail(sysMessageParamList);
    }
}
