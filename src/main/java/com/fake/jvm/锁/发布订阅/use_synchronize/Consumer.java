package com.fake.jvm.锁.发布订阅.use_synchronize;

import lombok.extern.slf4j.Slf4j;

/**
 * @author by catface
 * @date 2021/6/30 3:52 下午
 */
@Slf4j
public class Consumer implements Runnable {
    @Override
    public void run() {
        for (; ; ) {
            int num = MyQ.consume();
            log.info("consumer num:{}", num);
        }
    }
}
