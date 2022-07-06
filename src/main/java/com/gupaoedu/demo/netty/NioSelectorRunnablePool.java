package com.gupaoedu.demo.netty;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

public class NioSelectorRunnablePool {

    private AtomicInteger bossIndex = new AtomicInteger();
    private Boss[] bosses;

    private AtomicInteger workIndex = new AtomicInteger();
    private Worker[] workers;

    NioSelectorRunnablePool(Executor boss, Executor work) {
        initBoss(boss, 1);

        initWorker(work, Runtime.getRuntime().availableProcessors() * 2);
    }

    /**
     * 初始化worker线程
     * @param work
     * @param count
     */
    private void initWorker(Executor work, int count) {
        this.workers = new NioServerWork[count];

        for (int i = 0; i < workers.length; i++) {
            workers[i] = new NioServerWork(work,"work thread "+(i+1),this);
        }
    }
    /**
     * 初始化boss线程
     * @param boss
     * @param count
     */
    private void initBoss(Executor boss, int count) {
        this.bosses = new NioServerBoss[count];

        for (int i = 0; i < bosses.length; i++) {
            bosses[i] = new NioServerBoss(boss, "boss thread" + (i + 1), this);
        }
    }

    /**
     * 获取下一个worker
     * @return
     */
    public Worker nextWorker(){
        return workers[Math.abs(workIndex.getAndIncrement() % workers.length)];
    }

    public Boss nextBoss(){
        return bosses[Math.abs(bossIndex.getAndIncrement() % bosses.length)];
    }
}
