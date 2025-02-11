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
package vip.xiaonuo.sys.modular.file.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vip.xiaonuo.core.annotion.BusinessLog;
import vip.xiaonuo.core.annotion.Permission;
import vip.xiaonuo.core.context.constant.ConstantContextHolder;
import vip.xiaonuo.core.enums.LogAnnotionOpTypeEnum;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.core.pojo.response.ResponseData;
import vip.xiaonuo.core.pojo.response.SuccessResponseData;
import vip.xiaonuo.sys.modular.file.entity.SysFileInfo;
import vip.xiaonuo.sys.modular.file.param.SysFileInfoParam;
import vip.xiaonuo.sys.modular.file.result.SysOnlineFileInfoResult;
import vip.xiaonuo.sys.modular.file.service.SysFileInfoService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文件信息表 控制器
 *
 * @author yubaoshan
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
     * @author yubaoshan
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
     * @author yubaoshan, xuyuxiang
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
     * @author yubaoshan, xuyuxiang
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
     * @author yubaoshan
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
     * @author yubaoshan
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
     * @author yubaoshan
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
     * @author yubaoshan
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
