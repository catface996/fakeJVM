package com.fake.jvm.bytecode.code;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author by catface
 * @date 2021/7/1 4:00 下午
 */
@Slf4j
public class SynchronizedDemoTest {

    @Test
    public void setUseSynMethod() throws Exception {
        int size = 100;
        AtomicInteger id = new AtomicInteger(0);
        SynchronizedDemo demo = new SynchronizedDemo();
        CountDownLatch countDownLatch = new CountDownLatch(size);
        Thread[] threads = new Thread[size];
        for (int i = 0; i < size; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    demo.setUseSynMethod(id.incrementAndGet());
                }
                countDownLatch.countDown();
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        countDownLatch.await();
        int num = demo.getNum();
        int count = demo.getCount();
        log.info("num:{},count:{}", num, count);
    }

    @Test
    public void setUseSynBlock() throws Exception {
        int size = 100;
        AtomicInteger id = new AtomicInteger(0);
        SynchronizedDemo demo = new SynchronizedDemo();
        CountDownLatch countDownLatch = new CountDownLatch(size);
        Thread[] threads = new Thread[size];
        for (int i = 0; i < size; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    int num = id.incrementAndGet();
                    demo.setUseSynBlock(num);
                }
                countDownLatch.countDown();
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        countDownLatch.await();
        int num = demo.getNum();
        int count = demo.getCount();
        log.info("num:{},count:{}", num, count);
    }

    @Test
    public void setUseNothing() throws Exception {
        int size = 100;
        AtomicInteger id = new AtomicInteger(0);
        SynchronizedDemo demo = new SynchronizedDemo();
        CountDownLatch countDownLatch = new CountDownLatch(size);
        Thread[] threads = new Thread[size];
        for (int i = 0; i < size; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    int num = id.incrementAndGet();
                    demo.setUseNothing(num);
                }
                countDownLatch.countDown();
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        countDownLatch.await();
        int num = demo.getNum();
        int count = demo.getCount();
        log.info("num:{},count:{}", num, count);
    }
}