package com.taco.backend_demo.common.response;

import com.taco.backend_demo.common.response.Response;

/**
 * 响应工具类
 */
public class ResponseFactory {

    public static <T> Response<T> success() {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        return response;
    }

    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }

    public static <T> Response<T> success(T data, String messageCode) {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setData(data);
        response.setMessageCode(messageCode);
        return response;
    }

    public static <T> Response<T> success(T data, String messageCode, String... args) {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setData(data);
        response.setMessageCode(messageCode);
        response.setMessageArgs(args);
        return response;
    }

    public static <T> Response<T> fail(String messageCode) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setMessageCode(messageCode);
        return response;
    }


    public static <T> Response<T> fail(String messageCode, String... args) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setMessageCode(messageCode);
        response.setMessageArgs(args);
        return response;
    }

}