package com.fake.jvm.bytecode.code;

/**
 * @author by catface
 * @date 2021/7/1 3:07 下午
 */
public class VolatileDemo {

    private volatile static VolatileDemo VOLATILE_INSTANCE;
    private static VolatileDemo UN_VOLATILE_INSTANCE;

    public static VolatileDemo getVolatileInstance() {
        if (VOLATILE_INSTANCE == null) {
            synchronized (VolatileDemo.class) {
                if (VOLATILE_INSTANCE == null) {
                    VOLATILE_INSTANCE = new VolatileDemo();
                }
            }
        }
        return VOLATILE_INSTANCE;
    }

    public static VolatileDemo getUnVolatileInstance() {
        if (UN_VOLATILE_INSTANCE == null) {
            synchronized (VolatileDemo.class) {
                if (UN_VOLATILE_INSTANCE == null) {
                    UN_VOLATILE_INSTANCE = new VolatileDemo();
                }
            }
        }
        return UN_VOLATILE_INSTANCE;
    }
}
