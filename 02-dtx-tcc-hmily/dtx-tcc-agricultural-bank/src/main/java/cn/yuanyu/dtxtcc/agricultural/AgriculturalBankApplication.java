package cn.yuanyu.dtxtcc.agricultural;

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
@EnableDiscoveryClient
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableFeignClients(basePackages = {"cn.yuanyu.dtxtcc.construction.client"})
@ComponentScan({"cn.yuanyu.dtxtcc.agricultural","org.dromara.hmily"}) // 1.扫描
public class AgriculturalBankApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(AgriculturalBankApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}

