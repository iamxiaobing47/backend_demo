package com.taco.backend_demo.common.response;

import org.springframework.http.ResponseEntity;

/**
 * 响应工具类
 */
public class ResponseFactory {

    public static <T> ResponseEntity<Response<T>> success() {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<Response<T>> success(T data) {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setData(data);
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<Response<T>> success(T data, String messageCode) {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setData(data);
        response.setMessageCode(messageCode);
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<Response<T>> success(T data, String messageCode, String... args) {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setData(data);
        response.setMessageCode(messageCode);
        response.setMessageArgs(args);
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<Response<T>> fail(String messageCode) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setMessageCode(messageCode);
        return ResponseEntity.ok(response);
    }


    public static <T> ResponseEntity<Response<T>> fail(String messageCode, String... args) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setMessageCode(messageCode);
        response.setMessageArgs(args);
        return ResponseEntity.ok(response);
    }

}