package com.fake.jvm.锁.发布订阅.use_synchronize;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

/**
 * @author by catface
 * @date 2021/6/30 3:32 下午
 */
@Slf4j
public class MyQ {

    private static final Object lock = new Object();
    private static final LinkedList<Integer> queue = new LinkedList<>();
    private static final AtomicInteger id = new AtomicInteger(0);
    private static int count = 0;

    public static void publish(int num) {
        for (; ; ) {
            synchronized (lock) {
                int opId = id.incrementAndGet();
                if (count < 5) {
                    count++;
                    queue.addLast(num);
                    log.info("count after push is:{}", count);
                    lock.notifyAll();
                    // 换成 lock.notify(); 试试
                    return;
                } else {
                    try {
                        log.info("opId:{},full queue,wait...", opId);
                        lock.wait();
                        log.info("opId:{},be notified,continue..", opId);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public static int consume() {
        for (; ; ) {
            synchronized (lock) {
                int opId = id.incrementAndGet();
                if (count <= 0) {
                    try {
                        log.info("opId:{},empty queue,wait...", opId);
                        lock.wait();
                        log.info("opId:{},be notified,continue...", opId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    count--;
                    int num = queue.pollFirst();
                    lock.notifyAll();
                    // 换成 lock.notify(); 试试
                    return num;
                }
            }
        }
    }

    public static class TestClass {

        public static void main(String[] args) throws Exception {
            int publisherNum = 20;
            int consumerNum = 1;
            Thread[] pThreads = new Thread[publisherNum];
            for (int i = 0; i < publisherNum; i++) {
                pThreads[i] = new Thread(new Publisher());
                pThreads[i].setName("publisher-" + i);
            }
            Thread[] cThreads = new Thread[consumerNum];
            for (int i = 0; i < consumerNum; i++) {
                cThreads[i] = new Thread(new Consumer());
                cThreads[i].setName("consumer-" + i);
            }
            for (int i = 0; i < consumerNum; i++) {
                cThreads[i].start();
            }
            for (int i = 0; i < publisherNum; i++) {
                pThreads[i].start();
            }
            TimeUnit.HOURS.sleep(1);
        }

    }

}
