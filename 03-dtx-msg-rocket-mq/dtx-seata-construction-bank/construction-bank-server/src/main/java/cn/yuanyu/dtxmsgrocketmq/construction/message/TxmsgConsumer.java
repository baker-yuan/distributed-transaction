package cn.yuanyu.dtxmsgrocketmq.construction.message;


import com.alibaba.fastjson.JSONObject;
import cn.yuanyu.dtxmsgrocketmq.construction.model.AccountChangeEvent;
import cn.yuanyu.dtxmsgrocketmq.construction.service.TransferService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @version 1.0
 **/
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "consumer_group_tx_msg_construction_bank",topic = "topic_tx_msg")
public class TxmsgConsumer implements RocketMQListener<String> {

    @Autowired
    TransferService transferService;

    //接收消息
    @Override
    public void onMessage(String message) {
        log.info("开始消费消息:{}",message);
        //解析消息
        JSONObject jsonObject = JSONObject.parseObject(message);
        String accountChangeString = jsonObject.getString("accountChange");

        //转成AccountChangeEvent
        AccountChangeEvent accountChangeEvent = JSONObject.parseObject(accountChangeString, AccountChangeEvent.class);

        //更新本地账户，增加金额
        transferService.transferIn(accountChangeEvent);

        log.info("消息消费完成:{}",message);
    }
}
