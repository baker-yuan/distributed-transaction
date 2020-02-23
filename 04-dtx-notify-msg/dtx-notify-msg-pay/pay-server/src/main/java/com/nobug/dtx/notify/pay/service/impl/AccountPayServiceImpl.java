package com.nobug.dtx.notify.pay.service.impl;

import com.nobug.dtx.notify.pay.dao.AccountPayMapper;
import com.nobug.dtx.notify.pay.entity.AccountPay;
import com.nobug.dtx.notify.pay.service.AccountPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountPayServiceImpl implements AccountPayService {

    @Autowired
    AccountPayMapper accountPayDao;

    @Autowired
    RocketMQTemplate mqTemplate;


    /**
     * 插入充值记录
     */
    @Override
    public AccountPay insertAccountPay(AccountPay accountPay) {
        int success = accountPayDao.insertAccountPay(accountPay.getId(), accountPay.getAccountName(), accountPay.getPayAmount(), "success");
        if (success > 0) {
            //发送通知,使用普通消息发送通知
            accountPay.setResult("success");
            mqTemplate.convertAndSend("topic_notify_msg", accountPay);
            return accountPay;
        }
        return null;
    }

    /**
     * 查询充值记录，接收通知方调用此方法来查询充值结果
     */
    @Override
    public AccountPay getAccountPay(String txNo) {
        return accountPayDao.findByIdTxNo(txNo);
    }
}
