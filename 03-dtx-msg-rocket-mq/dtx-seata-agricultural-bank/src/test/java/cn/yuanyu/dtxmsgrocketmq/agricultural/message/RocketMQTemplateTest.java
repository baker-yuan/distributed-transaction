package cn.yuanyu.dtxmsgrocketmq.agricultural.message;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RocketMQTemplateTest {

    @Autowired
    RocketMQTemplate mqTemplate;

    @Test
    public void test_mqTemplate(){
        //System.out.println(mqTemplate);
        mqTemplate.sendOneWay("mq_test", "hello, world.");

    }
}
