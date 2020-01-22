package com.zheng.executor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MyExecutor {

    private final static int THREAD_POOL_THREADS;

    private final static int THREAD_POOL_QUEUES;

    static {
        THREAD_POOL_THREADS = Runtime.getRuntime().availableProcessors() * 2;
        THREAD_POOL_QUEUES = 512;

    }

    private final static ExecutorService THREAD_POOL = new ThreadPoolExecutor(THREAD_POOL_THREADS, THREAD_POOL_THREADS,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(THREAD_POOL_QUEUES), new ThreadFactory() {
        private AtomicInteger threadIndex = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "handlerThread_" + this.threadIndex.incrementAndGet());
        }
    },new ThreadPoolExecutor.DiscardPolicy());

    public static ExecutorService getThreadPool(){
        return THREAD_POOL;
    }
    public static void submit(Runnable task){
        THREAD_POOL.submit(task);
    }
    public static void execute(Runnable task){
        THREAD_POOL.execute(task);
    }
    public static void execute(Callable task){
        THREAD_POOL.submit(task);
    }
    public static Object submit(Callable task){
       return THREAD_POOL.submit(task);
    }
}
