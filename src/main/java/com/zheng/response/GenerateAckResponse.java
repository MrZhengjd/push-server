package com.zheng.response;

import com.zheng.msg.AckMessage;
import com.zheng.msg.BaseMessage;
import com.zheng.msg.Header;
import com.zheng.msg.ResponseMessage;

public class GenerateAckResponse implements GenerateResponse {
    @Override
    public ResponseMessage generateByType(BaseMessage baseMessage,String information) {
        ResponseMessage response = new ResponseMessage();
        Header header = new Header(baseMessage.getMessageType().value);
        AckMessage ackMessage = AckMessage.buildSuccess(baseMessage);
        response.setHeader(header);
        response.setMessage(ackMessage);
        return response;
    }
}
