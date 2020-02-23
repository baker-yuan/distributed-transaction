package com.nobug.dtx.notify.bank.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface JudgeMapper {

    /**
     * 查询幂等记录，用于幂等控制
     * @param txNo 事务号
     */
    int isExistTx(String txNo);

    /**
     * 添加事务记录，用于幂等控制
     * @param txNo 事务号
     */
    int addTx(String txNo);
}
