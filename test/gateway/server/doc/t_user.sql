DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` varchar(32) primary key NOT NULL comment 'id',
  `username` varchar(32) NOT NULL comment '用户名',
  `nickname` varchar(32) DEFAULT NULL comment '昵称',
  `age` int(3) DEFAULT NULL comment '年龄',
  `sex` char(1) DEFAULT NULL comment '性别',
  `create_by` varchar(32) DEFAULT NULL comment '创建人',
  `create_date` datetime DEFAULT NULL comment '创建时间',
  `update_by` varchar(32) DEFAULT NULL comment '修改人',
  `update_date` datetime DEFAULT NULL comment '修改时间',
  `status` int(1) DEFAULT NULL comment '数据状态',
  `remark` varchar(200) DEFAULT NULL comment '描述信息'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
