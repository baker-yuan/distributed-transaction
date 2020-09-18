package io.seata.sample.service;

import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StorageService {

    public static final String SUCCESS = "SUCCESS";

    public static final String FAIL = "FAIL";

    @Autowired
    private JdbcTemplate jdbcTemplate;



    public void deduct(String commodityCode, int count) {
        String xid = RootContext.getXID();
        log.info("deduct storage balance in transaction: " + xid);
        jdbcTemplate.update("update storage_tbl set count = count - ? where commodity_code = ?", new Object[]{count, commodityCode});
    }

}
