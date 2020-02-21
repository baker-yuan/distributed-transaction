package com.bug.construction.client;


import org.dromara.hmily.annotation.Hmily;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author yuanyu
 */
@Component
@FeignClient(value="construction-bank",fallback= ConstructionBankClientFallback.class)
public interface ConstructionBankClient {
    /**
     * 转入
     * @param to 收款人姓名
     * @param amount 转入金额
     */
    @Hmily //注意
    @PostMapping("/construction/transfer/in")
    boolean transferIn(@RequestParam("to")String to, @RequestParam("amount")Double amount);
}
