package com.gupaoedu.demo.netty;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public interface Worker {

    /**
     * 加入一个新的socketChannel
     * @param socketChannel
     */
    public void registerNewChannelTask(SocketChannel socketChannel);
}
