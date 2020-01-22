package com.zheng.server;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.zheng.loadbalance.*;
import com.zheng.mq.*;
import com.zheng.netty.Remoting;
import com.zheng.util.SocketAddressUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.net.InetSocketAddress;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public abstract class AbstractServer implements Remoting {


    private IdGenerator idGenerator = IdGeneratorFactory.getDefaultGenerator();

    public abstract void setOpenService();

    public final static String ZOOKEEPER_CENTER = ResourceBundle.getBundle("zookeeper").getString("zookeeper.server.center");
    public final static String PROVIDER = ResourceBundle.getBundle("zookeeper").getString("zookeeper.path.provider");
    protected static int workerThreads = Runtime.getRuntime().availableProcessors() ;
    protected String serviceName;
    public static Long serverId;

    protected AtomicBoolean userExecutor ;
    protected AtomicBoolean userMqConsumer;
    protected abstract void setUserMqConsumer();
    protected abstract void setUserExecutor();

    protected abstract void addBackGroundService();
    protected ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(workerThreads));
    protected ExecutorCompletionService<Void> executorService;
    public Long getServerId() {
        return serverId;
    }


    public String getServiceName() {
        return serviceName;
    }

    protected abstract void setServiceName();

    protected AtomicBoolean openService = new AtomicBoolean(false);
    private EventLoopGroup boosGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap bootstrap;
    private static InetSocketAddress address;
    private volatile boolean binded = false;
    //    private final EventExecutorGroup group = new DefaultEventExecutorGroup(16);
    protected ChannelInitializer initializer;
    protected int workerThread = 1;
    @Autowired
    protected RegistProvider registProvider;
    @Autowired
    @Qualifier("defaultMqProduce")
    protected MqProduce mqProduce;

    protected abstract void shudDownWithCache()throws Exception;
    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    protected static String serverPath;

    protected abstract void setInitializer();

    public void setWorkerThread(int workerThread) {
        this.workerThread = workerThread;
    }

    public void setBossThread(int bossThread) {
        this.bossThread = bossThread;
    }

    protected int bossThread = 1;


    @Override
    public void start() {
        try {
            if (bootstrap == null) {
                log.error("exception cause ");
                throw new RuntimeException("slkjf");
            }

            ChannelFuture future = this.bootstrap.bind(address.getPort()).sync();

            binded = true;

            if (openService.get() == true) {
                this.serverId = idGenerator.generateId();
                ServerData sd = new ServerData(SocketAddressUtil.ip, Integer.valueOf(SocketAddressUtil.port));
                registProvider.regist(new ZookeeperRegistContext(serverId, serverPath, sd));
            }
            if (userMqConsumer.get() == true){
                MqConsumer consumer = MqConsumerFactory.getDefaultMqConsumer();
                consumer.consume();
                mqProduce.start();
            }
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("error on start " + e);
        }
    }

    @Override
    public void shutdown() {
        try {
            shut();
            shudDownWithCache();
        } catch (Exception e) {
            log.error("exception catch on shutdow " + e,e);
        } finally {
            shut();
        }
    }

    private void shut() {
        if (boosGroup != null) {
            boosGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }

    }

    @Override
    public void init() {
        try {
            if (binded) {
                return;
            }
            setOpenService();
            setServiceName();
            setInitializer();
            setUserExecutor();
            addBackGroundService();
            setUserMqConsumer();
            boosGroup = new NioEventLoopGroup(bossThread);
            workerGroup = new NioEventLoopGroup(workerThread);
            bootstrap = new ServerBootstrap();
            address = SocketAddressUtil.getSockeAddress();
            bootstrap.group(boosGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 1024 * 1024 * 8)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .localAddress(address)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childHandler(initializer);
        } catch (Exception e) {
            log.error("start error with" + e,e);
        }
    }
}
