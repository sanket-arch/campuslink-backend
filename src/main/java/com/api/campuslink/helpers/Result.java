package com.api.campuslink.helpers;

import org.springframework.http.HttpStatus;

public class Result<T> {
    private T data;
    private String error;
    private boolean success;
    private HttpStatus httpStatus;

    private Result(T data, String error, boolean success, HttpStatus httpStatus) {
        this.data = data;
        this.error = error;
        this.success = success;
        this.httpStatus = httpStatus;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, null, true, HttpStatus.OK);
    }

    public static <T> Result<T> error(String error) {
        return new Result<>(null, error, false, null);
    }

    public static <T> Result<T> error(String error, HttpStatus httpStatus) {
        return new Result<>(null, error, false, httpStatus);
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
