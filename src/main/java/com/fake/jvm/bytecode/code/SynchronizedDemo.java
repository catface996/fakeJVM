package com.fake.jvm.bytecode.code;

import lombok.extern.slf4j.Slf4j;

/**
 * @author by catface
 * @date 2021/7/1 3:52 下午
 */
@Slf4j
public class SynchronizedDemo {

    private static final Object LOCK = new Object();
    private int num;
    private int count = 0;

    public void setUseNothing(int num) {
        this.num = num;
        count++;
    }

    public synchronized void setUseSynMethod(int num) {
        this.num = num;
        count++;
    }

    public void setUseSynBlock(int num) {
        synchronized (LOCK) {
            this.num = num;
            count++;
        }
    }

    public int getNum() {
        return num;
    }

    public int getCount() {
        return count;
    }

}
