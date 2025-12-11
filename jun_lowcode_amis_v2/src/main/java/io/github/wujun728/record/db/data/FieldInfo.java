package io.github.wujun728.record.db.data;

import lombok.Data;

import java.util.Objects;

/***
 * 字段信息
 */
@Data
public class FieldInfo {
    //字段名,用来区分新增更新
    private String oldColumnName;
    //字段名称
    private String columnName;
    //字段注释
    private String columnComment;
    //字段类型
    private String columnType;
    //是否允许为空
    private String isNullable;

//    FieldInfo
//    private String columnName;
    private String fieldName;
    private String fieldClass;
    private String fieldComment;
    private String fieldType;
    private Boolean isPrimaryKey;
    private Boolean isAutoIncrement;
    private long columnSize;
    private Boolean nullable;
    private Boolean comment;
    private String defaultValue;
    private String swaggerClass;


//    ColumnMeta
    //表名
    private String tableName;
    //字段名称
//    private String columnName;
    //字段别名
    private String columnLabel;
    //数据库字段类型
//    private String columnType;
    //java类型
    private String columnClassName;
    //字段注释
//    private String columnComment;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FieldInfo that = (FieldInfo) o;
        return columnName.equals(that.columnName) && Objects.equals(columnComment, that.columnComment) && columnType.equals(that.columnType) && isNullable.equals(that.isNullable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, columnComment, columnType, isNullable);
    }
}
