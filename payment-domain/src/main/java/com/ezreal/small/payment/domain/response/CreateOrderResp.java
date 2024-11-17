package com.ezreal.small.payment.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Ezreal
 * @Date 2024/11/17
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResp {

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 支付地址
     */
    private String payUrl;
}
