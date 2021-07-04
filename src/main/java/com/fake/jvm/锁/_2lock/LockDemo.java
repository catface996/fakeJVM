package com.fake.jvm.锁._2lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author by catface
 * @date 2021/7/2 5:23 下午
 */
public class LockDemo {

    private ReentrantLock REENTRANT_LOCK = new ReentrantLock(true);
    private int num;
    private int count;

    public void LockDemo() {
        num = 0;
        count = 0;
    }

    public void setNumUseLock(int num) {
        REENTRANT_LOCK.lock();
        try {
            this.num = num;
            this.count++;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            REENTRANT_LOCK.unlock();
        }
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        this.count++;
    }

    public int getCount() {
        return count;
    }

}
