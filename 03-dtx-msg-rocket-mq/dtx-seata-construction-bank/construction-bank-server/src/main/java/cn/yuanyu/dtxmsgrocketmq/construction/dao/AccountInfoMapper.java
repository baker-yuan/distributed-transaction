package cn.yuanyu.dtxmsgrocketmq.construction.dao;

import cn.yuanyu.dtxmsgrocketmq.construction.entity.AccountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface AccountInfoMapper {
    //更新账户
    int updateAccountBalance(@Param("accountName") String accountName, @Param("amount") Double amount);

    /**
     *
     */
    List<AccountInfo> getAllUserInfo();


    /**
     * 根据用户名查询用户
     */
    AccountInfo getUserInfoByName(@Param("accountName")String accountName);

}
