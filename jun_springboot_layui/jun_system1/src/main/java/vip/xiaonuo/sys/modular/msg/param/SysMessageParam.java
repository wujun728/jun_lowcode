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
package vip.xiaonuo.sys.modular.msg.param;

import lombok.Data;
import vip.xiaonuo.core.pojo.base.param.BaseParam;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
* 消息表参数类
 *
 * @author xuyuxiang
 * @date 2021-01-21 17:50:51
*/
@Data
public class SysMessageParam extends BaseParam {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空，请检查id参数", groups = {detail.class})
    private Long id;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类别，字典（1通知 2私信 3待办）
     */
    private Integer type;

    /**
     * 发送类别，字典（1直接发送 2定时发送）
     */
    private Integer sendType;

    /**
     * 业务数据，JSON格式
     */
    private String businessData;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 状态（字典 0未读 1已读）
     */
    private Integer status;

    /**
     * 发送人id，系统发送则为-1
     */
    private Long senderId;
}
