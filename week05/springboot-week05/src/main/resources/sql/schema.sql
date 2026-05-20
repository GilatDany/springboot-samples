CREATE DATABASE IF NOT EXISTS springboot3_db DEFAULT CHARACTER SET utf8mb4;

USE springboot3_db;

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
                          username VARCHAR(50) NOT NULL COMMENT '用户名',
                          password VARCHAR(100) NOT NULL COMMENT '密码',
                          age INT NULL COMMENT '年龄',
                          email VARCHAR(100) NULL COMMENT '邮箱',
                          create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';