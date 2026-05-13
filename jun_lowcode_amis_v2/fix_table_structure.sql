-- 修复online_table表的id字段为auto_increment
ALTER TABLE online_table MODIFY COLUMN id bigint NOT NULL AUTO_INCREMENT COMMENT 'id';

-- 修复online_table_column表的table_id字段为bigint类型
ALTER TABLE online_table_column MODIFY COLUMN table_id bigint COMMENT '表id';

-- 添加online_table_column表的table_id字段索引
CREATE INDEX idx_online_table_column_table_id ON online_table_column(table_id);
