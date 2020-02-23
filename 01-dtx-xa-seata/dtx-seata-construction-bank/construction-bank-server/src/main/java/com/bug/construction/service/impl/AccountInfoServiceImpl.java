package com.bug.construction.service.impl;


import com.bug.construction.dao.AccountInfoMapper;
import com.bug.construction.entity.AccountInfo;
import com.bug.construction.service.AccountInfoService;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {

    @Autowired
    AccountInfoMapper accountInfoMapper;

    /**
     * 转入
     *
     * @param to     收款人
     * @param amount 收款金额
     */
    @Override
    @Transactional
    public void transferIn(String to, Double amount) {
        log.info("bank2 service begin,XID：{}", RootContext.getXID());
        AccountInfo toUser = accountInfoMapper.getUserInfoByName(to);
        if (toUser == null) {
            throw new RuntimeException("用户" + to + "不存在");
        }
        //增加金额
        accountInfoMapper.updateAccountBalance(to, amount);
        if (amount == 3) {
            //人为制造异常
            throw new RuntimeException("商业银行转账发生异常...");
        }
    }

    /**
     * 查询所有用户
     */
    @Override
    public List<AccountInfo> getAllUserInfo() {
        return accountInfoMapper.getAllUserInfo();
    }
}
