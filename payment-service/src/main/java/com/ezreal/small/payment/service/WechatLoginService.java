package com.ezreal.small.payment.service;

import java.io.IOException;

/**
 * 微信登录接口
 *
 * @author Ezreal
 * @Date 2024/11/16
 */
public interface WechatLoginService {


    /**
     * 创建二维码ticket
     *
     * @return ticket
     */
    String createQrCodeTicket() throws Exception;

    /**
     * 保存用户登录状态
     *
     * @param ticket 二维码ticket
     * @param openid 用户唯一标识
     * @throws Exception 异常
     */
    void saveLoginState(String ticket, String openid);

    /**
     * 用户登录状态查询
     *
     * @param ticket 二维码ticket
     * @return 登录状态
     * @throws IOException 异常
     */
    String checkLoginState(String ticket);
}
