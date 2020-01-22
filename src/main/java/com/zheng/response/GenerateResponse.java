package com.zheng.response;

import com.zheng.msg.BaseMessage;
import com.zheng.msg.ResponseMessage;

public interface GenerateResponse {
    ResponseMessage generateByType(BaseMessage baseMessage,String information);
}
