package com.fake.jvm.锁.发布订阅.use_read_write_lock;

import java.util.concurrent.TimeUnit;

/**
 * @author by catface
 * @date 2021/6/30 5:40 下午
 */
public class Consumer3 implements Runnable {
    @Override
    public void run() {
        for (; ; ) {
            try {
                TimeUnit.SECONDS.sleep(1);
                MyQ3.consumer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
