
package io.github.wujun728.sys.modular.file.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import io.github.wujun728.sys.modular.file.entity.SysFileInfo;
import io.github.wujun728.sys.modular.file.param.SysFileInfoParam;
import io.github.wujun728.sys.modular.file.result.SysOnlineFileInfoResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.github.wujun728.core.annotion.BusinessLog;
import io.github.wujun728.core.annotion.Permission;
import io.github.wujun728.core.context.constant.ConstantContextHolder;
import io.github.wujun728.core.enums.LogAnnotionOpTypeEnum;
import io.github.wujun728.core.pojo.page.PageResult;
import io.github.wujun728.core.pojo.response.ResponseData;
import io.github.wujun728.core.pojo.response.SuccessResponseData;
import io.github.wujun728.sys.modular.file.service.SysFileInfoService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文件信息表 控制器
 *
 * @date 2020/6/7 22:15
 */
@Controller
public class SysFileInfoController {

    @Resource
    private SysFileInfoService sysFileInfoService;

    /**
     * onlyoffice资源文件路径
     */
    public static final String ONLY_OFFICE_APP_JS_SUFFIX = "/web-apps/apps/api/documents/api.js";

    /**
     * 系统文件页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @Permission
    @GetMapping("/sysFileInfo/index")
    public String index() {
        return "system/sysFileInfo/index.html";
    }

    /**
     * 在线文档管理页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @Permission
    @GetMapping("/sysFileInfo/onlineIndex")
    public String onlineIndex() {
        return "system/sysFileInfo/onlineIndex.html";
    }

    /**
     * 在线文档编辑页面
     *
     * @author xuyuxiang
     * @date 2020/11/17 16:40
     */
    @GetMapping("/sysFileInfo/onlineEditHtml")
    public String onlineEditHtml(Model model, SysFileInfoParam sysFileInfoParam) {
        //生成在线文档的model
        SysOnlineFileInfoResult sysOnlineFileInfoResult = sysFileInfoService.onlineAddOrUpdate(sysFileInfoParam);
        model.addAttribute("docServiceApiUrl", ConstantContextHolder.getOnlyOfficeUrl() + ONLY_OFFICE_APP_JS_SUFFIX);
        model.addAttribute("sysOnlineFileInfoResult", JSONUtil.toJsonStr(sysOnlineFileInfoResult));
        if(ObjectUtil.isEmpty(sysFileInfoParam.getId())) {
            //如果传入的文件id为空，则表示新建文档，为防止刷新一次创建一次文档，此处将新创建的文件id转发到本接口
            return "redirect:/sysFileInfo/onlineEditHtml?id=" + sysOnlineFileInfoResult.getFileId();
        }
        return "system/sysFileInfo/onlineEdit.html";
    }

    /**
     * 上传文件
     *
     *
     * @date 2020/6/7 22:15
     */
    @ResponseBody
    @PostMapping("/sysFileInfo/upload")
    public Dict upload(@RequestPart("file") MultipartFile file) {
        Long fileId = sysFileInfoService.uploadFile(file);
        SuccessResponseData successResponseData = new SuccessResponseData(fileId);
        Dict dict = Dict.parse(successResponseData);
        dict.put("dir", "/");
        dict.put("msg", successResponseData.getMessage());
        return dict;
    }

    /**
     * 下载文件
     *
     *, xuyuxiang
     * @date 2020/6/9 21:53
     */
    @ResponseBody
    @GetMapping("/sysFileInfo/download")
    public void download(@Validated(SysFileInfoParam.detail.class) SysFileInfoParam sysFileInfoParam, HttpServletResponse response) {
        sysFileInfoService.download(sysFileInfoParam, response);
    }

    /**
     * 文件预览
     *
     *, xuyuxiang
     * @date 2020/6/9 22:07
     */
    @ResponseBody
    @GetMapping("/sysFileInfo/preview")
    public void preview(@Validated(SysFileInfoParam.detail.class) SysFileInfoParam sysFileInfoParam, HttpServletResponse response) {
        sysFileInfoService.preview(sysFileInfoParam, response);
    }

    /**
     * 分页查询文件信息表
     *
     *
     * @date 2020/6/7 22:15
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysFileInfo/page")
    @BusinessLog(title = "文件信息表_分页查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<SysFileInfo> page(SysFileInfoParam sysFileInfoParam) {
        return sysFileInfoService.page(sysFileInfoParam);
    }

    /**
     * 获取全部文件信息表
     *
     *
     * @date 2020/6/7 22:15
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysFileInfo/list")
    @BusinessLog(title = "文件信息表_查询所有", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<Dict> list(SysFileInfoParam sysFileInfoParam) {
        return sysFileInfoService.list(sysFileInfoParam);
    }

    /**
     * 查看详情文件信息表
     *
     *
     * @date 2020/6/7 22:15
     */
    @Permission
    @ResponseBody
    @GetMapping("/sysFileInfo/detail")
    @BusinessLog(title = "文件信息表_查看详情", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(SysFileInfoParam.detail.class) SysFileInfoParam sysFileInfoParam) {
        return new SuccessResponseData(sysFileInfoService.detail(sysFileInfoParam));
    }

    /**
     * 删除文件信息表
     *
     *
     * @date 2020/6/7 22:15
     */
    @Permission
    @ResponseBody
    @PostMapping("/sysFileInfo/delete")
    @BusinessLog(title = "文件信息表_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(SysFileInfoParam.delete.class) List<SysFileInfoParam> sysFileInfoParamList) {
        sysFileInfoService.delete(sysFileInfoParamList);
        return new SuccessResponseData();
    }

    /**
     * 在线文档编辑回调
     *
     * @author xuyuxiang
     * @date 2021/3/25 16:06
     */
    @ResponseBody
    @PostMapping("/sysFileInfo/track")
    public void track() {
        sysFileInfoService.track();
    }
}
