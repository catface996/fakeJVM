package com.fake.jvm.sync_lock.use_lock_condition;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

/**
 * @author by catface
 * @date 2021/6/30 3:32 下午
 */
@Slf4j
public class MyQ2 {

    private static final Lock lock = new ReentrantLock(true);
    private static final Condition writeCondition = lock.newCondition();
    private static final Condition readCondition = lock.newCondition();
    private static final LinkedList<Integer> queue = new LinkedList<>();
    private static final AtomicInteger id = new AtomicInteger(0);
    private static final AtomicInteger messageId = new AtomicInteger(0);
    private static int count = 0;

    public MyQ2() {

    }

    public static void publish() {
        int opId = id.incrementAndGet();
        for (; ; ) {
            try {
                lock.lock();
                if (count < 5) {
                    count++;
                    queue.addLast(messageId.incrementAndGet());
                    log.info("opId:{},publish num is:{},count after push is:{}", opId, queue.peekLast(), count);
                    readCondition.signalAll();
                    return;
                } else {
                    log.info("opId:{},full queue,wait...", opId);
                    writeCondition.await();
                    log.info("opId:{},be notified,continue..", opId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static int consume() {
        int opId = id.incrementAndGet();
        for (; ; ) {
            try {
                lock.lock();
                if (count <= 0) {
                    try {
                        log.info("opId:{},empty queue,wait...", opId);
                        readCondition.await();
                        log.info("opId:{},be notified,continue...", opId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    count--;
                    int num = queue.pollFirst();
                    log.info("opId:{},consumer num is:{},after consumer count is:{}", opId, num, count);
                    writeCondition.signalAll();
                    return num;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }
    }

    public static class TestClass {

        public static void main(String[] args) throws Exception {
            int publisherNum = 4;
            int consumerNum = 2;
            Thread[] pThreads = new Thread[publisherNum];
            for (int i = 0; i < publisherNum; i++) {
                pThreads[i] = new Thread(new Publisher2());
                pThreads[i].setName("publisher-" + i);
            }
            Thread[] cThreads = new Thread[consumerNum];
            for (int i = 0; i < consumerNum; i++) {
                cThreads[i] = new Thread(new Consumer2());
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

