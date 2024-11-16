package com.ezreal.small.payment.config;

import com.ezreal.small.payment.service.WechatApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author Ezreal
 * @Date 2024/11/16
 */
@Slf4j
@Configuration
public class Retrofit2Config {

    private static final String BASE_URL = "https://api.weixin.qq.com/cgi-bin/";

    @Bean
    public Retrofit retrofit() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BASE_URL);
        builder.addConverterFactory(JacksonConverterFactory.create());
        return builder.build();
    }

    @Bean
    public WechatApiService wechatApiService(Retrofit retrofit) {
        return retrofit.create(WechatApiService.class);
    }
}
