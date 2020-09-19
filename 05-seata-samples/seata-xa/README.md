1.  https://github.com/seata/seata-samples/tree/master/seata-xa 

# Sample of Seata XA mode

Spring Cloud 中使用 Seata，使用 Feign 实现远程调用，使用 Spring JDBC 访问 MySQL 数据库

# 四个服务分别创建四个数据库

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

## account-xa

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

## order-xa

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

## storage-xa

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

## XA 模式与 AT 模式

只要切换数据源代理类型，该样例即可在 XA 模式和 AT 模式之间切换。

DataSourceConfiguration

XA 模式使用 DataSourceProxyXA

```java
@Bean("dataSourceProxy")
public DataSource dataSource(DruidDataSource druidDataSource) {
    // DataSourceProxyXA for XA mode
    // io.seata.rm.datasource.xa.DataSourceProxyXA
    return new DataSourceProxyXA(druidDataSource);
}
```

AT 模式使用 DataSourceProxy

```java
@Bean("dataSourceProxy")
public DataSource dataSource(DruidDataSource druidDataSource) {
    // DataSourceProxy for AT mode
    // io.seata.rm.datasource.DataSourceProxy
    return new DataSourceProxy(druidDataSource);
}
```

*当然，AT 模式需要在数据库中建立 undo_log 表。（XA 模式是不需要这个表的）*


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
business-xa
2020-09-19 11:15:54.781  INFO 15400 --- [io-56090-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2020-09-19 11:15:54.782  INFO 15400 --- [io-56090-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2020-09-19 11:15:54.791  INFO 15400 --- [io-56090-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 9 ms
2020-09-19 11:15:54.841  INFO 15400 --- [io-56090-exec-1] io.seata.tm.TransactionManagerHolder     : TransactionManager Singleton io.seata.tm.DefaultTransactionManager@3c2a49
2020-09-19 11:15:54.853  INFO 15400 --- [Send_TMROLE_1_1] i.s.c.r.netty.NettyClientChannelManager  : will connect to 120.79.202.181:60000
2020-09-19 11:15:54.856  INFO 15400 --- [Send_TMROLE_1_1] i.s.core.rpc.netty.NettyPoolableFactory  : NettyPool create channel to transactionRole:TMROLE,address:120.79.202.181:60000,msg:< RegisterTMRequest{applicationId='business-xa', transactionServiceGroup='my_test_tx_group'} >
2020-09-19 11:15:57.296  INFO 15400 --- [Send_TMROLE_1_1] i.s.c.rpc.netty.TmNettyRemotingClient    : register TM success. client version:1.3.0, server version:0.7.1,channel:[id: 0x98b66a5c, L:/192.168.43.252:6336 - R:/120.79.202.181:60000]
2020-09-19 11:15:57.303  INFO 15400 --- [Send_TMROLE_1_1] i.s.core.rpc.netty.NettyPoolableFactory  : register success, cost 116 ms, version:0.7.1,role:TMROLE,channel:[id: 0x98b66a5c, L:/192.168.43.252:6336 - R:/120.79.202.181:60000]
2020-09-19 11:15:57.441  INFO 15400 --- [io-56090-exec-1] i.seata.tm.api.DefaultGlobalTransaction  : Begin new global transaction [172.17.17.111:60000:2054213281]
2020-09-19 11:15:57.445  INFO 15400 --- [io-56090-exec-1] io.seata.sample.service.BusinessService  : parameter => userId=U100000,commodityCode=C100000,orderCount=30,rollback=false
2020-09-19 11:15:57.445  INFO 15400 --- [io-56090-exec-1] io.seata.sample.service.BusinessService  : New Transaction Begins: 172.17.17.111:60000:2054213281
2020-09-19 11:16:01.734  INFO 15400 --- [io-56090-exec-1] i.seata.tm.api.DefaultGlobalTransaction  : [172.17.17.111:60000:2054213281] commit status: Committed

storage-xa
2020-09-19 11:15:57.501  INFO 1412 --- [io-56091-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2020-09-19 11:15:57.502  INFO 1412 --- [io-56091-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2020-09-19 11:15:57.510  INFO 1412 --- [io-56091-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 8 ms
2020-09-19 11:15:57.542  INFO 1412 --- [io-56091-exec-1] io.seata.sample.service.StorageService   : parameter => commodityCode=C100000,count=30
2020-09-19 11:15:57.543  INFO 1412 --- [io-56091-exec-1] io.seata.sample.service.StorageService   : deduct storage balance in transaction: 172.17.17.111:60000:2054213281
2020-09-19 11:15:58.768  WARN 1412 --- [io-56091-exec-1] c.a.c.seata.web.SeataHandlerInterceptor  : xid in change during RPC from 172.17.17.111:60000:2054213281 to null
2020-09-19 11:16:10.423  INFO 1412 --- [h_RMROLE_1_1_16] i.s.c.r.p.c.RmBranchCommitProcessor      : rm client handle branch commit process:xid=172.17.17.111:60000:2054213281,branchId=2054213282,branchType=AT,resourceId=jdbc:mysql://121.36.33.154:56091/storage_xa,applicationData=null
2020-09-19 11:16:10.425  INFO 1412 --- [h_RMROLE_1_1_16] io.seata.rm.AbstractRMHandler            : Branch committing: 172.17.17.111:60000:2054213281 2054213282 jdbc:mysql://121.36.33.154:56091/storage_xa null
2020-09-19 11:16:10.426  INFO 1412 --- [h_RMROLE_1_1_16] io.seata.rm.AbstractRMHandler            : Branch commit result: PhaseTwo_Committed

order-xa
2020-09-19 11:15:57.402  INFO 11844 --- [eoutChecker_1_1] i.s.c.r.netty.NettyClientChannelManager  : will connect to 120.79.202.181:60000
2020-09-19 11:15:57.402  INFO 11844 --- [eoutChecker_1_1] i.s.core.rpc.netty.NettyPoolableFactory  : NettyPool create channel to transactionRole:TMROLE,address:120.79.202.181:60000,msg:< RegisterTMRequest{applicationId='order-xa', transactionServiceGroup='my_test_tx_group'} >
2020-09-19 11:15:57.497  INFO 11844 --- [eoutChecker_1_1] i.s.c.rpc.netty.TmNettyRemotingClient    : register TM success. client version:1.3.0, server version:0.7.1,channel:[id: 0x52f0fa7f, L:/192.168.43.252:6337 - R:/120.79.202.181:60000]
2020-09-19 11:15:57.497  INFO 11844 --- [eoutChecker_1_1] i.s.core.rpc.netty.NettyPoolableFactory  : register success, cost 46 ms, version:0.7.1,role:TMROLE,channel:[id: 0x52f0fa7f, L:/192.168.43.252:6337 - R:/120.79.202.181:60000]
2020-09-19 11:15:58.831  INFO 11844 --- [io-56089-exec-3] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2020-09-19 11:15:58.831  INFO 11844 --- [io-56089-exec-3] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2020-09-19 11:15:58.838  INFO 11844 --- [io-56089-exec-3] o.s.web.servlet.DispatcherServlet        : Completed initialization in 7 ms
2020-09-19 11:15:58.869  INFO 11844 --- [io-56089-exec-3] io.seata.sample.service.OrderService     : parameter => userId=U100000,commodityCode=C100000,count=30
2020-09-19 11:15:58.869  INFO 11844 --- [io-56089-exec-3] io.seata.sample.service.OrderService     : create order in transaction: 172.17.17.111:60000:2054213281
2020-09-19 11:16:01.654  WARN 11844 --- [io-56089-exec-3] c.a.c.seata.web.SeataHandlerInterceptor  : xid in change during RPC from 172.17.17.111:60000:2054213281 to null
2020-09-19 11:16:10.495  INFO 11844 --- [h_RMROLE_1_1_16] i.s.c.r.p.c.RmBranchCommitProcessor      : rm client handle branch commit process:xid=172.17.17.111:60000:2054213281,branchId=2054213283,branchType=AT,resourceId=jdbc:mysql://121.36.33.154:56089/order_xa,applicationData=null
2020-09-19 11:16:10.497  INFO 11844 --- [h_RMROLE_1_1_16] io.seata.rm.AbstractRMHandler            : Branch committing: 172.17.17.111:60000:2054213281 2054213283 jdbc:mysql://121.36.33.154:56089/order_xa null
2020-09-19 11:16:10.499  INFO 11844 --- [h_RMROLE_1_1_16] io.seata.rm.AbstractRMHandler            : Branch commit result: PhaseTwo_Committed

account-xa
2020-09-19 11:16:00.149  INFO 16080 --- [io-56088-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2020-09-19 11:16:00.149  INFO 16080 --- [io-56088-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2020-09-19 11:16:00.159  INFO 16080 --- [io-56088-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 10 ms
2020-09-19 11:16:00.314  INFO 16080 --- [io-56088-exec-1] io.seata.sample.service.AccountService   : parameter => userId=U100000,money=3000
2020-09-19 11:16:00.314  INFO 16080 --- [io-56088-exec-1] io.seata.sample.service.AccountService   : reduce account balance in transaction: 172.17.17.111:60000:2054213281
2020-09-19 11:16:01.070  INFO 16080 --- [io-56088-exec-1] io.seata.sample.service.AccountService   : balance after transaction: 7000
2020-09-19 11:16:01.628  WARN 16080 --- [io-56088-exec-1] c.a.c.seata.web.SeataHandlerInterceptor  : xid in change during RPC from 172.17.17.111:60000:2054213281 to null
2020-09-19 11:16:05.731  INFO 16080 --- [eoutChecker_1_1] i.s.c.r.netty.NettyClientChannelManager  : will connect to 120.79.202.181:60000
2020-09-19 11:16:05.731  INFO 16080 --- [eoutChecker_1_1] i.s.core.rpc.netty.NettyPoolableFactory  : NettyPool create channel to transactionRole:TMROLE,address:120.79.202.181:60000,msg:< RegisterTMRequest{applicationId='account-xa', transactionServiceGroup='my_test_tx_group'} >
2020-09-19 11:16:05.993  INFO 16080 --- [eoutChecker_1_1] i.s.c.rpc.netty.TmNettyRemotingClient    : register TM success. client version:1.3.0, server version:0.7.1,channel:[id: 0x114a442d, L:/192.168.43.252:6342 - R:/120.79.202.181:60000]
2020-09-19 11:16:05.993  INFO 16080 --- [eoutChecker_1_1] i.s.core.rpc.netty.NettyPoolableFactory  : register success, cost 105 ms, version:0.7.1,role:TMROLE,channel:[id: 0x114a442d, L:/192.168.43.252:6342 - R:/120.79.202.181:60000]
2020-09-19 11:16:10.568  INFO 16080 --- [h_RMROLE_1_1_16] i.s.c.r.p.c.RmBranchCommitProcessor      : rm client handle branch commit process:xid=172.17.17.111:60000:2054213281,branchId=2054213284,branchType=AT,resourceId=jdbc:mysql://121.36.33.154:56088/account_xa,applicationData=null
2020-09-19 11:16:10.570  INFO 16080 --- [h_RMROLE_1_1_16] io.seata.rm.AbstractRMHandler            : Branch committing: 172.17.17.111:60000:2054213281 2054213284 jdbc:mysql://121.36.33.154:56088/account_xa null
2020-09-19 11:16:10.571  INFO 16080 --- [h_RMROLE_1_1_16] io.seata.rm.AbstractRMHandler            : Branch commit result: PhaseTwo_Committed
```











```
docker start mysql_dtx_seata_official_order_xa;
docker start mysql_dtx_seata_official_storage_xa;
docker start mysql_dtx_seata_official_account_xa;
docker start seata_server;
```

```
sh seata-server.sh -p 60000 -m file
```

https://github.com/alibaba/druid/issues/2512 

