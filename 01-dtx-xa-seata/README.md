

# 文档

1.  https://github.com/seata/seata 
2.  http://seata.io/zh-cn/ 
3.   http://seata.io/zh-cn/docs/overview/what-is-seata.html 
4.  http://seata.io/zh-cn/docs/dev/mode/xa-mode.html 
5.   https://github.com/seata/seata-samples/tree/master/seata-xa 


# 创建nacos容器

```bash
docker run \
--name nacos_dtx_seata \
-p 8000:8848 \
-e MODE=standalone \
-d nacos/nacos-server:1.3.0
```

 http://121.36.33.154:8000/nacos 

# dtx-seata-agricultural-bank

```bash
docker run \
--name mysql_dtx_seata_agricultural_bank \
--env MYSQL_ROOT_HOST=%.%.%.% \
--env MYSQL_ROOT_PASSWORD=123456 \
-p 56081:3306  \
-di mysql:8.0.18
```

```
jdbc:mysql://121.36.33.154:56081?serverTimezone=UTC
```

```sql
DROP DATABASE IF EXISTS `user`;
CREATE DATABASE `user`;
USE `user`;

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


DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=167 DEFAULT CHARSET=utf8;

insert  into `account_info`(`id`,`account_name`,`account_no`,`account_password`,`account_balance`) values (2,'张三','1',NULL,1000);
```

# dtx-seata-construction-bank

```
docker run \
--name mysql_dtx_seata_construction_bank \
--env MYSQL_ROOT_HOST=%.%.%.% \
--env MYSQL_ROOT_PASSWORD=123456 \
-p 56082:3306  \
-di mysql:8.0.18
```

```
jdbc:mysql://121.36.33.154:56082?serverTimezone=UTC
```

```sql
DROP DATABASE IF EXISTS `user`;
CREATE DATABASE `user`;
USE `user`;

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

DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert  into `account_info`(`id`,`account_name`,`account_no`,`account_password`,`account_balance`) values (3,'李四','2',NULL,0);
```

# seata-server

```bash
unzip seata-server-0.7.1.zip 
cd /opt/seata-server-0.7.1/bin/

sh seata-server.sh -p 8888 -m file
```

# 测试

```
http://121.36.33.154:8000/nacos/

http://localhost:56081/agricultural/transfer/all
http://localhost:56082/construction/transfer/all
```



2020-09-01 23:26:44.870  INFO 2008 --- [imeoutChecker_1] i.s.core.rpc.netty.NettyPoolableFactory  : register success, cost 428 ms, version:0.7.1,role:TMROLE,channel:[id: 0xb89289fe, L:/192.168.43.252:10243 - R:/121.36.33.154:8888]

2020-09-01 23:26:54.562 DEBUG 2008 --- [lector_RMROLE_1] i.s.c.r.netty.AbstractRpcRemotingClient  : received PONG from /121.36.33.154:8888







```
docker start seata_server;
docker start nacos_dtx_seata;
docker start mysql_dtx_seata_agricultural_bank;
docker start mysql_dtx_seata_construction_bank;
```



 https://hub.docker.com/r/seataio/seata-server/tags 

```bash
docker run \
--name seata_server \
-p 8888:8091 \
-di seataio/seata-server:1.1.0
```

```bash
### 查看日志
docker logs -f seata_server 
```

