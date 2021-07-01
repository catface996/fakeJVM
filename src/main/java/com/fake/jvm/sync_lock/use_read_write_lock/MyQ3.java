package com.fake.jvm.sync_lock.use_read_write_lock;

import java.util.concurrent.TimeUnit;

import com.dp.发布订阅.use_lock_condition.Consumer2;

/**
 * @author by catface
 * @date 2021/6/30 5:39 下午
 */
public class MyQ3 {

    public static void consumer() {

    }

    public static void publish() {

    }

    public static class TestClass {

        public static void main(String[] args) throws Exception {
            int publisherNum = 4;
            int consumerNum = 2;
            Thread[] pThreads = new Thread[publisherNum];
            for (int i = 0; i < publisherNum; i++) {
                pThreads[i] = new Thread(new Publisher3());
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
