package com.gupaoedu.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NoIoServer {

    public static void main(String[] args) {
        try {
            // 和阻塞ServerSocket一个意思
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 为非阻塞
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(8080));
            // 不再阻塞
            while (true){
                // SocketChannel和客户端的socket一个意思
                SocketChannel socketChannel = serverSocketChannel.accept();
                if(socketChannel != null){
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    socketChannel.read(byteBuffer);
                    System.out.println(new String(byteBuffer.array()));

                    byteBuffer.flip();
                    socketChannel.write(byteBuffer);
                }else{
                    Thread.sleep(1000);
                    System.out.println("没有客户端连接");
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
