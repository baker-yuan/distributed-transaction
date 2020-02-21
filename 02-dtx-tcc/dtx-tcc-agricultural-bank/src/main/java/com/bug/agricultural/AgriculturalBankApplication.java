package com.bug.agricultural;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author yuanyu
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.bug.construction.client"})
@ComponentScan({"com.bug.agricultural","org.dromara.hmily"}) //1.扫描
public class AgriculturalBankApplication {
    public static void main(String[] args) {
        //SpringApplication.run(AgriculturalBankApplication.class, args);
        new SpringApplicationBuilder(AgriculturalBankApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}

