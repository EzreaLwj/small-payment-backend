package com.ezreal.small.payment.domain.request;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author Ezreal
 * @Date 2024/11/16
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class WechatCreateQrcodeTicketReq {

    /**
     * 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为60秒。
     */
    private Long expire_seconds;

    /**
     * 二维码类型，QR_SCENE为临时的整型参数值，QR_STR_SCENE为临时的字符串参数值，QR_LIMIT_SCENE为永久的整型参数值，QR_LIMIT_STR_SCENE为永久的字符串参数值
     */
    private String action_name;

    /**
     * 二维码详细信息
     */
    private ActionInfo action_info;

    @Data
    @Builder
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActionInfo {
        /**
         * 场景值ID（整型），临时二维码时为32位整型
         */
        private Long scene_id;
        /**
         * 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
         */
        private String scene_str;
    }

    @Getter
    @AllArgsConstructor
    public enum ActionType {
        /**
         * 临时的整型参数值
         */
        QR_SCENE("QR_SCENE"),
        /**
         * 临时的字符串参数值
         */
        QR_STR_SCENE("QR_STR_SCENE"),
        /**
         * 永久的整型参数值
         */
        QR_LIMIT_SCENE("QR_LIMIT_SCENE"),
        /**
         * 永久的字符串参数值
         */
        QR_LIMIT_STR_SCENE("QR_LIMIT_STR_SCENE");

        private final String actionType;

    }
}
