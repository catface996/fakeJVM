package com.fake.jvm.sync_lock.use_read_write_lock;

import java.util.concurrent.TimeUnit;

/**
 * @author by catface
 * @date 2021/6/30 5:40 下午
 */
public class Publisher3 implements Runnable {

    @Override
    public void run() {
        for (; ; ) {
            try {
                TimeUnit.SECONDS.sleep(3);
                MyQ3.publish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
