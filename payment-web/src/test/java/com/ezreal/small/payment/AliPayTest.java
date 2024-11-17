package com.ezreal.small.payment;

import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Ezreal
 * @Date 2024/11/17
 */
@Slf4j
@SpringBootTest
public class AliPayTest {

    // 「沙箱环境」应用ID - 您的APPID，收款账号既是你的APPID对应支付宝账号。获取地址；https://open.alipay.com/develop/sandbox/app
    public static String app_id = "9021000125624045";

    // 「沙箱环境」商户私钥，你的PKCS8格式RSA2私钥 -【秘钥工具】所创建的公户私钥
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCfrfC336UDRnr26XXQn1E7Ak5lkZsFHlayPpU5N93GxkjrgTQaQQI+cEqe6UTToEm/YNlfSmtyitBDpI8goOBGbRAS4b9lQj9JrOiPF/Fvz31RkX2T4/E68cTRNBUSLnsC2BgoWVFOcl7Gmz0y+LrL5oOIFltISlgz5YeU/FR+7Cg6Hf5gOdrYTzEc4F8Fh8E3wxD55ebwxLRafz5H+dr8JZqegvRO3J6Z0fzMqXf3Ejrc+2Qn2ZCgH1XT/GrmTp9imIxzL/JxKhuuGlONfvRrEP06sEGHHLcZrhSB0/9IF5vm/XlcIyUCnK+I04yWh9VT/XbOYX9dC6VzbBNIN+O9AgMBAAECggEAFHgGoHtxuq7BOn+I07pofkj4Zbq5hXlzIZw2IsSPkFiApnfEhw0ITZq1ZhN++CXW7kpq8vUmOuOieNlSY5tbxItcgR8hGsjYNgiNMzKdh3iwltEwqNGSbbzqPusPPGpq6F1tE1cHvieZEJp7yNnrkBG3kK1U/DpTfiuRKr8B0dcdVUVIt/LOqDRnk05dYpNdS6w98LByjsGqec6I/iv1n/+bW7g4VJtHuSOB+bT1K4amAcMFYb3AKOEzMFbjrjEhKIxykMv7O9S/y3Tp7hDCAxAdOocd68M1WPCdhnG8F1BQEoLwslBnOQxuTKF11gSXK3LiVegUZUoVZAcclcP7/QKBgQDxe47v+ErPD5c2vPFDtSHb1Eeq3Pu6vD1D3RWRDbYxpFymPip6/7ZkLiYiarVZozRIgeJCo9MSg5GQzmnuunT/BuR6IUjLKkuCVuBTvl3tGVrH7tYdfqhrTcwemxZnW7iZ3c94UesdwucXxXgPpShHL8vmtesnFHRkKbgURM0vTwKBgQCpR2w5+YQCCFAiXRJIDLIU1U+m7iCmbKcX5EmvxAg/BoHxUbN+bxDjKjWOGm+/2RFt2UD0RW5BNUlbS8dS6LHB8uPJwd95MVs+QIC+qM3WDO5r0pdcmAGvd7f0hztOkwsyVxkAi2tjgYpnEOrY5P0IHzHIYn96N//JIZX+EotZMwKBgGrb+3ezjx8rzgV5/OWyXOZTfNdebMLIk+/ZALcssygqjJlAPzoouESCq3tlbxTYhMVlPIOyzS+PKzZCMVxkBGvqV+a3nefRspJd8Wz9e8D1DFgXF4b7ynmZdq9Af9yDGSB1qF6dGYcU/YBmcOezdSxzOS7B5+pmQc8lKwFFJOs/AoGAE6QJ1qAwxNuaEKKj4GA4uVoWp9OxTh4FNSxzsEUaf9WNdiZ4oQ7Z8sGO/THsDWJuN0Sh2LXFmSNJo6IjOmdtWIeKDnrEJxem8o4hYf3MBC3z6+a/USsB5w4I9gJKy08cWXbpm7qRdzYsjNDVrtzCzxJvgESNbezZbMjM0nHsYSkCgYBYRdh6JowZDkYi1atarHEBTytOoZAZVa26Iv4l4Nybf0WT9E9crosfQzsftQBycxPuoqlzTC7ItZTvDifZHugVeqzVOLBy2n7eaBGaIwoLDWnzAPP1MAvPDmCsr+gXWDlMBsVxk5IybrdtJdF2hn9hYXRaqmBqeRuYk4YQeGBQSw==";

    // 「沙箱环境」支付宝公钥 -【秘钥填写】后提供给你的支付宝公钥
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl9FEZlaCniSh/aWhkDbiN3QlDlXF+HHoNkL6z5N7xNOsFb+OOMMxfULamnvd3pClNCx8RdsfIfp5QnSE2vpW6nEshyZwtBtxczbJ5bvIgjqE7sP99vjrhE/Prh59x5fpjtWvzkv5qusv6fyYLxyqqlWEW0y2OgInA+bJ+E/OxRGclMkCVCm24V0DI+nLPqoD2tdU7U6aAliIyUFhp2314PMAx/aoEdEZ0JIkA2/Kl9utv0oENHuzFV3qJb9nQrO7KUHO3sT4uqekm2MrPes6rooBsa1GIdij3X3FoXL18Q9LkjDbnw34wZRTPbfPhwc+wARA8wPAn/JIN16R2f2qcQIDAQAB";

    // 「沙箱环境」服务器异步通知回调地址
    public static String notify_url = "http://ezreal-payment.natapp1.cc/api/v1/alipay/alipay_notify_url";

    // 「沙箱环境」页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "https://www.baidu.com";

    // 「沙箱环境」
    public static String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    private AlipayClient alipayClient;

    @BeforeEach
    public void init() {
        this.alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id,
                merchant_private_key,
                "json",
                charset,
                alipay_public_key,
                sign_type);
    }

    @Test
    public void test_alipay_pageExecute() throws AlipayApiException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(notify_url);
        request.setReturnUrl(return_url);
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", "xfg2024092709120005");  // 我们自己生成的订单编号
        bizContent.put("total_amount", "0.01"); // 订单的总金额
        bizContent.put("subject", "测试商品");   // 支付的名称
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");  // 固定配置
        request.setBizContent(bizContent.toString());

        String form = alipayClient.pageExecute(request).getBody();
        log.info("测试结果：{}", form);
    }

    /**
     * 查询订单
     */
    @Test
    public void test_alipay_certificateExecute() throws AlipayApiException {

        AlipayTradeQueryModel bizModel = new AlipayTradeQueryModel();
        bizModel.setOutTradeNo("1128744013874287");

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizModel(bizModel);

        String body = alipayClient.execute(request).getBody();
        log.info("测试结果：{}", body);
    }

    /**
     * 退款接口
     */
    @Test
    public void test_alipay_refund() throws AlipayApiException {
        AlipayTradeRefundRequest request =new AlipayTradeRefundRequest();
        AlipayTradeRefundModel refundModel =new AlipayTradeRefundModel();
        refundModel.setOutTradeNo("daniel82AAAA000032333361X03");
        refundModel.setRefundAmount("1.00");
        refundModel.setRefundReason("退款说明");
        request.setBizModel(refundModel);

        AlipayTradeRefundResponse execute = alipayClient.execute(request);
        log.info("测试结果：{}", execute.isSuccess());
    }
}
