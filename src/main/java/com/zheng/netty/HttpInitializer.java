package com.zheng.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.ResourceBundle;

public class HttpInitializer extends ChannelInitializer {
    private final static int maxContentLength = Integer.valueOf(ResourceBundle.getBundle("http-server").getString("http.max.contentlength"));
    private  EventExecutorGroup group;
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpObjectAggregator(maxContentLength));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        // 启用gzip（由于使用本地存储文件，不能启用gzip）
        //pipeline.addLast(new HttpContentCompressor(1));
        pipeline.addLast(new ChunkedWriteHandler());
        // 将HttpRequestHandler放在业务线程池中执行，避免阻塞worker线程。
        pipeline.addLast(group, "httpRequestHandler", new HttpRequestHandler());
    }
}
