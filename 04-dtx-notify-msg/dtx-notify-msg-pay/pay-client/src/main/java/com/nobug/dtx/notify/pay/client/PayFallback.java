package com.nobug.dtx.notify.pay.client;


import com.nobug.dtx.notify.pay.entity.AccountPay;
import org.springframework.stereotype.Component;


/**
 * @author yuanyu
 */
@Component
public class PayFallback implements PayClient {
    @Override
    public AccountPay payResult(String txNo) {
        AccountPay accountPay = new AccountPay();
        accountPay.setResult("fail");
        return accountPay;
    }
}
