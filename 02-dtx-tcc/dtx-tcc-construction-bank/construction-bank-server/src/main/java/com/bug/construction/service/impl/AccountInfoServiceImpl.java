package com.bug.construction.service.impl;


import com.bug.construction.dao.AccountInfoMapper;
import com.bug.construction.dao.LogMapper;
import com.bug.construction.entity.AccountInfo;
import com.bug.construction.service.AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.core.concurrent.threadlocal.HmilyTransactionContextLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {

    @Autowired
    AccountInfoMapper accountInfoMapper;

    @Autowired
    LogMapper logMapper;

    /**
     * 转入
     */
    @Override
    @Hmily(confirmMethod="commit", cancelMethod="rollback")
    public void transferIn(String to, Double amount) {
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("商业银行try执行...xid:{}",transId);
    }


    /**
     * confirm方法（这个方法发生异常不会回滚）
     */
    @Transactional
    public void commit(String to, Double amount){
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("商业银行confirm开始执行...xid:{}",transId);

        //1.confirm幂等校验
        if(logMapper.isExistConfirm(transId)>0){
            log.info("商业银行confirm已经执行，无需重复执行...xid:{}",transId);
            return ;
        }

        //这里的异常，并不会回滚
        if (amount == 3) {
            throw new RuntimeException("商业银行发生异常啦...，xid:{}" + transId);
        }

        //2.正式增加金额
        accountInfoMapper.addAccountBalance(to,amount);

        //增加一条confirm日志，用于幂等
        logMapper.addConfirm(transId);

        log.info("商业银行confirm结束执行...xid:{}",transId);
    }

    public void rollback(String accountNo, Double amount){
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("商业银行cancel执行...xid:{}",transId);
    }














    /**
     * 查询所有用户
     */
    @Override
    public List<AccountInfo> getAllUserInfo() {
        return accountInfoMapper.getAllUserInfo();
    }
}
