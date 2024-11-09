package com.ezreal.small.payment.common.wechat;

import cn.hutool.crypto.digest.DigestUtil;

import java.util.Arrays;

/**
 * @author Ezreal
 * @Date 2024/11/9
 */
public class SignUtils {

    /**
     * 检验微信签名
     *
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param token     token
     * @return 结果
     */
    public static boolean verifyWeChatSign(String signature, String timestamp, String nonce, String token) {

        // 对token、timestamp和nonce按字典排序
        String[] arr = new String[]{token, timestamp, nonce};
        Arrays.sort(arr);

        // 将排序后的结果拼接成一个字符串
        StringBuilder content = new StringBuilder();
        for (String s : arr) {
            content.append(s);
        }
        String hexStr = null;

        try {
            // 进行SHA1加密
            hexStr = DigestUtil.sha1Hex(content.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return hexStr != null && hexStr.equals(signature);
    }

    /**
     * 进行字典排序
     */
    private static void sort(String[] str) {
        for (int i = 0; i < str.length - 1; i++) {
            for (int j = i + 1; j < str.length; j++) {
                if (str[j].compareTo(str[i]) < 0) {
                    String temp = str[i];
                    str[i] = str[j];
                    str[j] = temp;
                }
            }
        }
    }
}
