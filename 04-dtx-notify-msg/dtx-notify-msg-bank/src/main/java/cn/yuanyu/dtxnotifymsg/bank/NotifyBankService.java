package cn.yuanyu.dtxnotifymsg.bank;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"cn.yuanyu.dtxnotifymsg.pay.client"})
public class NotifyBankService {
	public static void main(String[] args) {
		SpringApplication.run(NotifyBankService.class, args);
	}
}
