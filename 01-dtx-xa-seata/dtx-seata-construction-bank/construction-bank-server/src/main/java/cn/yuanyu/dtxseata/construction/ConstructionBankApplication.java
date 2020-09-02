
package cn.yuanyu.dtxseata.construction;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class ConstructionBankApplication {
	public static void main(String[] args) {
		SpringApplication.run(ConstructionBankApplication.class, args);
	}
}
