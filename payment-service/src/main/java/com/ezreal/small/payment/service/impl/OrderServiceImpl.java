package com.ezreal.small.payment.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.ezreal.small.payment.common.entity.Constants;
import com.ezreal.small.payment.dao.mapper.PayOrderMapper;
import com.ezreal.small.payment.domain.po.PayOrder;
import com.ezreal.small.payment.domain.request.ShopCardReq;
import com.ezreal.small.payment.domain.response.CreateOrderResp;
import com.ezreal.small.payment.domain.vo.ProductVo;
import com.ezreal.small.payment.service.OrderService;
import com.ezreal.small.payment.service.rpc.ProductRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Ezreal
 * @Date 2024/11/17
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private PayOrderMapper payOrderMapper;

    @Resource
    private ProductRpc productRpc;

    @Value("${alipay.notify_url}")
    private String notifyUrl;

    @Value("${alipay.return_url}")
    private String returnUrl;

    @Resource
    private AlipayClient alipayClient;

    @Override
    public CreateOrderResp createOrder(ShopCardReq shopCardReq) throws Exception {

        String productId = shopCardReq.getProductId();
        String userId = shopCardReq.getUserId();
        PayOrder unPaidOrder = payOrderMapper.queryUnPaidOrder(userId, productId);
        if (unPaidOrder != null) {
            String payOrderStatus = unPaidOrder.getStatus();
            // 存在订单未支付，返回订单信息
            if (Constants.OrderStatusEnum.PAY_WAIT.getCode().equals(payOrderStatus)) {
                log.info("存在未支付的订单，orderId：{}", unPaidOrder.getOrderId());
                return new CreateOrderResp()
                        .setOrderId(unPaidOrder.getOrderId())
                        .setUserId(userId)
                        .setPayUrl(unPaidOrder.getPayUrl());
            }

            // 发生掉单，重新发起支付
            if (Constants.OrderStatusEnum.CREATE.getCode().equals(payOrderStatus)) {
                log.info("存在未发起支付的订单，orderId：{}", unPaidOrder.getOrderId());
                unPaidOrder = createPayOrder(unPaidOrder.getOrderId(), productId, unPaidOrder.getProductName(), unPaidOrder.getTotalAmount(), userId);
                return new CreateOrderResp()
                        .setOrderId(unPaidOrder.getOrderId())
                        .setUserId(userId)
                        .setPayUrl(unPaidOrder.getPayUrl());
            }

            // 防止订单重复支付
            if (Constants.OrderStatusEnum.PAY_SUCCESS.getCode().equals(payOrderStatus)) {
                log.info("订单已支付，orderId：{}", unPaidOrder.getOrderId());
                return new CreateOrderResp()
                      .setOrderId(unPaidOrder.getOrderId())
                      .setUserId(userId);
            }
        }

        // 其他情况，创建订单
        ProductVo productVo = productRpc.queryProductInfo(productId);

        PayOrder payOrder = new PayOrder();
        payOrder.setUserId(userId);
        payOrder.setProductId(productVo.getProductId());
        payOrder.setProductName(productVo.getProductName());
        payOrder.setTotalAmount(productVo.getProductPrice());
        payOrder.setOrderTime(new Date());
        String orderId = RandomUtil.randomNumbers(16);
        payOrder.setOrderId(orderId);
        payOrder.setStatus(Constants.OrderStatusEnum.CREATE.getCode());
        payOrderMapper.insertSelective(payOrder);

        // 发起支付，创建支付单
        payOrder = createPayOrder(orderId, productId, payOrder.getProductName(), payOrder.getTotalAmount(), userId);
        return new CreateOrderResp()
                .setOrderId(payOrder.getOrderId())
                .setUserId(userId)
                .setPayUrl(payOrder.getPayUrl());
    }

    @Override
    public void changeSuccessStatus(String tradeNo) {
        payOrderMapper.changeSuccessStatus(tradeNo);
    }

    @Override
    public void changeFailStatus(String tradeNo) {
        payOrderMapper.changeFailStatus(tradeNo);
    }

    private PayOrder createPayOrder(String orderId, String productId, String productName, BigDecimal totalAmount, String userId) throws AlipayApiException {

        // 发起支付，创建支付单
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(notifyUrl);
        request.setReturnUrl(returnUrl);
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderId);  // 我们自己生成的订单编号
        bizContent.put("total_amount", totalAmount.toString()); // 订单的总金额
        bizContent.put("subject", productName);   // 支付的名称
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");  // 固定配置
        request.setBizContent(bizContent.toString());
        String form = alipayClient.pageExecute(request).getBody();

        // 更新订单信息
        PayOrder payOrder = new PayOrder();
        payOrder.setOrderId(orderId);
        payOrder.setPayUrl(form);
        payOrder.setPayTime(new Date());
        payOrder.setStatus(Constants.OrderStatusEnum.PAY_WAIT.getCode());
        payOrderMapper.changePayWaitStatus(payOrder);

        return payOrder;
    }
}
