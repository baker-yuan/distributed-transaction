package com.bug.agricultural.dao;

import com.bug.agricultural.entity.AccountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface AccountInfoMapper {


    /**
     * 减少账户金额
     * @param accountName 用户名
     * @param amount 减少金额
     */
    int subtractAccountBalance(@Param("accountName") String accountName, @Param("amount") Double amount);


    /**
     * 增加账户金额
     * @param accountName 用户名
     * @param amount 增加金额
     */
    int addAccountBalance(@Param("accountName") String accountName, @Param("amount") Double amount);


    /**
     * 根据用户名查询用户
     */
    AccountInfo getUserInfoByName(@Param("accountName")String accountName);

    /**
     * 获取所有的用户信息
     */
    List<AccountInfo> getAllUserInfo();
}
