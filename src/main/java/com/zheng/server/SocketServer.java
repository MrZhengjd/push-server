package com.zheng.server;

import com.zheng.netty.DistributeSession;
import com.zheng.netty.ServerInitilizer;
import com.zheng.redis.CacheService;
import com.zheng.redis.CacheServiceFactory;
import io.netty.channel.ChannelInitializer;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.ResourceBundle;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public class SocketServer extends AbstractServer {


    private final static String LOCAL_SERVICE_NAME = ResourceBundle.getBundle("application").getString("service.name");

    private CacheService cacheService = CacheServiceFactory.getRediscacheService();


    @Override
    public void setOpenService() {
        this.openService =new  AtomicBoolean(true);

    }

    @Override
    protected void setUserMqConsumer() {
        userMqConsumer = new AtomicBoolean(true);
    }


    @Override
    protected void setUserExecutor() {
        this.userExecutor = new AtomicBoolean(true);
    }



    @Override
    protected void addBackGroundService() {
        if (userExecutor.get() == false){
           return;
        }

        try {
            executorService = new ExecutorCompletionService<Void>(executor);
            for (int i = 0;i<workerThreads;i++){
                executorService.submit(new AckPushMessageController());
                executorService.submit(new AckPullMessageController());
                executorService.submit(new FirePushMessageController());
            }

        }catch (Exception e){
            log.error("start mutile thread error " + e);
        }

    }


    @Override
    public void setServiceName() {
        this.serviceName = LOCAL_SERVICE_NAME;
    }

    @Override
    protected void shudDownWithCache() throws Exception {
        cacheService.delete(DistributeSession.buildNull());
    }

    @Override
    protected void setInitializer() {
        EventExecutorGroup group = new DefaultEventExecutorGroup(16);
        ChannelInitializer serverInitilizer ;
        serverPath = ZOOKEEPER_CENTER.concat("/").concat(serviceName).concat("/").concat(PROVIDER);
        if (openService.get() != true){
            serverInitilizer= new ServerInitilizer(group,null);
        }else {
            serverPath = ZOOKEEPER_CENTER.concat("/").concat(serviceName).concat("/").concat(PROVIDER);
            serverInitilizer = new ServerInitilizer(group,serverPath);
        }
        this.initializer = serverInitilizer;
    }

    public SocketServer() {


    }
}
