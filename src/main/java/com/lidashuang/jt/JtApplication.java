package com.lidashuang.jt;

import com.lidashuang.jt.actuator.Jtt808T5Actuator;
import com.lidashuang.jt.actuator.Jtt808T8Actuator;
import com.lidashuang.jt.jtt808.Jtt808T5;
import com.lidashuang.jt.jtt808.Jtt808T7;
import com.lidashuang.jt.jtt808.Jtt808T8;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * 华为云
 * 139.159.216.99
 * root
 * 777nnnnnuuuuuMMMMttt
 *
 * /data/source003
 *
 * @author lidashuang
 * @version 1.0
 *
 * 258 256 3
 * 16 * 16
 */
@SpringBootApplication
public class JtApplication {


    static {
        JttRegistry.registerMessage(Jtt808T5.M_ID, new Jtt808T5());
        JttRegistry.registerMessage(Jtt808T7.M_ID, new Jtt808T7());
        JttRegistry.registerMessage(Jtt808T8.M_ID, new Jtt808T8());

        // 处理登录消息
        // 终端注册
        JttRegistry.registerActuator(Jtt808T5.M_ID, new Jtt808T5Actuator());
        // 终端鉴权
        JttRegistry.registerActuator(Jtt808T8.M_ID, new Jtt808T8Actuator());
    }

    public static void main(String[] args) {
        SpringApplication.run(JtApplication.class, args);
        final EventLoopGroup bossGroup = new NioEventLoopGroup();
        // Worker线程
        final EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // Netty服务
            // ServerBootstrap   ServerSocketChannel
            final ServerBootstrap server = new ServerBootstrap();
            // 链路式编程
            server.group(bossGroup, workerGroup)
                    // 主线程处理类,看到这样的写法，底层就是用反射
                    .channel(NioServerSocketChannel.class)
                    // 子线程处理类 , Handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) {
                            // 管道中添加处理器
                            channel.pipeline()
                                    .addLast(new JttDecoder())
                                    .addLast(new JttEncoder())
                                    .addLast(new JttHandler());
                        }
                    });
//                    .option(ChannelOption.SO_BROADCAST, true)
//                    .option(ChannelOption.SO_RCVBUF, 2048 * 1024)
//                    .option(ChannelOption.SO_SNDBUF, 1024 * 1024);
            // 启动服务器
            final ChannelFuture channelFuture = server.bind(7611).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            // 关闭线程池
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
