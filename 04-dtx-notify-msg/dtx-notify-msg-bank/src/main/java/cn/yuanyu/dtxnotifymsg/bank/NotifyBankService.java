package cn.yuanyu.dtxnotifymsg.bank;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.nobug.dtx.notify.pay.client"})
public class NotifyBankService {
	public static void main(String[] args) {
		SpringApplication.run(NotifyBankService.class, args);
	}
}
