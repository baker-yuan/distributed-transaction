package cn.yuanyu.dtxnotifymsg.pay.controller;

import cn.yuanyu.dtxnotifymsg.pay.entity.AccountPay;
import cn.yuanyu.dtxnotifymsg.pay.service.AccountPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
public class AccountPayController {

    @Autowired
    AccountPayService accountPayService;

    //{
    //  "accountName": "张三",
    //  "payAmount": 10
    //}

    /**
     * 充值
     * http://localhost:56088/pay/paydo
     */
    @PostMapping(value = "/paydo")
    public AccountPay pay(@RequestBody AccountPay accountPay) {
        // 生成事务编号
        String txNo = UUID.randomUUID().toString();
        accountPay.setId(txNo);
        return accountPayService.insertAccountPay(accountPay);
    }

    /**
     * 查询充值结果
     *
     * @param txNo 事务号
     */
    @GetMapping(value = "/payresult/{txNo}")
    public AccountPay payresult(@PathVariable("txNo") String txNo) {
        return accountPayService.getAccountPay(txNo);
    }
}
