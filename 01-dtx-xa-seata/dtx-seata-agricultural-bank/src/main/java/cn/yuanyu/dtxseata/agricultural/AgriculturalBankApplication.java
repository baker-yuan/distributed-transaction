package cn.yuanyu.dtxseata.agricultural;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author yuanyu
 */
//@EnableDiscoveryClient
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = {"cn.yuanyu.dtxseata.construction.client"})
public class AgriculturalBankApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgriculturalBankApplication.class, args);
    }
}

