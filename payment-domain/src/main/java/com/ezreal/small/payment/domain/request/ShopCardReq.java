package com.ezreal.small.payment.domain.request;

import lombok.Data;

/**
 * @author Ezreal
 * @Date 2024/11/17
 */
@Data
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
