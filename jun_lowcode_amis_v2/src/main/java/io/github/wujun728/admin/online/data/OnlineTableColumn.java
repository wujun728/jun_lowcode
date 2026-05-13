package io.github.wujun728.admin.online.data;

import io.github.wujun728.record.common.BaseData;
import lombok.Data;

import java.util.Objects;

@Data
public class OnlineTableColumn extends BaseData {
    //表id
    private Long tableId;
    //字段名称
    private String name;
    //字段描述
    private String comments;
    //字段长度
    private Integer length;
    //小数点
    private Integer pointLength;
    //默认值
    private String defaultValue;
    //字段类型
    private String columnType;
    //字段主键
    private Boolean columnPk;
    //字段为空
    private Boolean columnNull;
    //表单项
    private Boolean formItem;
    //表单必填
    private Boolean formRequired;
    //表单控件
    private String formInput;
    //表单控件默认值
    private String formDefault;
    //表单字典
    private String formDict;
    //列表项
    private Boolean gridItem;
    //列表排序
    private Integer gridSort;
    //查询项
    private Boolean queryItem;
    //查询方式
    private String queryType;
    //查询控件
    private String queryInput;
    //扩展参数
    private String extParams;
    //排序
    private Integer sort;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OnlineTableColumn that = (OnlineTableColumn) o;
        return Objects.equals(tableId, that.tableId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(comments, that.comments) &&
                Objects.equals(length, that.length) &&
                Objects.equals(pointLength, that.pointLength) &&
                Objects.equals(defaultValue, that.defaultValue) &&
                Objects.equals(columnType, that.columnType) &&
                Objects.equals(columnPk, that.columnPk) &&
                Objects.equals(columnNull, that.columnNull) &&
                Objects.equals(formItem, that.formItem) &&
                Objects.equals(formRequired, that.formRequired) &&
                Objects.equals(formInput, that.formInput) &&
                Objects.equals(formDefault, that.formDefault) &&
                Objects.equals(formDict, that.formDict) &&
                Objects.equals(gridItem, that.gridItem) &&
                Objects.equals(gridSort, that.gridSort) &&
                Objects.equals(queryItem, that.queryItem) &&
                Objects.equals(queryType, that.queryType) &&
                Objects.equals(queryInput, that.queryInput) &&
                Objects.equals(extParams, that.extParams) &&
                Objects.equals(sort, that.sort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tableId, name, comments, length, pointLength, defaultValue, columnType, columnPk, columnNull, formItem, formRequired, formInput, formDefault, formDict, gridItem, gridSort, queryItem, queryType, queryInput, extParams, sort);
    }
}