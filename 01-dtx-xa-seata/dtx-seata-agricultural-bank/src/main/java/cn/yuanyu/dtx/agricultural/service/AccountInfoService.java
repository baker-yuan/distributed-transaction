package cn.yuanyu.dtx.agricultural.service;


import cn.yuanyu.dtx.agricultural.entity.AccountInfo;

import java.util.List;

public interface AccountInfoService {

    /**
     * @param from   转账人姓名
     * @param to     收款人姓名
     * @param amount 转账金额
     */
    boolean transferOut(String from, String to, Double amount);

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
