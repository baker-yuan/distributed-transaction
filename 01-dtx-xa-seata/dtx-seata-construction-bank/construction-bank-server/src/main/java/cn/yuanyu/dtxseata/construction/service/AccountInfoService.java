package cn.yuanyu.dtxseata.construction.service;


import cn.yuanyu.dtxseata.construction.entity.AccountInfo;

import java.util.List;

public interface AccountInfoService {

    /**
     * 查询所有用户
     */
    List<AccountInfo> getAllUserInfo();

    /**
     * 转入
     * @param to 收款人
     * @param amount 收款金额
     */
    void transferIn(String to, Double amount);
}
