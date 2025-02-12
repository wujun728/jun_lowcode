
package io.github.wujun728.core.pojo.node;

import lombok.Data;
import io.github.wujun728.core.pojo.base.node.BaseTreeNode;

import java.util.List;

/**
 * 登录菜单树节点
 *
 * @date 2020/6/9 12:42
 */
@Data
public class LoginMenuTreeNode implements BaseTreeNode {

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
     * 图标
     */
    private String icon;

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
