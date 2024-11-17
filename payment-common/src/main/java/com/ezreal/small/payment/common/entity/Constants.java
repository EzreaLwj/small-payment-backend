package com.ezreal.small.payment.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Ezreal
 * @Date 2024/2/2
 */
public class Constants {

    public static class DeleteConstants {

        public static Integer NOT_DELETE = 0;

        public static Integer DELETE = 1;
    }

    public static class OrderConstants {

        public static String DESC = "desc";

        public static String asc = "ASC";
    }

    public static class FileConstants {
        public static String COS_HOST = "https://ezreal-interview-1312880100.cos.ap-guangzhou.myqcloud.com/";
    }

    public static class UserConstants {

        /**
         * 用户登录态键
         */
        public static final String USER_LOGIN_STATE = "user_login";

        //  region 权限

        /**
         * 默认角色
         */
        public static final String DEFAULT_ROLE = "user";

        /**
         * 管理员角色
         */
        public static final String ADMIN_ROLE = "admin";

        /**
         * 被封号
         */
        public static final String BAN_ROLE = "ban";

        // endregion
    }


    @Getter
    public enum ResponseCode {
        SUCCESS("0000", "成功"),
        UN_ERROR("0001", "未知失败"),
        LOGIN_FAIL("0002", "账号或者密码错误"),
        UN_LOGIN("0003", "用户未登录"),
        ERROR_PARAMS("0004", "参数错误"),
        USER_EXIST("0005", "用户已经存在"),
        UPDATE_FAIL("0006", "更新失败"),
        EMAIL_CODE_SEND_FAIL("0007", "邮箱验证码发送失败"),
        EMAIL_REGISTER_CODE_FAIL("0008", "邮箱验证码错误"),
        USER_NOT_FOUND("0009", "用户未找到"),
        INSERT_FAIL("0011", "插入失败"),
        DELETE_FAIL("0012", "删除失败"),
        BANK_NOT_FOUND("0013", "题库未找到"),
        NOT_ALLOW("0014", "权限不足");

        private final String code;
        private final String info;

        ResponseCode(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }

    @Getter
    public enum RedisCode {

        EMAIL_REGISTER_CODE("email:register:code:", "邮箱注册key");

        private final String code;
        private final String info;

        RedisCode(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum OrderStatusEnum {

        CREATE("CREATE", "创建完成 - 如果调单了，也会从创建记录重新发起创建支付单"),
        PAY_WAIT("PAY_WAIT", "等待支付 - 订单创建完成后，创建支付单"),
        PAY_SUCCESS("PAY_SUCCESS", "支付成功 - 接收到支付回调消息"),
        DEAL_DONE("DEAL_DONE", "交易完成 - 商品发货完成"),
        CLOSE("CLOSE", "超时关单 - 超市未支付"),
        ;

        private final String code;
        private final String desc;

    }
}
