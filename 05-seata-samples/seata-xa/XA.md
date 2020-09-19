# 1 购买商品业务正常流程

1. 该商品库存减少
2. 订单数据库插入该订单
3. 用户余额减少

https://www.imooc.com/read/43/article/703 

```sql
-- 用户购买了30个单价为100元的商品

-- 1、减库存 storage_xa
use storage_xa;
update storage_tbl set count = count - 30 where commodity_code = 'C100000';

-- 2、插入订单数据 order_xa
use order_xa;
insert order_tbl(user_id, commodity_code, count, money) values ('U100000', 'C100000', 30, 3000);

-- 3、用户余额减少 account_xa
use account_xa;
update account_tbl set money = money - 3000 where user_id = 'U100000';
```

## 2 MySQL中使用XA

## 2.1 启动分支事务 

```bash
xa start 'a','a_1';
```

‘a’,‘a_1’ 表示 xid，

a 表示 gtrid，为分布式事务标识符，相同的分布式事务使用相同的 gtrid。

a_1 表示 bqual，为分支限定符，分布式事务中的每一个分支事务的 bqual 必须不同。

## 2.2 结束分支事务 

```bash
xa end 'a','a_1';
```

## 2.3 进入准备状态 

```bash
xa prepare 'a','a_1';
```

## 2.4 提交分支事务 

```bash
xa commit 'a','a_1';
```

## 2.5 回滚分支事务 

```bash
xa rollback 'a','a_1';
```

返回当前数据库中处于 prepare 状态的分支事务的详细信息 

```bash
xa recover;
```

# 3 案例

| storage_xa数据库                                             | order_xa数据库                                               | account_xa数据库                                             |
| :----------------------------------------------------------- | :----------------------------------------------------------- | ------------------------------------------------------------ |
| use storage_xa;                                              | use order_xa;                                                | use account_xa;                                              |
| truncate table storage_tbl;                                  | truncate table order_tbl;                                    | truncate table account_tbl;                                  |
| insert into storage_tbl(commodity_code, count) values ('C100000', '100'); |                                                              | insert into account_tbl(user_id, money) values ('U100000', '10000'); |
| xa start 'storage_order_account_14582147895','storage';      | xa start 'storage_order_account_14582147895','order';        | xa start 'storage_order_account_14582147895','account';      |
| update storage_tbl set count = count - 30 where commodity_code = 'C100000'; | insert order_tbl(user_id, commodity_code, count, money) values ('U100000', 'C100000', 30, 3000); | update account_tbl set money = money - 3000 where user_id = 'U100000'; |
| xa prepare 'storage_order_account_14582147895','storage';    | xa prepare 'storage_order_account_14582147895','order';      | xa prepare 'storage_order_account_14582147895','account';    |
| xa recover \G                                                | xa recover \G                                                | xa recover \G                                                |
| xa commit 'storage_order_account_14582147895','storage';     | xa commit 'storage_order_account_14582147895','order';       | xa commit 'storage_order_account_14582147895','account';     |
|                                                              |                                                              |                                                              |
| xa end 'storage_order_account_14582147895','storage';        | xa end 'storage_order_account_14582147895','order';          | xa end 'storage_order_account_14582147895','account';        |







