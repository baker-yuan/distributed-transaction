package cn.yuanyu.dtxnotifymsg.bank.message;


import cn.yuanyu.dtxnotifymsg.bank.model.AccountChangeEvent;
import cn.yuanyu.dtxnotifymsg.bank.service.AccountInfoService;
import cn.yuanyu.dtxnotifymsg.pay.entity.AccountPay;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = "topic_notify_msg", consumerGroup = "consumer_group_notify_msg_bank")
public class NotifyMsgListener implements RocketMQListener<AccountPay> {

    @Autowired
    private AccountInfoService accountInfoService;

    /**
     * 接收消息
     */
    @Override
    public void onMessage(AccountPay accountPay) {
        log.info("接收到消息：{}", JSON.toJSONString(accountPay));

        if ("success".equals(accountPay.getResult())) {
            // 更新账户金额
            AccountChangeEvent accountChangeEvent = new AccountChangeEvent();
            accountChangeEvent.setAccountName(accountPay.getAccountName());
            accountChangeEvent.setAmount(accountPay.getPayAmount());
            accountChangeEvent.setTxNo(accountPay.getId());
            accountInfoService.updateAccountBalance(accountChangeEvent);
        }

        log.info("处理消息完成：{}", JSON.toJSONString(accountPay));
    }
}
