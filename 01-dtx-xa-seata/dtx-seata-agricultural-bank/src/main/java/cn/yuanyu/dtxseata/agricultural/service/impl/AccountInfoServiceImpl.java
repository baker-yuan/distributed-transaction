package cn.yuanyu.dtxseata.agricultural.service.impl;


import cn.yuanyu.dtxseata.agricultural.dao.AccountInfoMapper;
import cn.yuanyu.dtxseata.agricultural.entity.AccountInfo;
import cn.yuanyu.dtxseata.agricultural.service.AccountInfoService;
import cn.yuanyu.dtxseata.construction.client.ConstructionBankClient;
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
@Slf4j
@Service
public class AccountInfoServiceImpl implements AccountInfoService {

    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Autowired
    private ConstructionBankClient constructionBankClient;

    /**
     * @param from   转账人姓名
     * @param to     收款人姓名
     * @param amount 转账金额
     */
    @Transactional
    @GlobalTransactional // TODO 开启全局事务
    @Override
    public boolean transferOut(String from, String to, Double amount) {
        log.info("bank1 service begin,XID：{}", RootContext.getXID());
        // 扣减from的金额
        AccountInfo fromUser = accountInfoMapper.getUserInfoByName(from);
        if (fromUser.getAccountBalance() - amount >= 0) {
            accountInfoMapper.updateAccountBalance(from, amount * -1);
        } else {
            throw new RuntimeException("转账金额多于用户实际金额");
        }
        // 调用construction微服务转账
        String transfer = constructionBankClient.transferIn(to, amount);
        // System.out.println(transfer);
        // 调用construction微服务异常
        if ("fallback".equals(transfer)) {
            throw new RuntimeException("转账给" + to + "失败");
        }
        if (amount == 2) {
            // 人为制造异常
            throw new RuntimeException("农业银行转账发生异常...");
        }
        return true;
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
