# 字节码

## class文件中的内容

java文件

~~~java
package com.fake.jvm.bytecode;

/**
 * @author by catface
 * @date 2021/6/29 6:16 下午
 */
public class ByteCodeDemo {
}
~~~

class文件(16进制展示)

~~~shell
cafe babe 0000 0034 0010 0a00 0300 0d07
000e 0700 0f01 0006 3c69 6e69 743e 0100
0328 2956 0100 0443 6f64 6501 000f 4c69
6e65 4e75 6d62 6572 5461 626c 6501 0012
4c6f 6361 6c56 6172 6961 626c 6554 6162
6c65 0100 0474 6869 7301 0024 4c63 6f6d
2f66 616b 652f 6a76 6d2f 6279 7465 636f
6465 2f42 7974 6543 6f64 6544 656d 6f3b
0100 0a53 6f75 7263 6546 696c 6501 0011
4279 7465 436f 6465 4465 6d6f 2e6a 6176
610c 0004 0005 0100 2263 6f6d 2f66 616b
652f 6a76 6d2f 6279 7465 636f 6465 2f42
7974 6543 6f64 6544 656d 6f01 0010 6a61
7661 2f6c 616e 672f 4f62 6a65 6374 0021
0002 0003 0000 0000 0001 0001 0004 0005
0001 0006 0000 002f 0001 0001 0000 0005
2ab7 0001 b100 0000 0200 0700 0000 0600
0100 0000 0700 0800 0000 0c00 0100 0000
0500 0900 0a00 0000 0100 0b00 0000 0200
0c
~~~

字节码文件解释:

[cafe babe] class文件的固定开头.

[0000 0034]  0000 代表小版本号, 0034 代表主版本号

[0010] 代表常量池中常量的个数,16进制换算成10进制是16个,由于常量池常量编号是从1开始,所以0010代表15个

...

Intellij Idea Show ByteCode 插件查看字节码文件:

* 一般信息:

![image-20210701150454372](https://tva1.sinaimg.cn/large/008i3skNly1gs1govzayjj310o0ro0xh.jpg)

* 默认构造方法的代码:

![image-20210701150605622](https://tva1.sinaimg.cn/large/008i3skNly1gs1gq6dl3kj30yr0u0wir.jpg)

* 其他信息可自行查看..

## volatile在字节码层面的实现

~~~java
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
~~~

VOLATILE_INSTANCE;

![image-20210701154824673](https://tva1.sinaimg.cn/large/008i3skNly1gs1hz76g2uj312o0hadhw.jpg)

UN_VOLATILE_INSTANCE;

![image-20210701155000956](https://tva1.sinaimg.cn/large/008i3skNly1gs1hzqfiydj311m0a6dho.jpg)

getVolatileInstance 和 getUnVolatileInstance

~~~shell
## getVolatileInstance
 0 getstatic #2 <com/fake/jvm/bytecode/code/VolatileDemo.VOLATILE_INSTANCE>
 3 ifnonnull 37 (+34)
 6 ldc #3 <com/fake/jvm/bytecode/code/VolatileDemo>
 8 dup
 9 astore_0
10 monitorenter
11 getstatic #2 <com/fake/jvm/bytecode/code/VolatileDemo.VOLATILE_INSTANCE>
14 ifnonnull 27 (+13)
17 new #3 <com/fake/jvm/bytecode/code/VolatileDemo>
20 dup
21 invokespecial #4 <com/fake/jvm/bytecode/code/VolatileDemo.<init>>
24 putstatic #2 <com/fake/jvm/bytecode/code/VolatileDemo.VOLATILE_INSTANCE>
27 aload_0
28 monitorexit
29 goto 37 (+8)
32 astore_1
33 aload_0
34 monitorexit
35 aload_1
36 athrow
37 getstatic #2 <com/fake/jvm/bytecode/code/VolatileDemo.VOLATILE_INSTANCE>
40 areturn

## getUnVolatileInstance
 0 getstatic #5 <com/fake/jvm/bytecode/code/VolatileDemo.UN_VOLATILE_INSTANCE>
 3 ifnonnull 37 (+34)
 6 ldc #3 <com/fake/jvm/bytecode/code/VolatileDemo>
 8 dup
 9 astore_0
10 monitorenter
11 getstatic #5 <com/fake/jvm/bytecode/code/VolatileDemo.UN_VOLATILE_INSTANCE>
14 ifnonnull 27 (+13)
17 new #3 <com/fake/jvm/bytecode/code/VolatileDemo>
20 dup
21 invokespecial #4 <com/fake/jvm/bytecode/code/VolatileDemo.<init>>
24 putstatic #5 <com/fake/jvm/bytecode/code/VolatileDemo.UN_VOLATILE_INSTANCE>
27 aload_0
28 monitorexit
29 goto 37 (+8)
32 astore_1
33 aload_0
34 monitorexit
35 aload_1
36 athrow
37 getstatic #5 <com/fake/jvm/bytecode/code/VolatileDemo.UN_VOLATILE_INSTANCE>
40 areturn
~~~

volatile在字节码层面除了变量的访问标志符有差异,其他的并无不同.

