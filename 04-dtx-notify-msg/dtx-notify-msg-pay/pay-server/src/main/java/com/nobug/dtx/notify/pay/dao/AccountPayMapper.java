package com.nobug.dtx.notify.pay.dao;

import com.nobug.dtx.notify.pay.entity.AccountPay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface AccountPayMapper {

    /**
     * 插入充值记录
     * @param id 事务号
     * @param accountNo 账号
     * @param pay_amount 充值金额
     * @param result 充值结果
     */
    int insertAccountPay(@Param("id") String id, @Param("accountNo") String accountNo, @Param("payAmount") Double pay_amount, @Param("result") String result);


    /**
     * 查询充值的结果
     * @param txNo 事务号
     */
    AccountPay findByIdTxNo(@Param("txNo") String txNo);



}
