package com.ezreal.small.payment.controller;

import com.ezreal.small.payment.common.entity.Constants;
import com.ezreal.small.payment.common.entity.Response;
import com.ezreal.small.payment.common.utils.ResultUtils;
import com.ezreal.small.payment.service.WechatLoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Ezreal
 * @Date 2024/11/16
 */
@Slf4j
@RestController
@RequestMapping("/v1/login")
public class LoginController {

    @Resource
    private WechatLoginService wechatLoginService;

    /**
     * 微信登录
     *
     * @return 获取二维码登录凭证
     */
    @GetMapping
    public Response<String> loginByWechat() {

        try {
            String qrCodeTicket = wechatLoginService.createQrCodeTicket();
            log.info("生成微信登录ticket成功，ticket：{}", qrCodeTicket);
            return ResultUtils.success(qrCodeTicket);
        } catch (Exception e) {
            log.error("获取创建二维码ticket失败", e);
            return ResultUtils.fail(Constants.ResponseCode.UN_ERROR);
        }
    }

    /**
     * 检查用户登录状态
     *
     * @param ticket 用户登录凭证
     * @return 用户登录状态
     */
    @GetMapping("/checkLoginState")
    public Response<String> checkLoginState(String ticket) {
        String loginState = wechatLoginService.checkLoginState(ticket);
        if (StringUtils.isNotBlank(loginState)) {
            return ResultUtils.success(loginState);
        }
        return ResultUtils.fail(Constants.ResponseCode.UN_LOGIN);
    }
}
