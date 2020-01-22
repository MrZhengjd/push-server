package com.zheng.msg;

public enum ResultStatus {
    SUCCESS(200,"ok"),
    FAILED(40,"fail"),
    UNAUTHORIZED(401, "Unauthorized"),
    NOT_FOUND(404,"NotFound");
    private final int value;
    private final String message;

    public String getMessage() {
        return message;
    }

    public int getValue() {
        return value;
    }

    ResultStatus(int value, String message) {
        this.value = value;
        this.message = message;
    }
}
