package cn.yuanyu.dtx.agricultural.dao;

import cn.yuanyu.dtx.agricultural.entity.AccountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface AccountInfoMapper {

    // 更新账户金额
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
