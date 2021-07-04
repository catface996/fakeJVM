# Lambda 表达式的实现机制

lambda 表达式是通过在编译期间,生成静态方法来实现,LambdaDemoTest.java 文件的源码如下:

~~~java
package com.fake.jvm.lambda.code;

import org.junit.Test;

/**
 * @author by catface
 * @date 2021/7/4 12:23 下午
 */
public class LambdaDemoTest {

    @Test
    public void sayHello() {
        LambdaDemo lambdaDemo = new LambdaDemo();
        String helloMessage = lambdaDemo.useHello((name) -> {
            return "hello," + name;
        }, "大大");
        System.out.println(helloMessage);
    }
}
~~~

字节码解析出的方法如下:

![image-20210704123504082](https://tva1.sinaimg.cn/large/008i3skNly1gs4t7vr6ngj314w0f40v5.jpg)

可以发现,java源码中,只有**syHello**一个方法,解析字节码分析得出,默认的构造方法**init**除外,还有一个**lambda$sayHello$0**,并且是 private static synthetic,
lambda$sayHello$0的java虚拟机指令序列如下:

~~~shell
 0 new #9 <java/lang/StringBuilder>
 3 dup
 4 invokespecial #10 <java/lang/StringBuilder.<init>>
 7 ldc #11 <hello,>
 9 invokevirtual #12 <java/lang/StringBuilder.append>
12 aload_0
13 invokevirtual #12 <java/lang/StringBuilder.append>
16 invokevirtual #13 <java/lang/StringBuilder.toString>
19 areturn
~~~



