package com.ezreal.small.payment.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.ezreal.small.payment.common.entity.Constants;
import com.ezreal.small.payment.common.entity.Response;
import com.ezreal.small.payment.common.utils.ResultUtils;
import com.ezreal.small.payment.controller.dto.CreatePayRequestDTO;
import com.ezreal.small.payment.domain.request.ShopCardReq;
import com.ezreal.small.payment.domain.response.CreateOrderResp;
import com.ezreal.small.payment.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ezreal
 * @Date 2024/11/17
 */
@Slf4j
@RestController
@RequestMapping("/v1/alipay")
public class AliPayController {

    @Value("${alipay.alipay_public_key}")
    public String alipayPublicKey;

    @Resource
    private OrderService orderService;

    @PostMapping("/createPayOrder")
    public Response<String> createPayOrder(@RequestBody CreatePayRequestDTO createPayRequestDTO) {
        String productId = createPayRequestDTO.getProductId();
        String userId = createPayRequestDTO.getUserId();
        try {
            log.info("用户：{}，开始创建订单，商品id为：{}", userId, productId);
            CreateOrderResp createOrderResp = orderService.createOrder(ShopCardReq.builder()
                    .userId(userId)
                    .productId(productId)
                    .build());
            return ResultUtils.success(createOrderResp.getPayUrl());
        } catch (Exception e) {
            log.error("用户：{}，创建订单失败，商品id为：{}", userId, productId, e);
        }
        return ResultUtils.fail(Constants.ResponseCode.UN_ERROR);
    }

    @PostMapping("/alipay_notify_url")
    public String payNotify(HttpServletRequest request) throws AlipayApiException {
        log.info("支付回调，消息接收 {}", request.getParameter("trade_status"));

        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            params.put(name, request.getParameter(name));
        }

        String tradeNo = params.get("out_trade_no");
        String sign = params.get("sign");
        String content = AlipaySignature.getSignCheckContentV1(params);
        boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayPublicKey, "UTF-8"); // 验证签名
        // 支付宝验签
        if (!checkSignature) {
            log.error("支付宝验签失败，订单号：{}", tradeNo);
            orderService.changeFailStatus(tradeNo);
            return "false";
        }

        if (!params.get("trade_status").equals("TRADE_SUCCESS")) {
            orderService.changeFailStatus(tradeNo);
            return "false";
        }

        // 验签通过
        log.info("支付回调，交易名称: {}", params.get("subject"));
        log.info("支付回调，交易状态: {}", params.get("trade_status"));
        log.info("支付回调，支付宝交易凭证号: {}", params.get("trade_no"));
        log.info("支付回调，商户订单号: {}", params.get("out_trade_no"));
        log.info("支付回调，交易金额: {}", params.get("total_amount"));
        log.info("支付回调，买家在支付宝唯一id: {}", params.get("buyer_id"));
        log.info("支付回调，买家付款时间: {}", params.get("gmt_payment"));
        log.info("支付回调，买家付款金额: {}", params.get("buyer_pay_amount"));
        log.info("支付回调，支付回调，更新订单 {}", tradeNo);

        orderService.changeSuccessStatus(tradeNo);

        return "success";
    }
}
