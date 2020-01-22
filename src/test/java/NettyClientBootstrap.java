import com.zheng.constants.Constants;
import com.zheng.msg.SendMessage;
import com.zheng.netty.*;
import com.zheng.loadbalance.ServerData;
import com.zheng.msg.Header;
import com.zheng.msg.Message;
import com.zheng.msg.PingMessage;
import com.zheng.redis.Login;
import com.zheng.redis.TokenGeneratorFactory;
import com.zheng.request.RequestProxy;
import com.zheng.request.SendSocketRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class NettyClientBootstrap {
    private int port;
    private String host;
    private SocketChannel socketChannel;
    private final BalanceProvider<ServerData> provider = new DefaultBalanceProvider();
    private final static String serviceName = ResourceBundle.getBundle("application").getString("service.name");
    private final static String CONSUMER = ResourceBundle.getBundle("zookeeper").getString("zookeeper.path.consumer");
    private static final EventExecutorGroup group = new DefaultEventExecutorGroup(20);

    public NettyClientBootstrap() throws InterruptedException {

        start();
    }

    private void start() throws InterruptedException {
        try {
            EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
            ServerData serverData = provider.getBalanceItem(Constants.IM_SERVER_PATH);
            if (serverData == null) {
                log.error("no server found");
                throw new RuntimeException("no server found");
            }
            port = serverData.getPort();
            host = serverData.getHost();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535));

            bootstrap.group(eventLoopGroup);
            bootstrap.remoteAddress(host, port);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new IdleStateHandler(20, 10, 0));
                    socketChannel.pipeline().addLast(new MessageEncoder());
                    socketChannel.pipeline().addLast(new MessageDecoder());
                    socketChannel.pipeline().addLast(new NettyClientHandler());
                }
            });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            String consumerPath =  Constants.ZOOKEEPER_CENTER+"/"+serviceName+"/"+CONSUMER;
            provider.regist(consumerPath,host+":"+port);
            if (future.isSuccess()) {
                socketChannel = (SocketChannel) future.channel();
                System.out.println("connect server  成功");
            }
        } catch (Exception e) {
            log.error("connection eroor " + e);
        }

    }

    public static void main(String[] args) throws InterruptedException {

//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXlsb2FkIjoie1widWlkXCI6XCIzXCIsXCJ1c2VyTmFtZVwiOlwiemhlbmdcIn0iLCJleHAiOjE1Njc0NDU1NjA2MzF9.Qx3uW1QMwH1Vkl_qiJ07EWMOD0ujxXkJvC6_2xlmiNI";
        String uid = "3";
        Login login = new Login();
        login.setUid(uid );
        login.setUserName("zheng");
        String token = TokenGeneratorFactory.getDefaultTokenGenerator().create(login,300000);

        NettyClientBootstrap bootstrap = new NettyClientBootstrap();
        if (bootstrap == null) {
            return;
        }
        AtomicLong count = new AtomicLong(0);
//        User user = new User("zqdl2576","15767103852");
//        String userString = JSONObject.toJSONString(user);
//        PingMessage pingMessage = new PingMessage("ping");
//        Header header = new Header(CacheKeys.USER_ID,MimeType.TEXT,RequestCode.REQUEST_LOGIN);
//        Message message = new Message(RequestType.REQUEST,header, userString);
//        bootstrap.socketChannel.writeAndFlush(message);
//        byte[] bytes = pingMessage.toString().getBytes();
//        Header header = new Header( MessageType.PING.getValue());
//        Message message = new Message(header, pingMessage);
//        for (int i = 0; i < 20; i++) {
//            bootstrap.socketChannel.writeAndFlush(message);
//        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setUid(uid);
        sendMessage.setToken(token);
        sendMessage.setFromUser("3");
        sendMessage.setToUser("4");
        sendMessage.setMessage("nihao");
        RequestProxy proxy = new RequestProxy(bootstrap.socketChannel);
        CountDownLatch latch = new CountDownLatch(10);
        System.out.println("welcome to here send message"+sendMessage.toString());
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            for (int i=0;i<10;i++){
                executorService.submit(()->{

                    proxy.send(sendMessage);

                });
                count.incrementAndGet();
                latch.countDown();
            }
            latch.await(60, TimeUnit.SECONDS);
            executorService.shutdownNow();
//            latch.await();
            System.out.println("have send request count "+count.get());
        }catch (Exception e){
            log.error("exception",e);
        }





    }
}
