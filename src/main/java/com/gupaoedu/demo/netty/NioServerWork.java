package com.gupaoedu.demo.netty;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

public class NioServerWork extends AbstractNioSelector implements Worker {

    private NioSelectorRunnablePool nioSelectorRunnablePool;

    public NioServerWork(Executor executor, String threadName, NioSelectorRunnablePool nioSelectorRunnablePool) {
        super(executor, threadName, nioSelectorRunnablePool);
    }

    @Override
    protected int select(Selector selector) throws IOException {
        return selector.select(500);
    }

    @Override
    protected void process(Selector selector) throws  Exception{
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        if (selectionKeys.isEmpty()){
            return;
        }
        Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
        while (iterator.hasNext()){
            SelectionKey key = iterator.next();
            iterator.remove();

            SocketChannel channel = (SocketChannel)key.channel();

            int red = 0;

            boolean failure = true;

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            try {
                red = channel.read(byteBuffer);
                failure = false;
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(red <=0 || failure){
                key.cancel();
                System.out.println("客户端连接断开");
            }else {
                System.out.println("收到数据" + new String(byteBuffer.array()));

                ByteBuffer wrap = ByteBuffer.wrap("收到\n".getBytes());
                channel.write(wrap);
            }

        }
    }

    @Override
    public void registerNewChannelTask(final SocketChannel socketChannel) {
        final  Selector selector = this.selector;

        registerTask(new Runnable(){

            @Override
            public void run() {
                try {
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
