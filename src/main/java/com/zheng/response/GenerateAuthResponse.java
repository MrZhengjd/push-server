package com.zheng.response;

import com.zheng.msg.*;

public class GenerateAuthResponse implements GenerateResponse {
    @Override
    public ResponseMessage generateByType(BaseMessage baseMessage,String information) {
        ResponseMessage response = new ResponseMessage();
        Header header = new Header(baseMessage.getMessageType().value);
        ResponseVo responseVo = ResponseVo.unAuthoried(information);
        response.setHeader(header);
        AuthMessage authMessage = new AuthMessage();
        authMessage.setResponseVo(responseVo);
        response.setMessage(authMessage);
        return response;
    }
}
