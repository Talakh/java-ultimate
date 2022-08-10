package org.course;

import lombok.SneakyThrows;

public class Deadlock {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    @SneakyThrows
    public static void main(String[] args) {
        var t1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName() + " in lock1, state: " + Thread.currentThread().getState());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName() + " in lock2, state: " + Thread.currentThread().getState());
                }
            }
        });
        var t2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + " in lock2, state: " + Thread.currentThread().getState());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (lock1) {
                    System.out.println(Thread.currentThread().getName() + " in lock1, state: " + Thread.currentThread().getState());
                }
            }
        });

        t1.start();
        System.out.println(t1.getName() + " " + t1.getState());
        t2.start();
        System.out.println(t2.getName() + " " + t2.getState());

        Thread.sleep(100);

        System.out.println(t1.getName() + " " + t1.getState());
        System.out.println(t2.getName() + " " + t2.getState());

        Thread.sleep(400);

        System.out.println(t1.getName() + " " + t1.getState());
        System.out.println(t2.getName() + " " + t2.getState());

        t1.join();
        t2.join();
    }
}
