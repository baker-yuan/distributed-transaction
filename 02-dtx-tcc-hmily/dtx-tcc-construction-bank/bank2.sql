CREATE DATABASE `bank`;
USE `bank`;

DROP TABLE IF EXISTS `account_info`;
CREATE TABLE `account_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '户主姓名',
  `account_no` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '银行卡号',
  `account_password` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '帐户密码',
  `account_balance` double DEFAULT NULL COMMENT '帐户余额',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `udx_userid` (`account_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;
insert  into `account_info`(`id`,`account_name`,`account_no`,`account_password`,`account_balance`) values (3,'李四','2',NULL,0);

DROP TABLE IF EXISTS `local_try_log`;
CREATE TABLE `local_try_log` (
     `tx_no` varchar(64) NOT NULL COMMENT '事务id',
     `create_time` datetime DEFAULT NULL,
     PRIMARY KEY (`tx_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `local_cancel_log`;
CREATE TABLE `local_cancel_log` (
    `tx_no` varchar(64) NOT NULL COMMENT '事务id',
    `create_time` datetime DEFAULT NULL,
    PRIMARY KEY (`tx_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `local_confirm_log`;
CREATE TABLE `local_confirm_log` (
     `tx_no` varchar(64) NOT NULL COMMENT '事务id',
     `create_time` datetime DEFAULT NULL,
     PRIMARY KEY (`tx_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE DATABASE `hmily`;

