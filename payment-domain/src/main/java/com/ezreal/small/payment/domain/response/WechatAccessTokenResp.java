package com.ezreal.small.payment.domain.response;

import lombok.Data;

/**
 * @author Ezreal
 * @Date 2024/11/16
 */
@Data
public class WechatAccessTokenResp {

    /**
     * 获取到的凭证
     */
    private String access_token;

    /**
     * 凭证有效时间，单位：秒
     */
    private Long expires_in;


    private String errcode;

    private String errmsg;
}
