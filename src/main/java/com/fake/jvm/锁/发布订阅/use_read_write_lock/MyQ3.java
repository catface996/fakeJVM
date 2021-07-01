package com.fake.jvm.锁.发布订阅.use_read_write_lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lombok.extern.slf4j.Slf4j;

/**
 * @author by catface
 * @date 2021/6/30 5:39 下午
 */
@Slf4j
public class MyQ3 {

    // TODO ReentrantReadWriteLock.readLock.newCondition()不可以使用

    private static final ReentrantReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();
    private static final Lock READ_LOCK = READ_WRITE_LOCK.readLock();
    private static final Lock WRITE_LOCK = READ_WRITE_LOCK.writeLock();
    private static final AtomicInteger MESSAGE_GEN = new AtomicInteger(0);
    private static int CACHE = 0;

    public static void consumer() {
        READ_LOCK.lock();
        try {
            log.info("read cache is:{}", CACHE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            READ_LOCK.unlock();
        }
    }

    public static void publish() {
        WRITE_LOCK.lock();
        try {
            CACHE = MESSAGE_GEN.incrementAndGet();
            log.info("write cache is: {}", CACHE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            WRITE_LOCK.unlock();
        }
    }

    public static class TestClass {

        public static void main(String[] args) throws Exception {
            int publisherNum = 2;
            int consumerNum = 4;
            Thread[] pThreads = new Thread[publisherNum];
            for (int i = 0; i < publisherNum; i++) {
                pThreads[i] = new Thread(new Publisher3());
                pThreads[i].setName("publisher-" + i);
            }
            Thread[] cThreads = new Thread[consumerNum];
            for (int i = 0; i < consumerNum; i++) {
                cThreads[i] = new Thread(new Consumer3());
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
