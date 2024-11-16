package com.ezreal.small.payment.controller;

import com.ezreal.small.payment.common.wechat.MessageTextEntity;
import com.ezreal.small.payment.common.wechat.SignUtils;
import com.ezreal.small.payment.common.wechat.XmlUtil;
import com.ezreal.small.payment.service.WechatLoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Ezreal
 * @Date 2024/11/9
 */
@Slf4j
@RestController
@RequestMapping("/wechat")
public class WeChatController {

    @Value("${wechat.config.token}")
    private String token;

    @Value("${wechat.config.originalId}")
    private String originalId;

    @Resource
    private WechatLoginService wechatLoginService;

    @GetMapping(value = "/verify", produces = "text/plain;charset=utf-8")
    public String verify(@RequestParam(value = "signature", required = false) String signature,
                         @RequestParam(value = "timestamp", required = false) String timestamp,
                         @RequestParam(value = "nonce", required = false) String nonce,
                         @RequestParam(value = "echostr", required = false) String echostr) {

        // 在这里进行微信验证逻辑
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            log.error("微信验签参数错误");
            return "error";
        }
        log.info("微信验签参数：signature={},timestamp={},nonce={},echostr={}", signature, timestamp, nonce, echostr);
        // 验证成功后返回 echostr，否则返回错误信息
        boolean verified = SignUtils.verifyWeChatSign(signature, timestamp, nonce, token);
        if (verified) {
            return echostr;
        }
        return null;
    }

    @PostMapping(value = "/verify", produces = "text/plain;charset=utf-8")
    public String receive(@RequestBody String body,
                          @RequestParam(value = "timestamp", required = false) String timestamp,
                          @RequestParam(value = "openid", required = false) String openid) {
        log.info("接受微信公众号的消息：{}，openid（fromUserId）：{}，timestamp：{}", body, openid, timestamp);
        MessageTextEntity wechatMessageContent = XmlUtil.xmlToBean(body, MessageTextEntity.class);
        if ("event".equals(wechatMessageContent.getMsgType()) && "SCAN".equals(wechatMessageContent.getEvent())) {
            // 关注公众号，扫码登录，返回消息
            String ticket = wechatMessageContent.getTicket();
            log.info("ticket：{}", ticket);
            wechatLoginService.saveLoginState(ticket, openid);
            return buildReturnWechatMessage(wechatMessageContent.getFromUserName(), "登录成功");
        }
        return buildReturnWechatMessage(wechatMessageContent.getFromUserName(), wechatMessageContent.getFromUserName() + "，你好");
    }

    private String buildReturnWechatMessage(String openid, String content) {
        MessageTextEntity wechatMessageContent = new MessageTextEntity();
        wechatMessageContent.setFromUserName(originalId);
        wechatMessageContent.setToUserName(openid);
        wechatMessageContent.setContent(content);
        wechatMessageContent.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000));
        wechatMessageContent.setMsgType("text");
        return XmlUtil.beanToXml(wechatMessageContent);
    }

}
