package cn.yuanyu.dtxmsgrocketmq.agricultural.service.impl;


import cn.yuanyu.dtxmsgrocketmq.agricultural.dao.AccountInfoMapper;
import cn.yuanyu.dtxmsgrocketmq.agricultural.dao.JudgeMapper;
import cn.yuanyu.dtxmsgrocketmq.agricultural.entity.AccountInfo;
import cn.yuanyu.dtxmsgrocketmq.agricultural.service.TransferService;
import cn.yuanyu.dtxmsgrocketmq.construction.model.AccountChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.UUID;

/**
 * @author yuanyu
 */
@Slf4j
@Service
public class TransferServiceImpl implements TransferService {

    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Autowired
    private JudgeMapper judgeMapper;

    @Autowired
    private RocketMQTemplate mqTemplate;


    // 向mq发送转账消息
    @Override
    public void sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
        log.info("开始发送消息：{}", accountChangeEvent);
        // 将accountChangeEvent转成json
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accountChange", accountChangeEvent);
        String jsonString = jsonObject.toJSONString();
        // 生成message类型
        Message<String> message = MessageBuilder.withPayload(jsonString).build();
        // 发送一条事务消息
        /**
         * String txProducerGroup  生产组
         * String destination      topic
         * Message<?> message      消息内容
         * Object arg              参数
         */
        mqTemplate.sendMessageInTransaction("producer_group_tx_msg_agricultural_bank", "topic_tx_msg", message, null);
        log.info("消息发成功：{}", accountChangeEvent);
    }

    //更新账户，扣减金额
    @Transactional
    @Override
    public void doUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
        log.info("开始更新账户：{}", accountChangeEvent);
        // 幂等判断
        if (judgeMapper.isExistTx(accountChangeEvent.getTxNo()) > 0) {
            return;
        }
        // 扣减金额
        accountInfoMapper.updateAccountBalance(accountChangeEvent.getAccountFromName(), accountChangeEvent.getAmount() * -1);
        // 添加事务日志
        judgeMapper.addTx(accountChangeEvent.getTxNo());

        if (accountChangeEvent.getAmount() == 2) {
            throw new RuntimeException("人为制造异常2");
        }

        log.info("更新账户完成：{}", accountChangeEvent);
    }


    /**
     * @param from   转账人姓名
     * @param to     收款人姓名
     * @param amount 转账金额
     */
    @Transactional
    @Override
    public void transferOut(String from, String to, Double amount) {
        // 创建一个事务id，作为消息内容发到mq
        String tx_no = UUID.randomUUID().toString();
        AccountChangeEvent accountChangeEvent = new AccountChangeEvent(from, to, amount, tx_no);
        // 发送消息
        sendUpdateAccountBalance(accountChangeEvent);
    }


    /**
     * 查询所有用户
     */
    @Override
    public List<AccountInfo> getAllUserInfo() {
        return accountInfoMapper.getAllUserInfo();
    }

    /**
     * 根据用户名查询用户
     */
    @Override
    public AccountInfo getUserInfoByName(String accountName) {
        return accountInfoMapper.getUserInfoByName(accountName);
    }
}
