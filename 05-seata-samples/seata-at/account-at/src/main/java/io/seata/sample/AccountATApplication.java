package io.seata.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableTransactionManagement
@SpringBootApplication
public class AccountATApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountATApplication.class, args);
    }
}
