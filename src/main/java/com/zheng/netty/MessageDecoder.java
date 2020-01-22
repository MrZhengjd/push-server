package com.zheng.netty;

import com.zheng.msg.BaseMessage;
import com.zheng.msg.BussinessMessage;
import com.zheng.msg.Header;
import com.zheng.msg.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.ResourceBundle;

@Slf4j
public class MessageDecoder extends ByteToMessageDecoder {

    private DataSerialize serializeUtil = DataSerializeFactory.getDefaultDataSerialize();
    private final static int HEADER_LENGTH = Integer.valueOf(ResourceBundle.getBundle("application").getString("header.length"));
    private final static int MAX_LENGTH = Integer.valueOf(ResourceBundle.getBundle("application").getString("max.length"));

    public MessageDecoder() {
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            if (in.readableBytes() < HEADER_LENGTH) {
                in.clear();
                return;
            }
            if (in.readableBytes() > MAX_LENGTH) {
                in.skipBytes(in.readableBytes());
                return;
            }
            int beginReader;
            while (true) {
                beginReader = in.readerIndex();
                in.markReaderIndex();
                if (in.readInt() == ConstantValue.HEAD_START) {
                    break;
                }

                in.resetReaderIndex();
                in.readByte();

                if (in.readableBytes() < HEADER_LENGTH) {
                    return;
                }
            }
            int length = in.readInt();

            byte type = in.readByte();
            log.info("rest byte"+in.readableBytes());
            if (in.readableBytes() < length)
            {
                in.resetReaderIndex();
                return;
            }
            Header header = new Header(length,type);
            byte[] data = new byte[length];
            in.readBytes(data);
            BaseMessage body = MessageUtil.getMessageByType(type);
            log.info("receivei message "+body);
            body = serializeUtil.deserialize(data,body.getClass());
            Message message = new Message(header, body);
            log.info("message"+message);
            out.add(body);
        } catch (Exception e) {
            log.error("decode error " + e,e);
        }
    }


}
