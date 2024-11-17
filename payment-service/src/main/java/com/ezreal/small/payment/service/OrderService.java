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
    CreateOrderResp createOrder(ShopCardReq shopCardReq);

}
