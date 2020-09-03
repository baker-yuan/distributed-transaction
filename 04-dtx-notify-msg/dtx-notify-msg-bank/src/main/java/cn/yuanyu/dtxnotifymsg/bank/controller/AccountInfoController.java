package cn.yuanyu.dtxnotifymsg.bank.controller;

import cn.yuanyu.dtxnotifymsg.bank.entity.AccountInfo;
import cn.yuanyu.dtxnotifymsg.bank.service.AccountInfoService;
import com.nobug.dtx.notify.pay.entity.AccountPay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/account")
public class AccountInfoController {

    @Autowired
    private AccountInfoService accountInfoService;


    /**
     * http://localhost:56087/bank/account/payresult/
     * 主动查询充值结果
     * @param txNo 事务号
     */
    @GetMapping(value = "/payresult/{txNo}")
    public AccountPay result(@PathVariable("txNo") String txNo){
        return accountInfoService.queryPayResult(txNo);
    }



    /**
     * http://localhost:56087/bank/account/%E5%BC%A0%E4%B8%89
     */
    @GetMapping("/{accountName}")
    AccountInfo getUserInfoByName(@PathVariable("accountName") String accountName){
        return accountInfoService.getUserInfoByName(accountName);
    }



    /**
     * http://localhost:56087/bank/account/all
     */
    @GetMapping("/all")
    public List<AccountInfo> getAllUserInfo(){
        return accountInfoService.getAllUserInfo();
    }



}
