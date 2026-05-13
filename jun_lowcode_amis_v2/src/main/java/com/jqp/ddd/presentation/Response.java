package com.jqp.ddd.presentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应对象
 *
 * @param <T> 数据类型
 * @author JQP
 * @date 2026/02/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

    /** 0-成功，-1-失败 */
    private int code = 0;

    /** 响应消息 */
    private String message = "成功";

    /** 响应数据 */
    private T data;

    // ============ 工厂方法 ============

    /**
     * 成功响应
     */
    public static <T> Response<T> ok(T data) {
        return new Response<>(0, "成功", data);
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> Response<T> ok() {
        return new Response<>(0, "成功", null);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> Response<T> ok(T data, String message) {
        return new Response<>(0, message, data);
    }

    /**
     * 失败响应
     */
    public static <T> Response<T> fail(String message) {
        return new Response<>(-1, message, null);
    }

    /**
     * 失败响应（指定错误码）
     */
    public static <T> Response<T> fail(int code, String message) {
        return new Response<>(code, message, null);
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return this.code == 0;
    }
}
