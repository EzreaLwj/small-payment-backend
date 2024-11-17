package com.ezreal.small.payment.dao.mapper;
import org.apache.ibatis.annotations.Param;
import com.ezreal.small.payment.domain.po.PayOrder;

import org.apache.ibatis.annotations.Mapper;

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
}
