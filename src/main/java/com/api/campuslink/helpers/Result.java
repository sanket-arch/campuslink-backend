package com.api.campuslink.helpers;

public class Result<T> {
    private T data;
    private String error;
    private boolean success;

    private Result(T data, String error, boolean success) {
        this.data = data;
        this.error = error;
        this.success = success;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, null, true);
    }

    public static <T> Result<T> error(String error) {
        return new Result<>(null, error, false);
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
}
