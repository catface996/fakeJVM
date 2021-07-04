package com.fake.jvm.lambda.code;

/**
 * @author by catface
 * @date 2021/7/4 12:19 下午
 */
public class LambdaDemo {

    public String useHello(Demo demo, String name) {
        return "你好," + demo.hello(name);
    }

}
