package com.bug.agricultural.controller;


import com.bug.agricultural.entity.AccountInfo;
import com.bug.agricultural.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author yuanyu
 */
@RestController
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    AccountInfoService accountInfoService;

    /**
     * http://localhost:56081/agricultural/transfer/out?from=张三&to=李四&amount=10
     */
    /**
     * 转出接口
     * @param from 转账人姓名
     * @param to 收款人姓名
     * @param amount 转账金额
     */
    @PostMapping("/out")
    public String transferOut(String from, String to, Double amount) {
        accountInfoService.transferOut(from, to, amount);
        return "agricultural" + amount;
    }


    /**
     * http://localhost:56081/agricultural/transfer/张三
     */
    @GetMapping("/{accountName}")
    AccountInfo getUserInfoByName(@PathVariable("accountName") String accountName){
        return accountInfoService.getUserInfoByName(accountName);
    }



    /**
     * http://localhost:56081/agricultural/transfer/all
     */
    @GetMapping("/all")
    public List<AccountInfo> getAllUserInfo(){
        return accountInfoService.getAllUserInfo();
    }

}
