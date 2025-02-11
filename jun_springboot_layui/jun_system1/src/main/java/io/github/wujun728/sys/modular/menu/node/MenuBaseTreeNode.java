
package io.github.wujun728.sys.modular.menu.node;

import lombok.Data;
import io.github.wujun728.core.pojo.base.node.BaseTreeNode;

import java.util.List;

/**
 * 菜单树节点
 * @date 2020/4/5 12:03
 */
@Data
public class MenuBaseTreeNode implements BaseTreeNode {

    /**
     * 主键
     */
    private Long id;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 名称
     */
    private String title;

    /**
     * 值
     */
    private String value;

    /**
     * 排序，越小优先级越高
     */
    private Integer weight;

    /**
     * 子节点
     */
    private List children;

    /**
     * 父id别名
     */
    @Override
    public Long getPid() {
        return this.parentId;
    }

    /**
     * 子节点
     */
    @Override
    public void setChildren(List children) {
        this.children = children;
    }
}
