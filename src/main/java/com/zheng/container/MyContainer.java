package com.zheng.container;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ResourceBundle;

public class MyContainer implements Container {
    private SpringContext context;
    private final static String XML_PATH = ResourceBundle.getBundle("application").getString("start");
    @Override
    public void start() {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext(XML_PATH);
        this.context = new SpringContext(context);
        context.start();
    }

    @Override
    public void stop() {
        if (context != null && context.getContext() != null){
            context.getContext().close();
            context = null;
        }
    }

    @Override
    public Context getContext() {
        return context;
    }
}
