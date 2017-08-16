-- 创建数据库
CREATE DATABASE `hualuomoli-sample` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE DATABASE `hualuomoli-sample-other` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
-- 创建用户
CREATE USER 'sample'@'%'; 
CREATE USER 'sample-other'@'%'; 
-- CREATE USER 'sample'@'%' IDENTIFIED BY '123456'; 
-- 赋权
GRANT ALL ON `hualuomoli-sample`.* to 'sample'@'%';
GRANT ALL ON `hualuomoli-sample-other`.* to 'sample-other'@'%';


CREATE TABLE `hualuomoli-sample`.`t_user` (
  `id` varchar(32) primary key NOT NULL comment 'id',
  `username` varchar(32) NOT NULL comment '用户名',
  `nickname` varchar(32) comment '昵称',
  `age` int(3) comment '年龄',
  `sex` char(1) comment '性别',
  `state` integer(1) comment '数据状态1=正常',
  `status` varchar(10) comment '数据状态',
  `remark` varchar(200) comment '描述信息'
) ENGINE=InnoDB comment '用户信息' DEFAULT CHARSET=utf8;


CREATE TABLE `hualuomoli-sample-other`.`t_address` (
  `id` varchar(32) primary key NOT NULL comment 'id',
  `areaCode` varchar(32) NOT NULL comment '区域编码',
  `province` varchar(32) comment '省份名称',
  `city` varchar(32) comment '地市',
  `county` varchar(32) comment '区县',
  `detailAddress` varchar(64) comment '详细地址'
) ENGINE=InnoDB comment '收货地址' DEFAULT CHARSET=utf8;
