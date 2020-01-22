package com.zheng.msg;

import java.io.Serializable;

public class ResponseVo implements Serializable {
    private static final long serialVersionUID = 3470655256630865856L;

    private int code;
    private String message;
    private Object data;

    public ResponseVo(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ResponseVo{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    public static ResponseVo success(Object body){
        ResponseBuilder builder = success();
        return builder.build(body);
    }
    public static ResponseVo fail(Object body){
        ResponseBuilder builder = fail();
        return builder.build(body);
    }

    public static ResponseVo unAuthoried( Object body){
        ResponseBuilder builder = unAuthoried();
        return builder.build(body);
    }
    public static ResponseBuilder status(ResultStatus status){
        return new ResponseBuilder(status);
    }
    public static ResponseBuilder success(){
        return status(ResultStatus.SUCCESS);
    }
    public static ResponseBuilder unAuthoried(){
        return status(ResultStatus.UNAUTHORIZED);
    }
    public static ResponseBuilder fail(){
        return status(ResultStatus.FAILED);
    }
    public static class ResponseBuilder{
        private ResultStatus status;

        public ResponseBuilder(ResultStatus status) {
            this.status = status;
        }

        public ResultStatus getStatus() {
            return status;
        }

        public void setStatus(ResultStatus status) {
            this.status = status;
        }

        public ResponseVo build(Object data){
            return new ResponseVo(this.status.getValue(),data);
        }
    }
}
