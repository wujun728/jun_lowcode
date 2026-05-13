package io.github.wujun728.admin.online.data;

import io.github.wujun728.record.common.BaseData;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class OnlineTable extends BaseData {
    //表名称
    private String name;
    //表描述
    private String comments;
    //表类型
    private String tableType;
    //表单布局
    private String formLayout;
    //表单宽度
    private String formWidth;
    //表单高度
    private String formHeight;
    //是否分页
    private Boolean isPagination;
    //查询宽度
    private String queryWidth;
    //查询最小宽度
    private String queryMinwidth;
    //查询标题宽度
    private String queryTitleWidth;
    //是否树
    private Boolean tree;
    //树父id
    private String treePid;
    //树展示列
    private String treeLabel;
    //是否更新
    private Boolean status;
    //版本号
    private Integer version;
    //删除标识
    private Boolean deleted;
    //创建者
    private Long creator;
    //创建时间
    private String createTime;
    //更新者
    private Long updater;
    //更新时间
    private String updateTime;

    //表字段列表
    private List<OnlineTableColumn> columns = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OnlineTable that = (OnlineTable) o;
        return Objects.equals(name, that.name) && Objects.equals(comments, that.comments) && Objects.equals(tableType, that.tableType) && Objects.equals(formLayout, that.formLayout) && Objects.equals(formWidth, that.formWidth) && Objects.equals(formHeight, that.formHeight) && Objects.equals(isPagination, that.isPagination) && Objects.equals(queryWidth, that.queryWidth) && Objects.equals(queryMinwidth, that.queryMinwidth) && Objects.equals(queryTitleWidth, that.queryTitleWidth) && Objects.equals(tree, that.tree) && Objects.equals(treePid, that.treePid) && Objects.equals(treeLabel, that.treeLabel) && Objects.equals(status, that.status) && Objects.equals(version, that.version) && Objects.equals(deleted, that.deleted) && Objects.equals(creator, that.creator) && Objects.equals(createTime, that.createTime) && Objects.equals(updater, that.updater) && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, comments, tableType, formLayout, formWidth, formHeight, isPagination, queryWidth, queryMinwidth, queryTitleWidth, tree, treePid, treeLabel, status, version, deleted, creator, createTime, updater, updateTime);
    }
}