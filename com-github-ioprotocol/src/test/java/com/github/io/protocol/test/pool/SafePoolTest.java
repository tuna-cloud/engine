package com.github.io.protocol.test.pool;

import com.github.io.protocol.protocolpool.IProtocolPool;
import com.github.io.protocol.protocolpool.SafeProtocolPool;
import com.github.io.protocol.protocolpool.UnsafeProtocolPool;
import org.junit.Test;

public class SafePoolTest {

    public void test() throws InterruptedException {
        IProtocolPool pool = new UnsafeProtocolPool();
//        IProtocolPool pool = new SafeProtocolPool();
        new ThWrite(pool).start();
        new ThRead(pool).start();
        Thread.sleep(100000);
    }

    public static class ThWrite extends Thread {
        private IProtocolPool pool;

        public ThWrite(IProtocolPool protocolPool) {
            pool = protocolPool;
        }

        @Override
        public void run() {
            try {
                PoolBean bean = (PoolBean) pool.getObject(PoolBean.class);
                Thread.sleep(1200);
                bean.setAge(1);
                System.out.println(bean.toString());
                Thread.sleep(1200);
                bean.setAge(2);
                System.out.println(bean.toString());
                Thread.sleep(1200);
                bean.setAge(3);
                System.out.println(bean.toString());
                Thread.sleep(1200);
                bean.setAge(4);
                System.out.println(bean.toString());
                Thread.sleep(1200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class ThRead extends Thread {
        private IProtocolPool pool;

        public ThRead(IProtocolPool protocolPool) {
            pool = protocolPool;
        }

        @Override
        public void run() {
            try {
                PoolBean bean = (PoolBean) pool.getObject(PoolBean.class);
                while (true) {
                    System.out.println(bean.toString());
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
