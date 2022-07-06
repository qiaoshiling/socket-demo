package com.gupaoedu.demo.netty;

import java.io.IOException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

public class NioServerBoss extends AbstractNioSelector implements Boss {

    public NioServerBoss(Executor executor, String threadName, NioSelectorRunnablePool nioSelectorRunnablePool) {

        super(executor, threadName, nioSelectorRunnablePool);
    }

    @Override
    protected int select(Selector selector) throws IOException {
        return selector.select();
    }

    @Override
    protected void process(Selector selector) throws Exception{

        Set<SelectionKey> selectionKeys = selector.selectedKeys();

        if (selectionKeys.isEmpty()) {
            return;
        }
        for (Iterator<SelectionKey> iterator = selectionKeys.iterator();iterator.hasNext();){
            SelectionKey key = iterator.next();
            iterator.remove();

            ServerSocketChannel serverSocketChannel =(ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            Worker worker = nioSelectorRunnablePool.nextWorker();
            worker.registerNewChannelTask(socketChannel);
            System.out.println("客户端连接");
        }

    }

    @Override
    public void registerAcceptChannelTask(final ServerSocketChannel serverSocketChannel) {
        final Selector selector = this.selector;

        registerTask(new Runnable() {

            @Override
            public void run() {
                try {
                    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
