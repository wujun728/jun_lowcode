
package io.github.wujun728.sys.modular.msg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统消息
 * @date 2021-01-21 17:50:51
 */
@Data
@TableName("sys_message")
public class SysMessage implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

}
