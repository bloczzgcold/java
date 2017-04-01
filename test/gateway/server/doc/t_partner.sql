/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.161
Source Server Version : 50627
Source Host           : 192.168.1.161:3306
Source Database       : hualuomoli

Target Server Type    : MYSQL
Target Server Version : 50627
File Encoding         : 65001

Date: 2017-04-01 14:37:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_partner`
-- ----------------------------
DROP TABLE IF EXISTS `t_partner`;
CREATE TABLE `t_partner` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `partner_id` varchar(32) NOT NULL COMMENT '合作伙伴ID',
  `name` varchar(32) NOT NULL COMMENT '合作伙伴名称',
  `configs` text NOT NULL COMMENT '配置信息 ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_partner
-- ----------------------------
INSERT INTO `t_partner` VALUES ('1', 'tester', '测试', '{\"SIGNATURE_RSA_PUBLIC_KEY\":\"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQ27Qvy0LYYe7mhbA+xMamECllocnt6/ydYny6p4C5k0/ldY8CXaJKzxu4UswedIejUM5RGeDxZdixrhmK9MiSXWocXWs+pH2gwa6OWvAzuyphOHwBhIahVxI773/hrP0fdAmsjByuQVATOMaULcDG06tEGzTH83AYRLA/y3P4OwIDAQAB\"}');
