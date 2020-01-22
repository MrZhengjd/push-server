package com.zheng.container;




import com.zheng.server.SocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
@Slf4j
public final class ServerBootInfo extends SocketServer implements ApplicationContextAware, InitializingBean {
    public ServerBootInfo() {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setWorkerThread(8);
        init();
        start();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("server start");
    }

}
