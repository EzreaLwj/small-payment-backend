package com.ezreal.small.payment.listener;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Ezreal
 * @Date 2024/11/17
 */
@Slf4j
@Component
public class OrderPaySuccessListener {

    @Subscribe
    public void handleEvent(String paySuccessMessage) {
        log.info("收到支付成功消息，可以做接下来的事情，如；发货、充值、开户员、返利 {}", paySuccessMessage);
    }

}
