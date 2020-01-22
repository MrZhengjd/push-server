package com.zheng.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public abstract class MessageCache<T> {

    private static AtomicLong messageCount = new AtomicLong(0);
    private ConcurrentLinkedQueue<T> cache = new ConcurrentLinkedQueue<>();
    private Semaphore semaphore = new Semaphore(0);
    public void appednMessage(T id){
        cache.add(id);
        log.info("insert message to queue count "+messageCount.getAndIncrement());
        semaphore.release();
    }
    protected abstract void parallelDispatch(LinkedList<T> list);
    public void commit(ConcurrentLinkedQueue<T> tasks){
        commitMessage(tasks);
    }
    public void commit(){
        commitMessage(cache);
    }

    protected void commitMessage(ConcurrentLinkedQueue<T> cache){
        LinkedList<T> linkedList = new LinkedList<>();
        linkedList.addAll(cache);
        cache.clear();
        if (linkedList != null && linkedList.size()>0){
            parallelDispatch(linkedList);
            linkedList.clear();
        }
    }
    public boolean hold(long timeout){
        try {
            return semaphore.tryAcquire(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
           log.error("error catch on "+e);
           return false;
        }
    }
    protected Pair<Integer,Integer> calculateBlocks(int parallel,int sizeOfTask){
        int numberOfThreads = parallel > sizeOfTask ? sizeOfTask : parallel;
        Pair<Integer,Integer> pair = new MutablePair<>(new Integer(sizeOfTask/parallel),new Integer(numberOfThreads));
        return pair;
    }
}
