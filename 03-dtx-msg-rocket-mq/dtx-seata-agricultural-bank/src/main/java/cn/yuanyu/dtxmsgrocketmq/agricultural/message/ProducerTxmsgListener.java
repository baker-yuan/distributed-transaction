package cn.yuanyu.dtxmsgrocketmq.agricultural.message;

import cn.yuanyu.dtxmsgrocketmq.agricultural.dao.AccountInfoMapper;
import cn.yuanyu.dtxmsgrocketmq.agricultural.dao.JudgeMapper;
import cn.yuanyu.dtxmsgrocketmq.agricultural.service.TransferService;
import cn.yuanyu.dtxmsgrocketmq.construction.model.AccountChangeEvent;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RocketMQTransactionListener(txProducerGroup = "producer_group_tx_msg_agricultural_bank")
public class ProducerTxmsgListener implements RocketMQLocalTransactionListener {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Autowired
    private JudgeMapper judgeMapper;

    // 事务消息发送后的回调方法，当消息发送给mq成功，此方法被回调
    @Override
    @Transactional
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        log.info("消息发送成功执行本地事务：{}", message);
        try {
            // 解析message，转成AccountChangeEvent
            String messageString = new String((byte[]) message.getPayload());
            JSONObject jsonObject = JSONObject.parseObject(messageString);
            String accountChangeString = jsonObject.getString("accountChange");
            // 将accountChange（json）转成AccountChangeEvent
            AccountChangeEvent accountChangeEvent = JSONObject.parseObject(accountChangeString, AccountChangeEvent.class);
            // 执行本地事务，扣减金额
            transferService.doUpdateAccountBalance(accountChangeEvent);
            // 当返回RocketMQLocalTransactionState.COMMIT，自动向mq发送commit消息，mq将消息的状态改为可消费
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            e.printStackTrace();
            return RocketMQLocalTransactionState.ROLLBACK;
        }

    }

    //事务状态回查，查询是否扣减金额
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        log.info("事务状态回查：{}", message);
        // 解析message，转成AccountChangeEvent
        String messageString = new String((byte[]) message.getPayload());
        JSONObject jsonObject = JSONObject.parseObject(messageString);
        String accountChangeString = jsonObject.getString("accountChange");
        // 将accountChange（json）转成AccountChangeEvent
        AccountChangeEvent accountChangeEvent = JSONObject.parseObject(accountChangeString, AccountChangeEvent.class);
        // 事务id
        String txNo = accountChangeEvent.getTxNo();
        int existTx = judgeMapper.isExistTx(txNo);
        if (existTx > 0) {
            return RocketMQLocalTransactionState.COMMIT;
        } else {
            return RocketMQLocalTransactionState.UNKNOWN;
        }
    }
}
