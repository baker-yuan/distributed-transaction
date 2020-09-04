package cn.yuanyu.dtxnotifymsg.bank.service.impl;

import cn.yuanyu.dtxnotifymsg.bank.dao.AccountInfoMapper;
import cn.yuanyu.dtxnotifymsg.bank.dao.JudgeMapper;
import cn.yuanyu.dtxnotifymsg.bank.entity.AccountInfo;
import cn.yuanyu.dtxnotifymsg.bank.model.AccountChangeEvent;
import cn.yuanyu.dtxnotifymsg.bank.service.AccountInfoService;
import cn.yuanyu.dtxnotifymsg.pay.client.PayClient;
import cn.yuanyu.dtxnotifymsg.pay.entity.AccountPay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {

    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Autowired
    private JudgeMapper judgeMapper;

    @Autowired
    private PayClient payClient;

    /**
     * 更新账户金额
     */
    @Override
    @Transactional
    public void updateAccountBalance(AccountChangeEvent accountChange) {
        //幂等校验
        if (judgeMapper.isExistTx(accountChange.getTxNo()) > 0) {
            return;
        }
        int count = accountInfoMapper.updateAccountBalance(accountChange.getAccountName(), accountChange.getAmount());

        if (count > 0) {
            //插入事务记录，用于幂等控制
            judgeMapper.addTx(accountChange.getTxNo());
        } else {
            log.info("更新账户金额失败：{}", accountChange);
            throw new RuntimeException("更新账户金额失败");
        }
    }

    /**
     * 远程调用查询充值结果
     */
    @Override
    public AccountPay queryPayResult(String txNo) {
        //远程调用
        AccountPay payresult = payClient.payResult(txNo);
        if ("success".equals(payresult.getResult())) {
            //更新账户金额
            AccountChangeEvent accountChangeEvent = new AccountChangeEvent();

            accountChangeEvent.setAccountName(payresult.getAccountName());//账号
            accountChangeEvent.setAmount(payresult.getPayAmount());//金额
            accountChangeEvent.setTxNo(payresult.getId());//充值事务号
            updateAccountBalance(accountChangeEvent);
        }
        return payresult;
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
