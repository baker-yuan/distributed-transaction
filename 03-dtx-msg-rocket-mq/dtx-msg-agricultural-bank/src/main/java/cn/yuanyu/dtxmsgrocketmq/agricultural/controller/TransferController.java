package cn.yuanyu.dtxmsgrocketmq.agricultural.controller;


import cn.yuanyu.dtxmsgrocketmq.agricultural.service.TransferService;
import cn.yuanyu.dtxmsgrocketmq.agricultural.entity.AccountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author yuanyu
 */
@RestController
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    TransferService transferService;

    /**
     * http://localhost:56085/agricultural/transfer/all
     * http://localhost:56086/construction/transfer/all
     */
    /**
     * http://localhost:56085/agricultural/transfer/out?from=张三&to=李四&amount=10
     *
     * http://localhost:56085/agricultural/transfer/out?from=张三&to=李四&amount=2
     * http://localhost:56085/agricultural/transfer/out?from=张三&to=李四&amount=3
     */
    /**
     * 转出接口
     *
     * @param from   转账人姓名
     * @param to     收款人姓名
     * @param amount 转账金额
     */
    @PostMapping("/out")
    public String transferOut(String from, String to, Double amount) {
        transferService.transferOut(from, to, amount);
        return "转账成功"; // 这里不一定转账成功
    }


    /**
     * http://localhost:56085/agricultural/transfer/张三
     */
    @GetMapping("/{accountName}")
    AccountInfo getUserInfoByName(@PathVariable("accountName") String accountName) {
        return transferService.getUserInfoByName(accountName);
    }


    /**
     * http://localhost:56085/agricultural/transfer/all
     */
    @GetMapping("/all")
    public List<AccountInfo> getAllUserInfo() {
        return transferService.getAllUserInfo();
    }

}
