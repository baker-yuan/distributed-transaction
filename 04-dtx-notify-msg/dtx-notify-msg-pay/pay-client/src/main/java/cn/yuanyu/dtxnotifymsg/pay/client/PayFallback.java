package cn.yuanyu.dtxnotifymsg.pay.client;


import cn.yuanyu.dtxnotifymsg.pay.entity.AccountPay;
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
