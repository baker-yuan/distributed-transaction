package io.seata.sample.controller;

import io.seata.sample.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.seata.sample.service.StorageService.FAIL;
import static io.seata.sample.service.StorageService.SUCCESS;

@RestController
public class StorageController {

    @Autowired
    private StorageService storageService;

    /**
     * 扣减商品
     *
     * @param commodityCode 商品ID
     * @param count         商品数量
     */
    @GetMapping(value = "/deduct")
    public String deduct(String commodityCode, int count) {
        try {
            storageService.deduct(commodityCode, count);
        } catch (Exception exx) {
            exx.printStackTrace();
            return FAIL;
        }
        return SUCCESS;
    }
}
