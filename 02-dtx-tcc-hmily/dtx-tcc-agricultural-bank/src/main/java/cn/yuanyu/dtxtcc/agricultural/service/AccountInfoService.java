package cn.yuanyu.dtxtcc.agricultural.service;


import cn.yuanyu.dtxtcc.agricultural.entity.AccountInfo;

import java.util.List;

public interface AccountInfoService {


    /**
     * 账户扣款
     *
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
