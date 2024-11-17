package com.ezreal.small.payment.service;

import com.ezreal.small.payment.domain.request.ShopCardReq;
import com.ezreal.small.payment.domain.response.CreateOrderResp;

/**
 * @author Ezreal
 * @Date 2024/11/17
 */
public interface OrderService {

    /**
     * 创建订单
     *
     * @param shopCardReq 请求参数
     * @return 订单信息
     */
    CreateOrderResp createOrder(ShopCardReq shopCardReq) throws Exception;

    /**
     * 更新订单状态为支付成功
     *
     * @param tradeNo 交易流水号
     */
    void changeSuccessStatus(String tradeNo);

    /**
     * 更新订单状态为支付成功
     *
     * @param tradeNo 交易流水号
     */
    void changeFailStatus(String tradeNo);
}
