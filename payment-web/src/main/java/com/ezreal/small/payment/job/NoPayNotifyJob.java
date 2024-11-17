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
 * @author Ezreal
 * @Date 2024/11/17
 */
@Slf4j
@Service
public class NoPayNotifyJob {

    @Resource
    private OrderService orderService;

    @Resource
    private AlipayClient alipayClient;

    @Scheduled(cron = "0 0/3 * * * ?")
    public void exec() {
        log.info("任务；检测未接收到或未正确处理的支付回调通知");

        try {
            List<String> orderIdList = orderService.queryNoPayNotifyOrderList();
            for (String orderId : orderIdList) {
                AlipayTradeQueryModel bizModel = new AlipayTradeQueryModel();
                bizModel.setOutTradeNo(orderId);
                AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
                request.setBizModel(bizModel);

                AlipayTradeQueryResponse response = alipayClient.execute(request);
                String code = response.getCode();
                if ("10000".equals(code)) {
                    orderService.changeSuccessStatus(orderId);
                }
            }
        } catch (AlipayApiException e) {
            log.error("执行检测未接收到或未正确处理的支付回调通知任务失败");
        }
    }
}
