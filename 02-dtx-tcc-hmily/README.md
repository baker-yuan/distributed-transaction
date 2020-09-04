# nacos

```
docker run \
--name nacos_dtx_tcc \
-p 8100:8848 \
-e MODE=standalone \
-d nacos/nacos-server:1.3.0
```

 http://121.36.33.154:8100/nacos/ 

# dtx-tcc-agricultural-bank

```bash
docker run \
--name mysql_tcc_agricultural_bank \
--env MYSQL_ROOT_HOST=%.%.%.% \
--env MYSQL_ROOT_PASSWORD=123456 \
-p 56083:3306  \
-di mysql:8.0.18
```

```
jdbc:mysql://121.36.33.154:56083?serverTimezone=UTC
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
insert  into `account_info`(`id`,`account_name`,`account_no`,`account_password`,`account_balance`) values (2,'张三','1',NULL,1000);

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
```

# dtx-tcc-construction-bank

```bash
docker run \
--name mysql_tcc_construction_bank \
--env MYSQL_ROOT_HOST=%.%.%.% \
--env MYSQL_ROOT_PASSWORD=123456 \
-p 56084:3306  \
-di mysql:8.0.18
```

```
jdbc:mysql://121.36.33.154:56084?serverTimezone=UTC
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
```





