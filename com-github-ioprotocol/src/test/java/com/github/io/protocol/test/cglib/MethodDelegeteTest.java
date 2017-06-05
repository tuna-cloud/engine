package com.github.io.protocol.test.cglib;

import net.sf.cglib.reflect.MethodDelegate;

import java.util.Arrays;

/**
 * Created by xsy on 2017/3/20.
 */
public class MethodDelegeteTest {
    public static interface TestDele {
        void test(String[] args);
    }

    public static class TestObj {
        public void test(String[] args) {
            System.out.println(Arrays.toString(args));
        }

        public void test1(String[] args) {
//            System.out.println(Arrays.toString(args));
//            System.out.println(Arrays.toString(args));
        }
    }

    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();
        TestObj obj = new TestObj();
        for(int i = 0; i < 10*10000; i++) {
            TestDele dele = (TestDele) MethodDelegate.create(obj, "test1", TestDele.class);
            dele.test(new String[]{"123", "456"});
        }
        long t2 = System.currentTimeMillis();
        byte b = (byte) 132;
        System.out.println(b);
        int x = b;
        System.out.println(x);
        System.out.println(x&0xFF);
    }
}
