package cn.yuanyu.dtx.construction.client;

import org.springframework.stereotype.Component;


/**
 * @author yuanyu
 */
@Component
public class ConstructionBankClientFallback implements ConstructionBankClient {
    /**
     * @param to     收款人姓名
     * @param amount 转入金额
     * @return 转账失败返回空
     */
    @Override
    public String transferIn(String to, Double amount) {
        return "fallback";
    }
}
