CREATE DATABASE `bank_pay`;
USE `bank_pay`;

DROP TABLE IF EXISTS `account_pay`;
CREATE TABLE `account_pay`(
    `id`         varchar(64) NOT NULL,
    `account_no` varchar(100) DEFAULT NULL COMMENT '账号',
    `pay_amount` double       DEFAULT NULL COMMENT '充值余额',
    `result`     varchar(20)  DEFAULT NULL COMMENT '充值结果:success，fail',
    PRIMARY KEY (`id`) USING BTREE
);
