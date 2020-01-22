package com.zheng.msg;

public enum RequestCode {
    REQUEST_DEFAULT(0),
    REQUEST_LOGIN(1),
    REQUEST_PRIVATE(2);
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    RequestCode(int value) {
        this.value = value;
    }
    public static RequestCode fromName(Integer typeName) {
        for (RequestCode type : RequestCode.values()) {
            if (type.getValue() == typeName) {
                return type;
            }
        }
        return null;
    }

}

