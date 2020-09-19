package io.seata.sample.service;

import io.seata.core.context.RootContext;
import io.seata.sample.feign.OrderFeignClient;
import io.seata.sample.feign.StorageFeignClient;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BusinessService {

    public static final String SUCCESS = "SUCCESS";

    public static final String FAIL = "FAIL";

    @Autowired
    private StorageFeignClient storageFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;


    /**
     * 用户购买商品
     *
     * @param userId        用户ID
     * @param commodityCode 商品ID
     * @param orderCount    商品数量
     * @param rollback      是否回滚
     */
    @GlobalTransactional //
    public void purchase(String userId, String commodityCode, int orderCount, boolean rollback) {
        log.info("parameter => userId={},commodityCode={},orderCount={},rollback={}", userId, commodityCode, orderCount, rollback);
        String xid = RootContext.getXID();
        log.info("New Transaction Begins: " + xid);
        // 扣减库存
        String result = storageFeignClient.deduct(commodityCode, orderCount);
        if (!SUCCESS.equals(result)) {
            throw new RuntimeException("库存服务调用失败,事务回滚!");
        }
        // 生成订单
        result = orderFeignClient.create(userId, commodityCode, orderCount);
        if (!SUCCESS.equals(result)) {
            throw new RuntimeException("订单服务调用失败,事务回滚!");
        }
        if (rollback) {
            throw new RuntimeException("Force rollback ... ");
        }
    }
}
