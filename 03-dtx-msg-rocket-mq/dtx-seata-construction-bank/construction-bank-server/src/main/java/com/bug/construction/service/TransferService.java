package com.bug.construction.service;


import com.bug.construction.entity.AccountInfo;
import com.bug.construction.model.AccountChangeEvent;

import java.util.List;

public interface TransferService {

    /**
     * 转入
     */
    void transferIn(AccountChangeEvent accountChangeEvent);

    /**
     * 查询所有用户
     */
    List<AccountInfo> getAllUserInfo();
}
