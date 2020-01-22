package com.zheng.response;

import com.zheng.msg.*;

public class GeneratePushResponse implements GenerateResponse {
    @Override
    public ResponseMessage generateByType(BaseMessage baseMessage,String information) {

        ResponseMessage response = new ResponseMessage();
        Header header = new Header(baseMessage.getMessageType().value);
        PushMessage pushMessage = (PushMessage) baseMessage;

        response.setHeader(header);
        response.setMessage(pushMessage);
        return response;
    }
}
