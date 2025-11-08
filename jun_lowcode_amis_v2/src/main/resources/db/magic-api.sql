CREATE TABLE `magic_api_file` (
  `file_path` varchar(512) NOT NULL COMMENT '路径',
  `file_content` mediumtext COMMENT '内容',
  PRIMARY KEY (`file_path`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='magic-api存储'