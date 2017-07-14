-- 创建数据库
DROP DATABASE IF EXISTS `hualuomoli-sample`;
CREATE DATABASE `hualuomoli-sample` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
-- 创建用户
DROP USER IF EXISTS `sample`;
CREATE USER 'sample'@'%'; 
-- CREATE USER 'sample'@'%' IDENTIFIED BY '123456'; 
-- 赋权
GRANT ALL ON `hualuomoli-sample`.* to 'sample'@'%';
