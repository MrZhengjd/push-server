package com.zheng.netty;

import com.zheng.loadbalance.BalanceUpdateProvider;
import com.zheng.loadbalance.DefaultBalanceUpdateProvider;
import com.zheng.msg.BaseMessage;
import com.zheng.redis.CacheService;
import com.zheng.redis.CacheServiceFactory;
import com.zheng.strategy.StrategyContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class ServerHandler extends SimpleChannelInboundHandler<BaseMessage> {
//    public final static String SERVER = ResourceBundle.getBundle("zookeeper").getString("zookeeper.server.location");
    private final StrategyContext context = new StrategyContext();
    private static ServerHandler serverHandler;


    @PostConstruct
    public void init(){
        serverHandler = this;
        serverHandler.cacheService = this.cacheService;
        serverHandler.balanceProvider = this.balanceProvider;
    }
    public void setBalanceProvider(BalanceUpdateProvider balanceProvider) {
        this.balanceProvider = balanceProvider;
    }

    private CacheService cacheService = CacheServiceFactory.getRediscacheService();
    private BalanceUpdateProvider balanceProvider;
    private static final Integer BALANCE_STEP = 1;
    private static AtomicBoolean banlanced = new AtomicBoolean(true);

    public BalanceUpdateProvider getBalanceProvider() {
        return balanceProvider;
    }

    public ServerHandler() {
        super();
        String serverPath = ServerInitilizer.serverPath;
        if (StringUtils.isEmpty(serverPath)){
            return;
        }
        balanceProvider = new DefaultBalanceUpdateProvider(serverPath);
    }

    public ServerHandler(BalanceUpdateProvider balanceProvider) {
        this.balanceProvider = balanceProvider;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseMessage msg) throws Exception {
        try {
            System.out.println("get message "+ msg);
            context.invokeStrategy(msg);
        } catch (Exception e) {
            log.error("error handle " + e);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        log.info("one client connect...");
        if (banlanced.get() == false) {
            return;
        }

        if (balanceProvider != null) {
            balanceProvider.addBalance(BALANCE_STEP);
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("one client lost connect...");
        if (banlanced.get() == false) {
            return;
        }
        if (balanceProvider != null) {
            balanceProvider.reduceBalance(BALANCE_STEP);
        }
        Channel channel = ctx.channel();
        String clientId = NettyChannelMap.contain(channel);
        if (StringUtils.isEmpty(clientId)){
            return;
        }
        NettyChannelMap.deleteByClientId(clientId);
        cacheService.delete(DistributeSession.build(channel,clientId));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exception catch " + cause);
        ctx.close();
    }
}
