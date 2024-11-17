package com.ezreal.small.payment;

import cn.hutool.json.JSONUtil;
import com.ezreal.small.payment.domain.request.ShopCardReq;
import com.ezreal.small.payment.domain.response.CreateOrderResp;
import com.ezreal.small.payment.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author Ezreal
 * @Date 2024/11/17
 */
@Slf4j
@SpringBootTest
public class OrderServiceTest {

    @Resource
    private OrderService orderService;

    @Test
    public void createOrder() {
        ShopCardReq shopCardReq = new ShopCardReq();
        shopCardReq.setProductId("10002");
        shopCardReq.setUserId("10001");
        CreateOrderResp createOrderResp = orderService.createOrder(shopCardReq);
        log.info("createOrderResp:{}", JSONUtil.toJsonStr(createOrderResp));
    }
}
