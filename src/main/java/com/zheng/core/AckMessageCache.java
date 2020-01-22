package com.zheng.core;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class AckMessageCache extends MessageCache<String> {

    private CyclicBarrier barrier = null;
    private long successTaskCount = 0 ;

    private AckMessageCache() {
    }

    public long getSuccessTaskCount() {
        return successTaskCount;
    }

    @Override
    protected void parallelDispatch(LinkedList<String> list) {
        List<Callable<Long>> tasks = new ArrayList<>();
        List<Future<Long>> futureList = new ArrayList<>();
        int startPosition = 0;
        Pair<Integer,Integer> pair = calculateBlocks(list.size(),list.size());
        int numberOfThreads = pair.getRight();
        int blocks = pair.getLeft();
        barrier = new CyclicBarrier(numberOfThreads);
        for (int i = 0;i<numberOfThreads ;i++){
            String[] task = new String[blocks];
            System.arraycopy(list.toArray(),startPosition,task,0,blocks);
            tasks.add(new AckMessageTask(barrier,task));
            startPosition += blocks;
        }
        System.out.println("cyclic barrier " + numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        try {
            futureList = executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            log.error("error catch on "+e);
        }
        for (Future<Long> longFuture : futureList){
            try {
                successTaskCount += longFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    public static class AckMessageCacheHolder{
        public static AckMessageCache instance = new AckMessageCache();
    }
    public static AckMessageCache getInstance(){
        return AckMessageCacheHolder.instance;
    }
}
