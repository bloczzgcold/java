DROP TABLE IF EXISTS `t_partner`;
CREATE TABLE `t_partner` (
  `id` varchar(32) primary key not NULL comment 'id',
  `partner_id` varchar(32) not NULL comment '合作伙伴ID',
  `name` varchar(32) not null comment '合作伙伴名称',
  `configs` text not null comment '配置信息 '
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


