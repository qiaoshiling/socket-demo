package com.gupaoedu.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 咕泡学院，只为更好的你
 * 咕泡学院-Mic: 2227324689
 * http://www.gupaoedu.com
 **/
public class NewIoServer {

    static Selector selector;


    public static void main(String[] args) {
        try {
            selector=Selector.open();
            //selector 必须是非阻塞
            ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false); //设置为非阻塞
            serverSocketChannel.socket().bind(new InetSocketAddress(8080));
            // 往selector上面注册一个OP_ACCEPT事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); //把连接事件注册到多路复用器上
            while(true){
                // 阻塞机制  你有事件，我就不阻塞，你没有事件，我就阻塞==》有客户端连接不阻塞，没有就阻塞
                selector.select();
                Set<SelectionKey> selectionKeySet=selector.selectedKeys();
                Iterator<SelectionKey> iterable=selectionKeySet.iterator();
                while(iterable.hasNext()){
                    SelectionKey key=iterable.next();
                    iterable.remove();
                    if(key.isAcceptable()){ //连接事件
                        handleAccept(key);
                    }else if(key.isReadable()){ //读的就绪事件
                        handleRead(key);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleAccept(SelectionKey selectionKey){

        ServerSocketChannel serverSocketChannel=(ServerSocketChannel) selectionKey.channel();
        try {
            SocketChannel socketChannel=serverSocketChannel.accept() ;//一定会有一个连接
            // 设置非阻塞IO
            socketChannel.configureBlocking(false);
            socketChannel.write(ByteBuffer.wrap("Hello Client,I'm NIO Server".getBytes()));
            // 我发现客户端连接进来，那么我就可以去读客户端的数据了，我想读数据话，的先在selector注册一个读事件
            socketChannel.register(selector,SelectionKey.OP_READ); //注册读事件

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void handleRead(SelectionKey selectionKey){
        System.out.println(Thread.currentThread().getName());
        SocketChannel socketChannel=(SocketChannel)selectionKey.channel();
        ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
        try {
            int read = socketChannel.read(byteBuffer);//这里是不是一定有值
            if(read>0){
                System.out.println("server receive msg:"+new String(byteBuffer.array()));
            }else {
                System.out.println("server receive msg:没有数据");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
