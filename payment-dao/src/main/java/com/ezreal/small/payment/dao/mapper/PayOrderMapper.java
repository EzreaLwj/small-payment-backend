package com.ezreal.small.payment.dao.mapper;

import org.apache.ibatis.annotations.Param;
import com.ezreal.small.payment.domain.po.PayOrder;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Ezreal
 * @description 针对表【pay_order】的数据库操作Mapper
 * @createDate 2024-11-17 11:38:07
 * @Entity com.ezreal.small.payment.domain.po.PayOrder
 */
@Mapper
public interface PayOrderMapper {

    PayOrder queryUnPaidOrder(@Param("userId") String userId, @Param("productId") String productId);

    int insertSelective(PayOrder payOrder);

    void changePayWaitStatus(@Param("payOrder") PayOrder payOrder);

    void changeSuccessStatus(@Param("tradeNo") String tradeNo);

    void changeFailStatus(@Param("tradeNo") String tradeNo);

    List<String> queryNoPayNotifyOrderList();

    List<String> queryTimeOutOrderList();

    void changeTimeOutStatus(String orderId);
}
