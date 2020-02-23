package com.bug.agricultural.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface JudgeMapper {


    int isExistTx(String txNo);


    int addTx(String txNo);
}
