package com.zheng.request;

import com.zheng.executor.MyExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CallBackInvoker<T> {
    private final CountDownLatch latch = new CountDownLatch(1);
    private T messageResult;
    private List<CallBackListener<T>> listeners = Collections.synchronizedList(new ArrayList<CallBackListener<T>>());
    private String requestId;
    private Throwable reason;

    public CallBackInvoker() {
    }

    public void join(CallBackListener<T> listener){
        this.listeners.add(listener);
    }
    public void setReason(Throwable reason) {
        this.reason = reason;
        publish();
        this.latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public T getMessageResult() {
        return messageResult;
    }

    public void setMessageResult(T messageResult) {
        this.messageResult = messageResult;
    }

    public List<CallBackListener<T>> getListeners() {
        return listeners;
    }

    public void setListeners(List<CallBackListener<T>> listeners) {
        this.listeners = listeners;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Throwable getReason() {
        return reason;
    }

    private void publish() {
        for (CallBackListener<T> listener : listeners){
            listener.onCallBack(messageResult);
        }
    }
    public Object getMessageResult(long timeout, TimeUnit unit){
        Long currentTime = System.currentTimeMillis();
        Object o =MyExecutor.submit(new Callable() {
            @Override
            public Object call() throws Exception {
                while ((System.currentTimeMillis()-currentTime) > unit.toNanos(timeout)){
                    if (latch.getCount() == 0)
                        return messageResult;
                }
                return null;
            }
        });
        return o;
    }
}
