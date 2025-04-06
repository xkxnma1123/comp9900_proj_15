package com.comp9900.proj_15.common;

import lombok.Data;

/**
 * Unified response structure
 */
@Data
public class R<T> {

    /**
     * Status code
     */
    private Integer code;

    /**
     * Message
     */
    private String message;

    /**
     * Data
     */
    private T data;

    /**
     * Success response
     */
    public static <T> R<T> success(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMessage("Operation successful");
        r.setData(data);
        return r;
    }

    /**
     * Success response (custom message)
     */
    public static <T> R<T> success(String message, T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    /**
     * Error response
     */
    public static <T> R<T> error(String message) {
        R<T> r = new R<>();
        r.setCode(500);
        r.setMessage(message);
        return r;
    }

    /**
     * Error response (custom status code)
     */
    public static <T> R<T> error(Integer code, String message) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }


}
