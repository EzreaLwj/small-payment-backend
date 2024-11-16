package com.ezreal.small.payment.common.entity;

import lombok.Data;

/**
 * @author Ezreal
 * @Date 2024/2/2
 */
@Data
public class Response<T> {

    private T data;

    private String code;

    private String info;

    public Response(T data) {
        this.data = data;
    }

    public Response(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public Response(String code, String info, T data) {
        this.code = code;
        this.info = info;
        this.data = data;
    }
}
