package com.fake.jvm.锁._2lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author by catface
 * @date 2021/7/2 5:32 下午
 */
@Slf4j
public class LockDemoTest {

    @Test
    public void setNum() throws Exception {
        int size = 10;
        AtomicInteger id = new AtomicInteger();
        CountDownLatch countDownLatch = new CountDownLatch(size);
        LockDemo lockDemo = new LockDemo();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < size; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    int num = id.incrementAndGet();
                    lockDemo.setNum(num);
                }
                countDownLatch.countDown();
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        countDownLatch.await();
        int num = lockDemo.getNum();
        int count = lockDemo.getCount();
        log.info("num:{},count:{}", num, count);
    }

    @Test
    public void setNumUseLock() throws Exception {
        int size = 10;
        AtomicInteger id = new AtomicInteger(0);
        CountDownLatch countDownLatch = new CountDownLatch(size);
        LockDemo lockDemo = new LockDemo();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < size; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    int num = id.incrementAndGet();
                    lockDemo.setNumUseLock(num);
                }
                countDownLatch.countDown();
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        countDownLatch.await();
        int num = lockDemo.getNum();
        int count = lockDemo.getCount();
        log.info("num:{},count:{}", num, count);
    }
}