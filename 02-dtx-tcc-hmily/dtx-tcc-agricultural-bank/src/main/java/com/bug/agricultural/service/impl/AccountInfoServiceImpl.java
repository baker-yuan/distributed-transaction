package com.bug.agricultural.service.impl;


import com.bug.agricultural.dao.AccountInfoMapper;
import com.bug.agricultural.dao.LogMapper;
import com.bug.agricultural.entity.AccountInfo;
import com.bug.agricultural.service.AccountInfoService;
import com.bug.construction.client.ConstructionBankClient;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.core.concurrent.threadlocal.HmilyTransactionContextLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author yuanyu
 */
@Slf4j
@Service
public class AccountInfoServiceImpl implements AccountInfoService {
    @Autowired
    AccountInfoMapper accountInfoMapper;

    @Autowired
    LogMapper logMapper;

    @Autowired
    ConstructionBankClient constructionBankClient;


    /**
     * 转账方法，就是tcc的try方法
     *
     * @param from   转账人姓名
     * @param to     收款人姓名
     * @param amount 转账金额
     */
    @Override
    @Transactional
    //只要标记@Hmily就是try方法，在注解中指定confirm、cancel两个方法的名字
    @Hmily(confirmMethod = "commit", cancelMethod = "rollback")
    public void transferOut(String from, String to, Double amount) {
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("农业银行try开始执行...xid:{}", transId);

        //1.try幂等校验
        //幂等判断 判断local_try_log表中是否有try日志记录，如果有则不再执行
        if (logMapper.isExistTry(transId) > 0) {
            log.info("农业银行try已经执行，无需重复执行，xid:{}", transId);
            return;
        }

        //2.try悬挂处理，如果cancel、confirm有一个已经执行了，try不再执行
        if (logMapper.isExistConfirm(transId) > 0 || logMapper.isExistCancel(transId) > 0) {
            log.info("农业银行try悬挂处理cancel或confirm已经执行，不允许执行try,xid:{}", transId);
            return;
        }

        //3.检查余额是够扣减金额
        //4.扣减金额
        if (accountInfoMapper.subtractAccountBalance(from, amount) <= 0) {
            //扣减失败
            throw new RuntimeException("农业银行try扣减金额失败，xid:{}" + transId);
        }

        //插入try执行记录,用于幂等判断
        logMapper.addTry(transId);

        //远程调用李四，转账
        if (!constructionBankClient.transferIn(to, amount)) {
            throw new RuntimeException("农业银行远程调用建设银行微服务失败，xid:{}" + transId);
        }

        if (amount == 2) {
            throw new RuntimeException("农业银行发生异常啦...，xid:{}" + transId);
        }

        log.info("农业银行try结束执行...xid:{}", transId);
    }

    /**
     * confirm方法
     */
    @Transactional
    public void commit(String from, String to, Double amount) {
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("农业银行confirm执行...xid:{},accountName:{},amount:{}", transId, from, amount);
    }


    /**
     * cancel方法
     */
    @Transactional
    public void rollback(String from, String to, Double amount) {
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("农业银行cancel开始执行...xid:{}", transId);

        //1.cancel幂等校验
        if (logMapper.isExistCancel(transId) > 0) {
            log.info("农业银行cancel已经执行，无需重复执行,xid:{}", transId);
            return;
        }

        //2.cancel空回滚处理，如果try没有执行，cancel不允许执行
        if (logMapper.isExistTry(transId) <= 0) {
            log.info("农业银行 空回滚处理，try没有执行，不允许cancel执行，xid:{}", transId);
            return;
        }

        //3.增加可用余额
        accountInfoMapper.addAccountBalance(from, amount);

        //插入一条cancel的执行记录
        logMapper.addCancel(transId);

        log.info("农业银行cancel结束执行...xid:{}", transId);
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
