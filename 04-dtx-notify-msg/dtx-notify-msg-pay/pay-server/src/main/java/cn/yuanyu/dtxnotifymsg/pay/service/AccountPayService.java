package cn.yuanyu.dtxnotifymsg.pay.service;

import cn.yuanyu.dtxnotifymsg.pay.entity.AccountPay;


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
