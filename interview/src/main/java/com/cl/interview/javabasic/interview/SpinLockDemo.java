package com.cl.interview.javabasic.interview;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author chenLei
 * @date 2020/6/14 16:13
 */
public class SpinLockDemo {
    // 原子引用
    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public void myLock() {
        // 获取当前线程
        Thread thread = Thread.currentThread();
        System.out.println(Thread.currentThread().getName() + "\t come in");
        // 比较并交换
        while (!atomicReference.compareAndSet(null, thread)) {

        }

    }

    public void myUnLock() {
        // 获取当前线程
        Thread thread = Thread.currentThread();
        System.out.println(Thread.currentThread().getName() + "\t go out");
        // 比较并交换
        atomicReference.compareAndSet(thread, null);
    }

    public static void main(String[] args) {
        // 获取操作类对象
        SpinLockDemo demo = new SpinLockDemo();

        // 第一个线程
        new Thread(()->{
            demo.myLock();
            demo.myUnLock();
        },"t1").start();

        // 第二个线程
        new Thread(()->{
            demo.myLock();
            demo.myUnLock();
        },"t2").start();
    }
}
