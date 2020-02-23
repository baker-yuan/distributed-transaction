package com.bug.agricultural.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface LogMapper {

    /**
     * 增加某分支事务try执行记录
     * @param localTradeNo 本地事务编号
     */
    int addTry(String localTradeNo);

    /**
     * 增加某分支事务confirm执行记录
     * @param localTradeNo 本地事务编号
     */
    int addConfirm(String localTradeNo);

    /**
     * 增加某分支事务cancel执行记录
     * @param localTradeNo 本地事务编号
     */
    int addCancel(String localTradeNo);

    /**
     * 查询分支事务try是否已执行
     * @param localTradeNo 本地事务编号
     */
    int isExistTry(String localTradeNo);
    /**
     * 查询分支事务confirm是否已执行
     * @param localTradeNo 本地事务编号
     */
    int isExistConfirm(String localTradeNo);

    /**
     * 查询分支事务cancel是否已执行
     * @param localTradeNo 本地事务编号
     */
    int isExistCancel(String localTradeNo);

}
