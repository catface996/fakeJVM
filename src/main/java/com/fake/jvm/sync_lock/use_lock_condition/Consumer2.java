package com.fake.jvm.sync_lock.use_lock_condition;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * @author by catface
 * @date 2021/6/30 3:52 下午
 */
@Slf4j
public class Consumer2 implements Runnable {

    @Override
    public void run() {
        for (; ; ) {
            try {
                TimeUnit.SECONDS.sleep(2);
                MyQ2.consume();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
