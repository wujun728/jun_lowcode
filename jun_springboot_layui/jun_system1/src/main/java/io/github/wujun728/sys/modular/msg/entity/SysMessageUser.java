
package io.github.wujun728.sys.modular.msg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统消息人员关联表
 * @date 2021-01-22 09:37:58
 */
@Data
@TableName("sys_message_user")
public class SysMessageUser implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 消息id
     */
    private Long messageId;

    /**
     * 接收人id
     */
    private Long receiverId;

    /**
     * 发送人id，系统发送则为-1
     */
    private Long senderId;

    /**
     * 状态（字典 0未读 1已读）
     */
    private Integer status;
}
