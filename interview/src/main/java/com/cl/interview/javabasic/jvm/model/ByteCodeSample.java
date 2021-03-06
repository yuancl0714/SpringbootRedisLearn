package com.cl.interview.javabasic.jvm.model;

import java.util.concurrent.TimeUnit;

/**
 * @author chenLei
 * @date 2020/4/18 15:48
 */
public class ByteCodeSample {
    public static void main(String[] args) {

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

class MyData {
    volatile int number = 0;

    public void addTo60() {
        this.number = 60;
    }
}
