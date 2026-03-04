package com.taco.backend_demo.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应对象
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Response<T> {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 数据
     */
    private T data;

    /**
     * 消息代码
     */
    private String messageCode;

    /**
     * 消息参数数组
     */
    private String[] messageArgs;
}