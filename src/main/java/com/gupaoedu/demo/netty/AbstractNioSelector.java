package com.gupaoedu.demo.netty;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

public abstract class AbstractNioSelector implements Runnable {

    public NioSelectorRunnablePool nioSelectorRunnablePool;

    private final Executor executor;

    public Selector selector;

    public final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<Runnable>();

    public String threadName;

    public AbstractNioSelector(Executor executor, String threadName,NioSelectorRunnablePool nioSelectorRunnablePool) {
        this.executor = executor;
        this.selector = selector;
        this.threadName = threadName;
        this.nioSelectorRunnablePool = nioSelectorRunnablePool;
        openSelector();
    }

    protected  void openSelector(){
        try {
            this.selector = Selector.open();

        } catch (IOException e) {
            e.printStackTrace();
        }
        executor.execute(this);
    }

    @Override
    public void run() {
        Thread.currentThread().setName(this.threadName);

        while (true){
            try {
                select(selector);

                processTaskQueue();

                process(selector);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private void processTaskQueue() {
        for (;;){
            final  Runnable task = taskQueue.poll();
            if(task == null){
                break;
            }
            task.run();
        }
    }

    public void registerTask(Runnable task){
        taskQueue.add(task);

        Selector selector = this.selector;

        if(selector !=null){
            selector.wakeup();
        }else{
            taskQueue.remove(task);
        }
    }

    protected abstract int select(Selector selector) throws IOException;

    protected abstract void process(Selector selector) throws Exception;

}
