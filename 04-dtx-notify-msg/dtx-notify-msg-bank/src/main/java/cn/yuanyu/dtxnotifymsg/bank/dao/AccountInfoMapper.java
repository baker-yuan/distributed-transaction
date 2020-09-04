package cn.yuanyu.dtxnotifymsg.bank.dao;

import cn.yuanyu.dtxnotifymsg.bank.entity.AccountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AccountInfoMapper {

    /**
     * 修改账户金额
     *
     * @param accountName 用户名
     * @param amount      修改金额大小
     */
    int updateAccountBalance(@Param("accountName") String accountName, @Param("amount") Double amount);


    /**
     * 根据用户名查询用户
     */
    AccountInfo getUserInfoByName(@Param("accountName") String accountName);

    /**
     * 获取所有的用户信息
     */
    List<AccountInfo> getAllUserInfo();
}
