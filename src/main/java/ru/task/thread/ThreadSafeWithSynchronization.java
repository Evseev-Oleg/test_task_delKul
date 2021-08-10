package ru.task.thread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


/**
 * Producer Consumer
 */
public class ThreadSafeWithSynchronization {
    private final Queue<Integer> randomsQueue = new LinkedList<>();


    public void STARThreadProcess() throws InterruptedException {
//        producer1.start();
//        consumer.start();
//        producer1.join();
//        consumer.join();


        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }

    /**
     * size limit
     */
    private final Thread producer = new Thread(() -> {
        while (true) {
            synchronized (this) {
                int LIMIT = 10;
                while (randomsQueue.size() == LIMIT) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Integer n = new Random().nextInt(100);
                randomsQueue.offer(n);
                System.out.println("wrote");
                this.notify();
            }
        }
    });

    /**
     * size random
     */
    private final Thread producer1 = new Thread(() -> {
        while (true) {
            int i = 0;
            int size = new Random().nextInt(100);
            synchronized (this) {
                for (; i < size; ) {
                    Integer n = new Random().nextInt(100);
                    randomsQueue.offer(n);
                    System.out.println(n);
                    System.out.println("wrote");
                    System.out.println(randomsQueue.size());
                    i++;
                    try {
                        this.notify();
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    });


    // Поток для чтения данных. Читает данные из списка Randoms и выводит их в консоль
    private final Thread consumer = new Thread(() -> {
        while (true) {
            synchronized (this) {
                while (randomsQueue.size() == 0) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(randomsQueue.poll());
                System.out.println(randomsQueue.size());
                System.out.println("read");
                this.notify();
            }

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    public static void main(String[] args) throws InterruptedException {
        ThreadSafeWithSynchronization threadSafeWithSynchronization = new ThreadSafeWithSynchronization();
        threadSafeWithSynchronization.STARThreadProcess();
    }
}



