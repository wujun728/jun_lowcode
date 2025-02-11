
package io.github.wujun728.sys.modular.pos.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.wujun728.core.pojo.base.entity.BaseEntity;

/**
 * 系统职位表
 * @date 2020/3/11 11:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_pos")
public class SysPos extends BaseEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String remark;

    /**
     * 状态（字典 0正常 1停用 2删除）
     */
    private Integer status;
}
