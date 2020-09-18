package io.seata.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class OrderXAApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderXAApplication.class, args);
    }
}
