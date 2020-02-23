package com.nobug.dtx.notify.pay.client;

import com.nobug.dtx.notify.pay.entity.AccountPay;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 远程调用pay充值系统
 * @author yuanyu
 */
@FeignClient(value = "dtx-notify-msg-pay", fallback = PayFallback.class)
public interface PayClient {


    /**
     * 远程调用充值系统的接口查询充值结果
     */
    @GetMapping(value = "/pay/payresult/{txNo}")
    AccountPay payResult(@PathVariable("txNo") String txNo);
}
