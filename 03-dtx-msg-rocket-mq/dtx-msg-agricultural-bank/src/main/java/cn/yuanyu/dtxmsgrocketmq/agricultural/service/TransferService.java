package cn.yuanyu.dtxmsgrocketmq.agricultural.service;


import cn.yuanyu.dtxmsgrocketmq.agricultural.entity.AccountInfo;
import cn.yuanyu.dtxmsgrocketmq.construction.model.AccountChangeEvent;

import java.util.List;

public interface TransferService {


    /**
     * 向mq发送转账消息
     */
    void sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent);


    /**
     * 更新账户，扣减金额
     */
    void doUpdateAccountBalance(AccountChangeEvent accountChangeEvent);


    /**
     * @param from   转账人姓名
     * @param to     收款人姓名
     * @param amount 转账金额
     */
    void transferOut(String from, String to, Double amount);

    /**
     * 查询所有用户
     */
    List<AccountInfo> getAllUserInfo();

    /**
     * 根据用户名查询用户
     *
     * @param accountName 用户名（唯一）
     */
    AccountInfo getUserInfoByName(String accountName);
}
