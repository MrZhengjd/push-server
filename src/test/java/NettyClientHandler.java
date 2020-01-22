
import com.zheng.msg.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<Message> {
    private Boolean isOk = false;
    //利用写空闲发送心跳检测消息
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:{
//
//                    Header header = new Header("");
//                    Message message = new Message();
//                    ctx.writeAndFlush(message);
//                    ctx.writeAndFlush(pingMsg);
                    }
                    break;
                default:
                    break;
            }
        }
    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
//        switch (msg.getRequestType()){
//            case REQUEST:{
//                System.out.println(msg.toString());
//
//                Result result = JSON.parseObject(msg.getData(),Result.class);
////                if (result.getStatus() == ResultStatus.SUCCESS){
////                    System.out.println("nihao"+ msg.getData());
////                }
//            }break;
//            case RESPONSE:{
//                Result result = JSON.parseObject(msg.getData(),Result.class);
//
//                System.out.println("nihao"+ msg.getData());
//
//            }break;
//        }
//        log.info(msg.toString());
    }
}
