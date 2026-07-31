package com.likelion.dev_community.common;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final String message;
    private final String code;
    private final Object meta;

    private ApiResponse(boolean success, T data, String message, String code, Object meta) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.code = code;
        this.meta = meta;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, null, null);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(true, null, null, null, null);
    }

    public static <T> ApiResponse<T> success(String message, T data){
        return new ApiResponse<>(true, data, message, null, null);
    }

    public static <T> ApiResponse<T> success(String message,T data, Object meta){
        return new ApiResponse<>(true, data, message, null, meta);
    }


    public static ApiResponse<Void> error(String code, String message) {
        return new ApiResponse<>(false, null, message, code,null);
    }
}
