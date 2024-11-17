package com.ezreal.small.payment.service.rpc;

import com.ezreal.small.payment.domain.vo.ProductVo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Ezreal
 * @Date 2024/11/17
 */
@Service
public class ProductRpc {

    public ProductVo queryProductInfo(String productId) {
        return new ProductVo()
                .setProductId(productId)
                .setProductPrice(new BigDecimal("13.2"))
                .setProductName("测试商品");
    }
}
