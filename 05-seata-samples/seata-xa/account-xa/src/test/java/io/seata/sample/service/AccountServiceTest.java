package io.seata.sample.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void reduce() {
        accountService.reduce("U100000", 10);
    }

    @Test
    public void jdbcTemplate() {
        //jdbcTemplate.update("update account_tbl set money = money - ? where user_id = ?", new Object[]{10, "U100000"});

        int balance = jdbcTemplate.queryForObject("select money from account_tbl where user_id = ?", new Object[]{"U100000"}, Integer.class);
        log.info("balance => {}", balance);
    }

}