package com.bug.agricultural.client;

import org.springframework.stereotype.Component;


/**
 * @author yuanyu
 */
@Component
public class ConstructionBankClientFallback implements ConstructionBankClient {
    @Override
    public String transferIn(String to, Double amount) {
        return "fallback";
    }
}
