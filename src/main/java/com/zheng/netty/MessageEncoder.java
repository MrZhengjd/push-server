package com.zheng.netty;

import com.zheng.msg.Header;
import com.zheng.msg.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<Message> {

    private DataSerialize dataSerialize = DataSerializeFactory.getDefaultDataSerialize();

    public MessageEncoder() {
    }

    public MessageEncoder(DataSerialize dataSerialize) {
        this.dataSerialize = dataSerialize;
    }

    /**
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        Header header = msg.getHeader();
        out.writeInt(header.getCrcCode());
        byte[] data = dataSerialize.serialize(msg.getMessage());
        out.writeInt(data.length);
        out.writeByte(header.getType());
        out.writeBytes(data);
    }
}
