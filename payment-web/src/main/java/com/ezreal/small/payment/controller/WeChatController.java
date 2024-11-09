package com.ezreal.small.payment.controller;

import com.ezreal.small.payment.common.wechat.MessageTextEntity;
import com.ezreal.small.payment.common.wechat.SignUtils;
import com.ezreal.small.payment.common.wechat.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ezreal
 * @Date 2024/11/9
 */
@Slf4j
@RestController
public class WeChatController {

    @Value("${wechat.config.token}")
    private String token;

    @Value("${wechat.config.originalId}")
    private String originalId;

    @GetMapping(value = "/wechat/verify", produces = "text/plain;charset=utf-8")
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

    @PostMapping(value = "/wechat/verify", produces = "text/plain;charset=utf-8")
    public String receive(@RequestBody String body,
                          @RequestParam(value = "timestamp", required = false) String timestamp,
                          @RequestParam(value = "openid", required = false) String openid) {
        log.info("接受微信公众的消息：{}，openid（fromUserId）：{}，timestamp：{}", body, openid, timestamp);
        MessageTextEntity wechatMessageContent = XmlUtil.xmlToBean(body, MessageTextEntity.class);
        return buildReturnWechatMessage(wechatMessageContent.getFromUserName(), "hello world");
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
