package com.lidashuang.jt;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author lidashuang
 * @version 1.0
 */
public class JtClient {
    public static void main(String[] args) throws InterruptedException {
        final EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        System.out.println("正在连接中...");
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ChannelInboundHandler() {
                            @Override
                            public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {
                                System.out.println("channelRegistered");
                            }

                            @Override
                            public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {
                                System.out.println("channelUnregistered");
                            }

                            @Override
                            public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
                                System.out.println("channelActive");

                                byte[] bytes = new byte[] {
                                        126, 1, 0, 0, 54, 0, 4, 121, 55, 83, 41, 17, -127, 0, 0, 0, 0, 0, 0, 3, 32, 125, 1, 72, 68, 67, 84, 77, 83, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 67, 57, 50, 65, 70, 69, 49, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -9, 126
                                };
                                System.out.println(Arrays.toString(bytes));
                                channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(bytes));
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
                                System.out.println("channelInactive");
                            }

                            @Override
                            public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
                                // 读取数据流
                                ByteBuf byteBuf = (ByteBuf) o;
                                final byte[] bytes = new byte[byteBuf.readableBytes()];
                                byteBuf.readBytes(bytes);
                                System.out.println("收到  " + Arrays.toString(bytes));
                            }

                            @Override
                            public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
                                System.out.println("channelReadComplete");
                            }

                            @Override
                            public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
                                System.out.println("userEventTriggered");
                            }

                            @Override
                            public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {
                                System.out.println("channelWritabilityChanged");
                            }

                            @Override
                            public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
                                System.out.println("exceptionCaught");
                            }

                            @Override
                            public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
                                System.out.println("handlerAdded");
                            }

                            @Override
                            public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
                                System.out.println("handlerRemoved");
                            }
                        });

                    }
                });
        //发起异步连接请求，绑定连接端口和host信息
        final ChannelFuture future = b.connect("127.0.0.1", 7611).sync();
    }
}
