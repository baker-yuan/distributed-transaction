创建nacos容器

```bash
docker run \
--name nacos_dtx_notify_msg \
-p 8200:8848 \
-e MODE=standalone \
-d nacos/nacos-server:1.3.0
```

 http://121.36.33.154:8200/nacos 

# rocketmq

 https://yuanyu.blog.csdn.net/article/details/108413697 

# dtx-notify-msg-bank

```bash
docker run \
--name mysql_notify_msg_bank \
--env MYSQL_ROOT_HOST=%.%.%.% \
--env MYSQL_ROOT_PASSWORD=123456 \
-p 56087:3306  \
-di mysql:8.0.18
```

```
jdbc:mysql://121.36.33.154:56087?serverTimezone=UTC
```

```sql
CREATE DATABASE `bank`;
USE `bank`;

DROP TABLE IF EXISTS `account_info`;
CREATE TABLE `account_info`(
    `id`               bigint(20) NOT NULL AUTO_INCREMENT,
    `account_name`     varchar(100) DEFAULT NULL COMMENT '户主姓名',
    `account_no`       varchar(100) DEFAULT NULL COMMENT '银行卡号',
    `account_password` varchar(100) DEFAULT NULL COMMENT '帐户密码',
    `account_balance`  double       DEFAULT NULL COMMENT '帐户余额',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `udx_userid` (`account_name`)
);

insert into `account_info`(`id`, `account_name`, `account_no`, `account_password`, `account_balance`) values (2, '张三', '1', NULL, 1000);

DROP TABLE IF EXISTS `de_duplication`;
CREATE TABLE `de_duplication`(
     `tx_no`       varchar(64) NOT NULL,
     `create_time` datetime DEFAULT NULL,
     PRIMARY KEY (`tx_no`) USING BTREE
);
```

# dtx-notify-msg-pay

```sql
docker run \
--name mysql_notify_msg_pay \
--env MYSQL_ROOT_HOST=%.%.%.% \
--env MYSQL_ROOT_PASSWORD=123456 \
-p 56088:3306  \
-di mysql:8.0.18
```

```
jdbc:mysql://121.36.33.154:56088?serverTimezone=UTC
```

```sql
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
```


