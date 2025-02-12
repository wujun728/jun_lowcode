
package io.github.wujun728.sys.modular.msg.param;

import lombok.Data;
import io.github.wujun728.core.pojo.base.param.BaseParam;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
* 消息表参数类
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
