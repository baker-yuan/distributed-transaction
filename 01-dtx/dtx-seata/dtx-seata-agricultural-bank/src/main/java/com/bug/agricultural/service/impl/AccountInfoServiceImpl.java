package com.bug.agricultural.service.impl;


import com.bug.agricultural.dao.AccountInfoMapper;
import com.bug.agricultural.entity.AccountInfo;
import com.bug.agricultural.service.AccountInfoService;
import com.bug.agricultural.client.ConstructionBankClient;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author yuanyu
 */
@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {

    @Autowired
    AccountInfoMapper accountInfoMapper;

    @Autowired
    ConstructionBankClient constructionBankClient;

    /**
     * @param from   转账人姓名
     * @param to     收款人姓名
     * @param amount 转账金额
     */
    @Transactional
    @GlobalTransactional//开启全局事务
    @Override
    public void transferOut(String from, String to, Double amount) {
        log.info("bank1 service begin,XID：{}", RootContext.getXID());

        //扣减from的金额
        AccountInfo fromUser = accountInfoMapper.getUserInfoByName(from);
        if (fromUser.getAccountBalance() - amount >= 0) {
            accountInfoMapper.updateAccountBalance(from, amount * -1);
        } else {
            throw new RuntimeException("你转你🐎呢");
        }

        //调用construction微服务转账
        String transfer = constructionBankClient.transferIn(to, amount);
        //调用construction微服务异常
        if ("fallback".equals(transfer)) {
            throw new RuntimeException("转账给" + to + "失败");
        }
        if (amount == 2) {
            //人为制造异常
            throw new RuntimeException("bank1 make exception..");
        }
    }


    /**
     * 查询所有用户
     */
    @Override
    public List<AccountInfo> getAllUserInfo() {
        return accountInfoMapper.getAllUserInfo();
    }

    /**
     * 根据用户名查询用户
     */
    @Override
    public AccountInfo getUserInfoByName(String accountName) {
        return accountInfoMapper.getUserInfoByName(accountName);
    }
}
