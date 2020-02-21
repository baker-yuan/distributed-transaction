package com.bug.construction.controller;


import com.bug.construction.entity.AccountInfo;
import com.bug.construction.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 转入接口
     *
     * @param to     收款人
     * @param amount 收款金额
     */
    @PostMapping("/in")
    public String transferIn(String to, Double amount) {
        accountInfoService.transferIn(to, amount);
        return "商业银行收款成功";
    }


    /**
     * http://localhost:56082/construction/transfer/all
     */
    @GetMapping("/all")
    public List<AccountInfo> getAllUserInfo() {
        return accountInfoService.getAllUserInfo();
    }


}
