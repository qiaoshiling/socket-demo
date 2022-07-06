package com.gupaoedu.demo.netty;

import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;

public class ServerBootStrap {

    private NioSelectorRunnablePool nioSelectorRunnablePool;

    public ServerBootStrap(NioSelectorRunnablePool nioSelectorRunnablePool) {
        this.nioSelectorRunnablePool = nioSelectorRunnablePool;
    }

    public void bind(final SocketAddress localAddress){
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(localAddress);

            Boss nextBoss = nioSelectorRunnablePool.nextBoss();
            nextBoss.registerAcceptChannelTask(serverSocketChannel);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
