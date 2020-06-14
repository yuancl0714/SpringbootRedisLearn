package com.cl.interview.javabasic.volatiel;

import java.util.concurrent.TimeUnit;

/**
 * @author chenLei
 * @date 2020/6/2 18:53
 */
class MyData {
    volatile int number = 0;

    public void addTo60() {
        this.number = 60;
    }

    public void add() {
        number++;
    }
}

public class VolatileDemo {
    public static void main(String[] args) {
        MyData data = new MyData();
        // 验证volatile不是原子性
        // 创建20的线程
        for (int i = 1; i <= 20; i++) {
            new Thread(() -> {
                for (int j = 1; j <= 1000; j++) {
                    data.add();
                }
            }, String.valueOf(i)).start();
        }

        // 让主线程等待到所有线程都计算完成之后在启动
        while (Thread.activeCount() > 2) {
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName() + ":" + data.number);
    }

    // volatile可见性的证明
    private static void seeOkByVolatile() {
        MyData data = new MyData();// 资源类
        // 创建线程
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "come in");
            // 线程停留三秒钟
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            data.addTo60();
            System.out.println(Thread.currentThread().getName() + "\t" + "update date" + data.number);
        }, "AAA").start();

        // 第二个线程是主线程
        while (data.number == 0) {

        }

        System.out.println(Thread.currentThread().getName() + "\t" + data.number);
    }
}


