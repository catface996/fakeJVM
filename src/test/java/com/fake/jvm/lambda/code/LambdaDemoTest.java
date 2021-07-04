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