package com.zheng.container;

import org.springframework.context.support.AbstractApplicationContext;

public class SpringContext implements Context<AbstractApplicationContext> {
    private final AbstractApplicationContext context;

    public SpringContext(AbstractApplicationContext context) {
        this.context = context;
    }

    @Override
    public AbstractApplicationContext getContext() {
        return this.context;
    }
}
