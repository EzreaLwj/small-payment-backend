package com.ezreal.small.payment.common.utils;

import com.ezreal.small.payment.common.entity.Constants;
import com.ezreal.small.payment.common.entity.Response;

/**
 * @author Ezreal
 * @Date 2024/2/2
 */
public class ResultUtils {

    public static <T> Response<T> success(T data) {
        return new Response<>(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo(), data);
    }

    public static <T> Response<T> fail(String code, String info) {
        return new Response<>(code, info);
    }

    public static <T> Response<T> fail(Constants.ResponseCode code) {
        return new Response<>(code.getCode(), code.getInfo());
    }
}
