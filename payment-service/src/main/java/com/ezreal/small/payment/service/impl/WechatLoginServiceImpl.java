package com.ezreal.small.payment.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ezreal.small.payment.domain.request.WechatCreateQrcodeTicketReq;
import com.ezreal.small.payment.domain.response.WechatAccessTokenResp;
import com.ezreal.small.payment.domain.response.WechatCreateQrcodeTicketResp;
import com.ezreal.small.payment.service.WechatApiService;
import com.ezreal.small.payment.service.WechatLoginService;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Ezreal
 * @Date 2024/11/16
 */
@Slf4j
@Service
public class WechatLoginServiceImpl implements WechatLoginService {

    @Value("${wechat.config.appId}")
    private String appId;

    @Value("${wechat.config.secret}")
    private String secret;

    @Resource
    private WechatApiService wechatApiService;

    @Resource
    private Cache<String, String> accessTokenCache;

    @Resource
    private Cache<String, String> ticketCache;

    @Override
    public String createQrCodeTicket() throws Exception {

        // 1.获取accessToken
        String accessToken = accessTokenCache.getIfPresent(appId);
        if (StrUtil.isBlank(accessToken)) {
            Call<WechatAccessTokenResp> getAccessTokenCall = wechatApiService.getAccessToken("client_credential", appId, secret);
            Response<WechatAccessTokenResp> accessTokenResponse = getAccessTokenCall.execute();
            WechatAccessTokenResp wechatAccessTokenResp = accessTokenResponse.body();
            if (wechatAccessTokenResp == null) {
                throw new RuntimeException("获取accessToken失败，wechatAccessTokenResp返回值为空");
            }
            accessToken = wechatAccessTokenResp.getAccess_token();
            if (StrUtil.isBlank(accessToken)) {
                throw new RuntimeException(String.format("获取accessToken失败，errMsg：%s，errCode：%s", wechatAccessTokenResp.getErrmsg(), wechatAccessTokenResp.getErrcode()));
            }
            accessTokenCache.put(appId, accessToken);
        }

        // 2.获取二维码创建凭证
        WechatCreateQrcodeTicketReq wechatCreateQrcodeTicketReq = new WechatCreateQrcodeTicketReq();
        wechatCreateQrcodeTicketReq.setExpire_seconds(2592000L)
                .setAction_name(WechatCreateQrcodeTicketReq.ActionType.QR_SCENE.getActionType())
                .setAction_info(WechatCreateQrcodeTicketReq.ActionInfo
                        .builder()
                        .scene_id(100L)
                        .build());
        Call<WechatCreateQrcodeTicketResp> createQrcodeTicketCall = wechatApiService.getQrCodeTicket(accessToken, wechatCreateQrcodeTicketReq);
        Response<WechatCreateQrcodeTicketResp> qrcodeTicketResponse = createQrcodeTicketCall.execute();
        WechatCreateQrcodeTicketResp wechatCreateQrcodeTicketResp = qrcodeTicketResponse.body();
        if (wechatCreateQrcodeTicketResp == null) {
            throw new RuntimeException("获取二维码凭证失败失败");
        }
        String ticket = wechatCreateQrcodeTicketResp.getTicket();
        if (StrUtil.isBlank(ticket)) {
            throw new RuntimeException(String.format("获取二维码凭证失败失败，errMsg：{}，errCode：{}", wechatCreateQrcodeTicketResp.getErrcode(), wechatCreateQrcodeTicketResp.getErrmsg()));
        }
        log.info("生成二维码创建ticket，accessToken：{}，ticket：{}", accessToken, ticket);
        return ticket;
    }

    @Override
    public void saveLoginState(String ticket, String openid) {

        // 1.保存登录状态
        ticketCache.put(ticket, openid);
    }

    @Override
    public String checkLoginState(String ticket) {
        return ticketCache.getIfPresent(ticket);
    }

}
