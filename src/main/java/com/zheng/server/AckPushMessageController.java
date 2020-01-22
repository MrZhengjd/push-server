package com.zheng.server;

import com.zheng.constants.Constants;
import com.zheng.core.AckMessageCache;

import java.util.concurrent.Callable;

public class AckPushMessageController implements Callable<Void> {
    private volatile boolean stoped = false;

    @Override
    public Void call() throws Exception {
        AckMessageCache ref = AckMessageCache.getInstance();
        int timeout = Constants.SystemPropertyAckMessageControllerTimeOutValue;
        while (!stoped){
            if(ref.hold(timeout)){
                ref.commit();
            }
        }
        return null;
    }
    public void stop() {
        stoped = true;
    }

    public boolean isStoped() {
        return stoped;
    }
}
