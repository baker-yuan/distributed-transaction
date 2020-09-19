package io.seata.sample.controller;

import io.seata.sample.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static io.seata.sample.service.BusinessService.SUCCESS;

@RestController
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    /**
     * @param rollback 是否回滚
     * @param count    购买商品数量
     */
    // http://127.0.0.1:56090/purchase?rollback=false&count=30
    //
    @GetMapping(value = "/purchase")
    public String purchase(@RequestParam Boolean rollback, @RequestParam Integer count) {
        int orderCount = 30;
        if (count != null) {
            orderCount = count;
        }
        try {
            // TODO 这里先写死
            businessService.purchase("U100000", "C100000", orderCount, rollback == null ? false : rollback.booleanValue());
        } catch (Exception exx) {
            return "Purchase Failed:" + exx.getMessage();
        }
        return SUCCESS;
    }

}
