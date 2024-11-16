package com.ezreal.small.payment.service;

import com.ezreal.small.payment.domain.request.WechatCreateQrcodeTicketReq;
import com.ezreal.small.payment.domain.response.WechatAccessTokenResp;
import com.ezreal.small.payment.domain.response.WechatCreateQrcodeTicketResp;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 微信对接接口
 *
 * @author Ezreal
 * @Date 2024/11/16
 */
public interface WechatApiService {

    /**
     * 获取微信操作accessToken
     * https请求方式: GET https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
     *
     * @param grantType 获取access_token填写client_credential
     * @param appid     第三方用户唯一凭证
     * @param secret    第三方用户唯一凭证密钥，即appsecret
     * @return 结果
     */
    @GET("token")
    Call<WechatAccessTokenResp> getAccessToken(@Query("grant_type") String grantType,
                                               @Query("appid") String appid,
                                               @Query("secret") String secret);

    /**
     * http请求方式: POST URL: https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN
     *
     * @param accessToken                 accessToken
     * @param wechatCreateQrcodeTicketReq 创建二维码凭证
     * @return 响应
     */
    @POST("qrcode/create")
    Call<WechatCreateQrcodeTicketResp> getQrCodeTicket(@Query("access_token") String accessToken,
                                                       @Body WechatCreateQrcodeTicketReq wechatCreateQrcodeTicketReq);
}
