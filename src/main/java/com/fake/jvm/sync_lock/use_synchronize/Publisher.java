package com.fake.jvm.sync_lock.use_synchronize;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * @author by catface
 * @date 2021/6/30 3:52 下午
 */
@Slf4j
public class Publisher implements Runnable {
    @Override
    public void run() {
        for (; ; ) {
            try {
                TimeUnit.SECONDS.sleep(1);
                int num = (int)(Math.random() * 10);
                MyQ.publish(num);
                log.info("publish num:{}", num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
