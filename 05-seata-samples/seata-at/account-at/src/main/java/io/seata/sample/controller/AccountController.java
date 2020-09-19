package io.seata.sample.controller;

import io.seata.sample.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.seata.sample.service.AccountService.FAIL;
import static io.seata.sample.service.AccountService.SUCCESS;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * 用户扣减指定金额
     *
     * @param userId 用户ID
     * @param money  扣减金额
     */
    @GetMapping(value = "/reduce")
    public String reduce(String userId, int money) {
        try {
            accountService.reduce(userId, money);
        } catch (Exception exx) {
            exx.printStackTrace();
            return FAIL;
        }
        return SUCCESS;
    }
}
