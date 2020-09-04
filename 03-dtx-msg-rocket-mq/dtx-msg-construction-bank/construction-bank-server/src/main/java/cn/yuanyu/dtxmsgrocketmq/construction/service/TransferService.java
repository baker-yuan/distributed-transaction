package cn.yuanyu.dtxmsgrocketmq.construction.service;


import cn.yuanyu.dtxmsgrocketmq.construction.model.AccountChangeEvent;
import cn.yuanyu.dtxmsgrocketmq.construction.entity.AccountInfo;

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
