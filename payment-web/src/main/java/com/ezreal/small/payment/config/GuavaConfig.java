package com.ezreal.small.payment.config;

import com.ezreal.small.payment.listener.OrderPaySuccessListener;
import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ezreal
 * @Date 2024/11/17
 */
@Configuration
public class GuavaConfig {

    @Bean
    public EventBus eventBusListener(OrderPaySuccessListener listener) {
        EventBus eventBus = new EventBus();
        eventBus.register(listener);
        return eventBus;
    }

}
