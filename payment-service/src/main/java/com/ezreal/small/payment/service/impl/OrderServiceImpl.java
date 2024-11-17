package com.ezreal.small.payment.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.ezreal.small.payment.common.entity.Constants;
import com.ezreal.small.payment.dao.mapper.PayOrderMapper;
import com.ezreal.small.payment.domain.po.PayOrder;
import com.ezreal.small.payment.domain.request.ShopCardReq;
import com.ezreal.small.payment.domain.response.CreateOrderResp;
import com.ezreal.small.payment.domain.vo.ProductVo;
import com.ezreal.small.payment.service.OrderService;
import com.ezreal.small.payment.service.rpc.ProductRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Override
    public CreateOrderResp createOrder(ShopCardReq shopCardReq) {

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
                return new CreateOrderResp()
                        .setOrderId(unPaidOrder.getOrderId())
                        .setUserId(userId)
                        .setPayUrl(unPaidOrder.getPayUrl());
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
        payOrder.setOrderId(RandomUtil.randomNumbers(16));
        payOrder.setStatus(Constants.OrderStatusEnum.CREATE.getCode());
        payOrderMapper.insertSelective(payOrder);

        // 发起支付，创建支付单

        return new CreateOrderResp()
                .setOrderId(payOrder.getOrderId())
                .setUserId(userId)
                .setPayUrl(payOrder.getPayUrl());
    }
}
