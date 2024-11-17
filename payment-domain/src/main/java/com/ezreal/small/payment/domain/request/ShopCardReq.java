package com.ezreal.small.payment.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ezreal
 * @Date 2024/11/17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopCardReq {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 商品id
     */
    private String productId;
}
