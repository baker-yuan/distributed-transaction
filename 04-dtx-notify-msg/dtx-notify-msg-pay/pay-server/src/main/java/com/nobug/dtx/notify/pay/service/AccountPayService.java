package com.nobug.dtx.notify.pay.service;

import com.nobug.dtx.notify.pay.entity.AccountPay;

/**
 * Created by Administrator.
 */
public interface AccountPayService {

    /**
     * 充值
     */
     AccountPay insertAccountPay(AccountPay accountPay);


    /**
     * 查询充值结果
     */
     AccountPay getAccountPay(String txNo);
}
