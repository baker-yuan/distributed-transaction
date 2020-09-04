# rocketmq

 https://yuanyu.blog.csdn.net/article/details/108413697 

# dtx-msg-agricultural-bank

```sql
docker run \
--name mysql_msg_rocket_mq_agricultural_bank \
--env MYSQL_ROOT_HOST=%.%.%.% \
--env MYSQL_ROOT_PASSWORD=123456 \
-p 56085:3306  \
-di mysql:8.0.18
```

```
jdbc:mysql://121.36.33.154:56085?serverTimezone=UTC
```

```sql
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;
DROP TABLE IF EXISTS `de_duplication`;
CREATE TABLE `de_duplication` (
  `tx_no` varchar(64) COLLATE utf8_bin NOT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`tx_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

insert  into `account_info`(`id`,`account_name`,`account_no`,`account_password`,`account_balance`) values (2,'张三','1',NULL,1000);
```

# dtx-msg-construction-bank

```sql
docker run \
--name mysql_msg_rocket_mq_construction_bank \
--env MYSQL_ROOT_HOST=%.%.%.% \
--env MYSQL_ROOT_PASSWORD=123456 \
-p 56086:3306  \
-di mysql:8.0.18
```

```
jdbc:mysql://121.36.33.154:56086?serverTimezone=UTC
```

```sql
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

DROP TABLE IF EXISTS `de_duplication`;
CREATE TABLE `de_duplication` (
  `tx_no` varchar(64) COLLATE utf8_bin NOT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`tx_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

insert  into `account_info`(`id`,`account_name`,`account_no`,`account_password`,`account_balance`) values (3,'李四','2',NULL,0);
```
