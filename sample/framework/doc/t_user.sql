DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` varchar(32) primary key NOT NULL comment 'id',
  `username` varchar(32) NOT NULL comment '用户名',
  `nickname` varchar(32) comment '昵称',
  `age` int(3) comment '年龄',
  `sex` char(1) comment '性别',
  `state` integer(1) comment '数据状态1=正常',
  `status` varchar(10) comment '数据状态',
  `remark` varchar(200) comment '描述信息'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
