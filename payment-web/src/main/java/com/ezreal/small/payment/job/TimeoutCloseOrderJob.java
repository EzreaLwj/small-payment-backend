package com.ezreal.small.payment.job;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.ezreal.small.payment.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description 超时关闭订单
 * @author Ezreal
 * @Date 2024/11/17
 */
@Slf4j
@Service
public class TimeoutCloseOrderJob {

    @Resource
    private OrderService orderService;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void exec() {
        log.info("任务；超时关单");

        try {
            List<String> orderIdList = orderService.queryTimeOutOrderList();
            if (orderIdList.isEmpty()) {
                log.info("未查询到超时订单");
                return;
            }
            for (String orderId : orderIdList) {
                orderService.changeTimeOutStatus(orderId);
            }
        } catch (Exception e) {
            log.error("任务；超时关单失败");
        }
    }
}
