package com.fake.jvm.bytecode.code;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * @author by catface
 * @date 2021/7/1 3:10 下午
 */
public class VolatileDemoTest {

    @Test
    public void getVolatileInstance() throws Exception {
        int size = 100;
        ConcurrentHashMap<VolatileDemo, VolatileDemo> map = new ConcurrentHashMap<>();
        Thread[] threads = new Thread[size];
        for (int i = 0; i < size; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    try {
                        VolatileDemo volatileDemo = VolatileDemo.getVolatileInstance();
                        map.putIfAbsent(volatileDemo, volatileDemo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        for (int i = 0; i < size; i++) {
            threads[i].start();
        }
        for (VolatileDemo volatileDemo : map.keySet()) {
            System.out.println(volatileDemo);
        }
        TimeUnit.HOURS.sleep(1);
    }

    @Test
    public void getUnVolatileInstance() throws Exception {
        int size = 100;
        ConcurrentHashMap<VolatileDemo, VolatileDemo> map = new ConcurrentHashMap<>();
        Thread[] threads = new Thread[size];
        for (int i = 0; i < size; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    try {
                        VolatileDemo volatileDemo = VolatileDemo.getUnVolatileInstance();
                        map.putIfAbsent(volatileDemo, volatileDemo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        for (int i = 0; i < size; i++) {
            threads[i].start();
        }
        for (VolatileDemo volatileDemo : map.keySet()) {
            System.out.println(volatileDemo);
        }
        TimeUnit.HOURS.sleep(1);
    }

}