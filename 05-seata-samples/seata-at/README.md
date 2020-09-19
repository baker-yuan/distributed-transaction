1.  https://github.com/seata/seata-samples/tree/master/seata-xa 

# Sample of Seata AT mode

Spring Cloud 中使用 Seata，使用 Feign 实现远程调用，使用 Spring JDBC 访问 MySQL 数据库

![relation](./relation.png)

# 四个服务分别创建四个数据库

![db](./db.png)

## seata-server

```bash
docker run \
--name seata_server \
-p 60000:8091 \
-di seataio/seata-server:1.1.0
```

```bash
### 查看日志
docker logs -f seata_server 
```

## account-at

```bash
docker run \
--name mysql_dtx_seata_official_account_xa \
--env MYSQL_ROOT_HOST=%.%.%.% \
--env MYSQL_ROOT_PASSWORD=123456 \
-p 56088:3306  \
-di mysql:8.0.18
```

```bash
jdbc:mysql://121.36.33.154:56088?serverTimezone=UTC
```

```sql
DROP DATABASE IF EXISTS `account_xa`;
CREATE DATABASE `account_xa`;
USE `account_xa`;

DROP TABLE IF EXISTS `account_tbl`;
CREATE TABLE `account_tbl`
(
    `id`      int(11) NOT NULL AUTO_INCREMENT,
    `user_id` varchar(255) DEFAULT NULL,
    `money`   int(11)      DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

insert into account_tbl(user_id, money) values ('U100000', '10000');
```

## order-at

```bash
docker run \
--name mysql_dtx_seata_official_order_xa \
--env MYSQL_ROOT_HOST=%.%.%.% \
--env MYSQL_ROOT_PASSWORD=123456 \
-p 56089:3306  \
-di mysql:8.0.18
```

```bash
jdbc:mysql://121.36.33.154:56089?serverTimezone=UTC
```

```sql
DROP DATABASE IF EXISTS `order_xa`;
CREATE DATABASE `order_xa`;
USE `order_xa`;

DROP TABLE IF EXISTS `order_tbl`;
CREATE TABLE `order_tbl`
(
    `id`             int(11) NOT NULL AUTO_INCREMENT,
    `user_id`        varchar(255) DEFAULT NULL,
    `commodity_code` varchar(255) DEFAULT NULL,
    `count`          int(11)      DEFAULT 0,
    `money`          int(11)      DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
```

## storage-at

```bash
docker run \
--name mysql_dtx_seata_official_storage_xa \
--env MYSQL_ROOT_HOST=%.%.%.% \
--env MYSQL_ROOT_PASSWORD=123456 \
-p 56091:3306  \
-di mysql:8.0.18
```

```bash
jdbc:mysql://121.36.33.154:56091?serverTimezone=UTC
```

```sql
DROP DATABASE IF EXISTS `storage_xa`;
CREATE DATABASE `storage_xa`;
USE `storage_xa`;

DROP TABLE IF EXISTS `storage_tbl`;
CREATE TABLE `storage_tbl`
(
    `id`             int(11) NOT NULL AUTO_INCREMENT,
    `commodity_code` varchar(255) DEFAULT NULL,
    `count`          int(11)      DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`commodity_code`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

insert into storage_tbl(commodity_code, count) values ('C100000', '100');
```

## 测试 

```bash
### 无错误成功提交
http://127.0.0.1:56090/purchase?rollback=false&count=30
```
基于初始化数据，和默认的调用逻辑，purchase 将可以被成功调用 3 次。

每次账户余额扣减 3000，由最初的 10000 减少到 1000。

第 4 次调用，因为账户余额不足，purchase 调用将失败。相应的：库存、订单、账户都回滚。

## AT 模式

AT 模式使用 DataSourceProxy

```java
@Bean("dataSourceProxy")
public DataSource dataSource(DruidDataSource druidDataSource) {
    // DataSourceProxy for AT mode
    // io.seata.rm.datasource.DataSourceProxy
    return new DataSourceProxy(druidDataSource);
}
```

当然，AT 模式需要在数据库中建立 undo_log 表，三个数据库都需要创建这张表。


```sql
CREATE TABLE `undo_log`
(
    `id`            bigint(20)   NOT NULL AUTO_INCREMENT,
    `branch_id`     bigint(20)   NOT NULL,
    `xid`           varchar(100) NOT NULL,
    `context`       varchar(128) NOT NULL,
    `rollback_info` longblob     NOT NULL,
    `log_status`    int(11)      NOT NULL,
    `log_created`   datetime     NOT NULL,
    `log_modified`  datetime     NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4;
```



## AT 模式日志

```bash
### docker logs -f seata_server 
2020-09-19 03:54:51.612 INFO [ServerHandlerThread_1_500]io.seata.server.coordinator.DefaultCore.begin:134 -Successfully begin global transaction xid = 172.18.0.6:8091:2054185921
2020-09-19 03:54:51.612 INFO [batchLoggerPrint_1]io.seata.core.rpc.DefaultServerMessageListenerImpl.run:206 -SeataMergeMessage timeout=60000,transactionName=purchase(java.lang.String, java.lang.String, int, boolean)
,clientIp:14.30.2.158,vgroup:my_test_tx_group
2020-09-19 03:54:53.004 INFO [ServerHandlerThread_1_500]io.seata.server.coordinator.AbstractCore.lambda$branchRegister$0:86 -Successfully register branch xid = 172.18.0.6:8091:2054185921, branchId = 2054185922
2020-09-19 03:54:53.005 INFO [batchLoggerPrint_1]io.seata.core.rpc.DefaultServerMessageListenerImpl.run:206 -SeataMergeMessage xid=172.18.0.6:8091:2054185921,branchType=AT,resourceId=jdbc:mysql://121.36.33.154:56091/storage_xa,lockKey=storage_tbl:7
,clientIp:14.30.2.158,vgroup:my_test_tx_group
2020-09-19 03:54:54.584 INFO [ServerHandlerThread_1_500]io.seata.server.coordinator.AbstractCore.lambda$branchRegister$0:86 -Successfully register branch xid = 172.18.0.6:8091:2054185921, branchId = 2054185923
2020-09-19 03:54:54.584 INFO [batchLoggerPrint_1]io.seata.core.rpc.DefaultServerMessageListenerImpl.run:206 -SeataMergeMessage xid=172.18.0.6:8091:2054185921,branchType=AT,resourceId=jdbc:mysql://121.36.33.154:56089/order_xa,lockKey=order_tbl:7
,clientIp:14.30.2.158,vgroup:my_test_tx_group
2020-09-19 03:54:56.303 INFO [ServerHandlerThread_1_500]io.seata.server.coordinator.AbstractCore.lambda$branchRegister$0:86 -Successfully register branch xid = 172.18.0.6:8091:2054185921, branchId = 2054185924
2020-09-19 03:54:56.304 INFO [batchLoggerPrint_1]io.seata.core.rpc.DefaultServerMessageListenerImpl.run:206 -SeataMergeMessage xid=172.18.0.6:8091:2054185921,branchType=AT,resourceId=jdbc:mysql://121.36.33.154:56088/account_xa,lockKey=account_tbl:7
,clientIp:14.30.2.158,vgroup:my_test_tx_group
2020-09-19 03:54:56.804 INFO [batchLoggerPrint_1]io.seata.core.rpc.DefaultServerMessageListenerImpl.run:206 -SeataMergeMessage xid=172.18.0.6:8091:2054185921,extraData=null
,clientIp:14.30.2.158,vgroup:my_test_tx_group
2020-09-19 03:54:57.411 INFO [AsyncCommitting_1]io.seata.server.coordinator.DefaultCore.doGlobalCommit:238 -Global[172.18.0.6:8091:2054185921] committing is successfully done.
```

```bash
### business-xa
2020-09-19 11:54:51.413  INFO 1296 --- [io-56090-exec-5] i.seata.tm.api.DefaultGlobalTransaction  : Begin new global transaction [172.18.0.6:8091:2054185921]
2020-09-19 11:54:51.414  INFO 1296 --- [io-56090-exec-5] io.seata.sample.service.BusinessService  : parameter => userId=U100000,commodityCode=C100000,orderCount=30,rollback=false
2020-09-19 11:54:51.414  INFO 1296 --- [io-56090-exec-5] io.seata.sample.service.BusinessService  : New Transaction Begins: 172.18.0.6:8091:2054185921
2020-09-19 11:54:56.573  INFO 1296 --- [io-56090-exec-5] i.seata.tm.api.DefaultGlobalTransaction  : [172.18.0.6:8091:2054185921] commit status: Committed

### storage-xa
2020-09-19 11:54:51.422  INFO 11328 --- [io-56091-exec-3] io.seata.sample.service.StorageService   : parameter => commodityCode=C100000,count=30
2020-09-19 11:54:51.422  INFO 11328 --- [io-56091-exec-3] io.seata.sample.service.StorageService   : deduct storage balance in transaction: 172.18.0.6:8091:2054185921
2020-09-19 11:54:51.514 ERROR 11328 --- [io-56091-exec-3] c.a.druid.pool.DruidAbstractDataSource   : discard long time none received connection. , jdbcUrl : jdbc:mysql://121.36.33.154:56091/storage_xa?characterEncoding=utf-8&useUnicode=true&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true, jdbcUrl : jdbc:mysql://121.36.33.154:56091/storage_xa?characterEncoding=utf-8&useUnicode=true&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true, lastPacketReceivedIdleMillis : 148257
2020-09-19 11:54:53.117  WARN 11328 --- [io-56091-exec-3] c.a.c.seata.web.SeataHandlerInterceptor  : xid in change during RPC from 172.18.0.6:8091:2054185921 to null
2020-09-19 11:54:57.034  INFO 11328 --- [h_RMROLE_1_2_16] i.s.c.r.p.c.RmBranchCommitProcessor      : rm client handle branch commit process:xid=172.18.0.6:8091:2054185921,branchId=2054185922,branchType=AT,resourceId=jdbc:mysql://121.36.33.154:56091/storage_xa,applicationData=null
2020-09-19 11:54:57.034  INFO 11328 --- [h_RMROLE_1_2_16] io.seata.rm.AbstractRMHandler            : Branch committing: 172.18.0.6:8091:2054185921 2054185922 jdbc:mysql://121.36.33.154:56091/storage_xa null
2020-09-19 11:54:57.034  INFO 11328 --- [h_RMROLE_1_2_16] io.seata.rm.AbstractRMHandler            : Branch commit result: PhaseTwo_Committed

### order-xa
2020-09-19 11:54:53.125  INFO 8420 --- [io-56089-exec-3] io.seata.sample.service.OrderService     : parameter => userId=U100000,commodityCode=C100000,count=30
2020-09-19 11:54:53.125  INFO 8420 --- [io-56089-exec-3] io.seata.sample.service.OrderService     : create order in transaction: 172.18.0.6:8091:2054185921
2020-09-19 11:54:53.227 ERROR 8420 --- [io-56089-exec-3] c.a.druid.pool.DruidAbstractDataSource   : discard long time none received connection. , jdbcUrl : jdbc:mysql://121.36.33.154:56089/order_xa?characterEncoding=utf-8&useUnicode=true&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true, jdbcUrl : jdbc:mysql://121.36.33.154:56089/order_xa?characterEncoding=utf-8&useUnicode=true&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true, lastPacketReceivedIdleMillis : 149450
2020-09-19 11:54:56.519  WARN 8420 --- [io-56089-exec-3] c.a.c.seata.web.SeataHandlerInterceptor  : xid in change during RPC from 172.18.0.6:8091:2054185921 to null
2020-09-19 11:54:57.081  INFO 8420 --- [h_RMROLE_1_2_16] i.s.c.r.p.c.RmBranchCommitProcessor      : rm client handle branch commit process:xid=172.18.0.6:8091:2054185921,branchId=2054185923,branchType=AT,resourceId=jdbc:mysql://121.36.33.154:56089/order_xa,applicationData=null
2020-09-19 11:54:57.081  INFO 8420 --- [h_RMROLE_1_2_16] io.seata.rm.AbstractRMHandler            : Branch committing: 172.18.0.6:8091:2054185921 2054185923 jdbc:mysql://121.36.33.154:56089/order_xa null
2020-09-19 11:54:57.081  INFO 8420 --- [h_RMROLE_1_2_16] io.seata.rm.AbstractRMHandler            : Branch commit result: PhaseTwo_Committed

### account-xa
2020-09-19 11:54:54.793 ERROR 12080 --- [io-56088-exec-3] c.a.druid.pool.DruidAbstractDataSource   : discard long time none received connection. , jdbcUrl : jdbc:mysql://121.36.33.154:56088/account_xa?characterEncoding=utf-8&useUnicode=true&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true, jdbcUrl : jdbc:mysql://121.36.33.154:56088/account_xa?characterEncoding=utf-8&useUnicode=true&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true, lastPacketReceivedIdleMillis : 151285
2020-09-19 11:54:55.707  INFO 12080 --- [io-56088-exec-3] io.seata.sample.service.AccountService   : parameter => userId=U100000,money=3000
2020-09-19 11:54:55.707  INFO 12080 --- [io-56088-exec-3] io.seata.sample.service.AccountService   : reduce account balance in transaction: 172.18.0.6:8091:2054185921
2020-09-19 11:54:56.028  INFO 12080 --- [io-56088-exec-3] io.seata.sample.service.AccountService   : balance after transaction: 7000
2020-09-19 11:54:56.517  WARN 12080 --- [io-56088-exec-3] c.a.c.seata.web.SeataHandlerInterceptor  : xid in change during RPC from 172.18.0.6:8091:2054185921 to null
2020-09-19 11:54:57.129  INFO 12080 --- [h_RMROLE_1_2_16] i.s.c.r.p.c.RmBranchCommitProcessor      : rm client handle branch commit process:xid=172.18.0.6:8091:2054185921,branchId=2054185924,branchType=AT,resourceId=jdbc:mysql://121.36.33.154:56088/account_xa,applicationData=null
2020-09-19 11:54:57.129  INFO 12080 --- [h_RMROLE_1_2_16] io.seata.rm.AbstractRMHandler            : Branch committing: 172.18.0.6:8091:2054185921 2054185924 jdbc:mysql://121.36.33.154:56088/account_xa null
2020-09-19 11:54:57.129  INFO 12080 --- [h_RMROLE_1_2_16] io.seata.rm.AbstractRMHandler            : Branch commit result: PhaseTwo_Committed
```



```
docker start mysql_dtx_seata_official_order_xa;
docker start mysql_dtx_seata_official_storage_xa;
docker start mysql_dtx_seata_official_account_xa;
docker start seata_server;
```

