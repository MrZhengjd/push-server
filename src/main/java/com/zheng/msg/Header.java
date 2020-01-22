package com.zheng.msg;

import com.zheng.netty.ConstantValue;

import java.io.Serializable;

public class Header implements Serializable {

    private final int crcCode;

    public Header(int length, byte type) {
        this.crcCode = ConstantValue.HEAD_START;
        this.type = type;
        this.length = length;
    }

    @Override
    public String toString() {
        return "Header{" +
                "crcCode=" + crcCode +
                ", length=" + length +
                ", type=" + type +
                '}';
    }

    private int length;//消息长度



    private final byte type;//消息类型


    public Header( byte type) {
        this.crcCode = ConstantValue.HEAD_START;
        this.type = type;
    }

    public int getCrcCode() {
        return crcCode;
    }

    public int getLength() {
        return length;
    }


    public byte getType() {
        return type;
    }
}
