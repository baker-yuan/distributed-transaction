
package cn.yuanyu.dtxtcc.construction;


import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableDiscoveryClient
@ComponentScan({"cn.yuanyu.dtxtcc.construction", "org.dromara.hmily"})
public class ConstructionBankApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ConstructionBankApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
