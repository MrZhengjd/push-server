package com.zheng.netty;

import com.zheng.msg.BaseMessage;
import com.zheng.msg.ResponseMessage;
import com.zheng.redis.*;
import com.zheng.response.GenerateContext;
import com.zheng.server.AbstractServer;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


@Slf4j
@Getter
@Setter
@Component
public class AuthHandler extends SimpleChannelInboundHandler<BaseMessage> {

    public AuthHandler() {

    }


    private final GenerateContext context = new GenerateContext();


    private TokenGenerator tokenGenerator = TokenGeneratorFactory.getDefaultTokenGenerator();


    private CacheService cacheService = CacheServiceFactory.getRediscacheService();


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseMessage msg) throws Exception {
        try {
            String token = msg.getToken();
            String uid = msg.getUid();
            if (StringUtils.isEmpty(token) || StringUtils.isEmpty(uid)) {
                unAuthResponse(msg, ctx, "unauthor");
                return;
            }
            Login login = tokenGenerator.unsign(token, Login.class);

            if (login == null || !StringUtils.equals(login.getUid(), uid)) {
                unAuthResponse(msg, ctx, "user id no auth");
                return;
            }
//        if (SocketChannelMap.get(uid)==null){
//            unAuthResponse(msg,ctx,"connection lost");
//        }
            RedisLogin redisLogin = (RedisLogin) cacheService.get(new RedisLogin(uid), RedisLogin.class);
            if (null == redisLogin) {
                unAuthResponse(msg, ctx, "un login error");
                return;
            }
            if (!StringUtils.equals(token, redisLogin.getToken())) {
                unAuthResponse(msg, ctx, "USERID_NOT_UNAUTHORIZED");
                return;
            }

            if (System.currentTimeMillis() > redisLogin.getRefTime()) {
                unAuthResponse(msg, ctx, "login expire");
                return;
            }
            DistributeSession session = (DistributeSession) cacheService.get(DistributeSession.build(ctx.channel(), uid), DistributeSession.class);
            if (session != null && session.getServerId()!= AbstractServer.serverId){
//                unAuthResponse(msg, ctx, "haved login on other servers");
                log.info("login multi place" + login);

            }
            if (NettyChannelMap.get(uid) == null) {
                cacheService.put(DistributeSession.build(ctx.channel(), uid));
                NettyChannelMap.add(uid, ctx.channel());
            }
            log.info("welcome ..........");
            ctx.fireChannelRead(msg);
        } catch (Exception e) {
            log.error(e + "error");
        }


    }

    public void unAuthResponse(BaseMessage msg, ChannelHandlerContext ctx, String message) {
        msg.setMessageType(MessageType.AUTH);
        ResponseMessage responseMessage = context.invokeStrategy(msg, message);
        ctx.writeAndFlush(responseMessage).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                future.channel().close();
            }
        });
    }
}
