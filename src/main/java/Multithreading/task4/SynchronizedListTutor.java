package Multithreading.task4;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

/**
 * 1) Попробуйте запустить программу. Почему программа (периодически) падает
 * с ArrayIndexOutOfBoundException? Что надо сделать, чтобы этого не происходило?
 * 2) Теперь попробуйте уменьшить количество циклов в run() до 10 и
 * добавить вывод на печать print() после добавления нового элемента.
 * Почему происходит ConcurrentModificationException?
 * Что сделать, чтобы этого не происходило?
 */
public class SynchronizedListTutor {
    static String[] langs =
            {"SQL", "PHP", "XML", "Java", "Scala",
                    "Python", "JavaScript", "ActionScript", "Clojure", "Groovy",
                    "Ruby", "C++"};

//    final List<String> randomLangs = Collections.synchronizedList(new ArrayList<String>());
//    final List<String> randomLangs = new CopyOnWriteArrayList<>();
//    final Queue<String> randomLangs = new LinkedBlockingQueue<>();
    final Queue<String> randomLangs = new ConcurrentLinkedQueue<>();

    public String getRandomLangs() {
        int index = (int) (Math.random() * langs.length);
        return langs[index];
    }

    class TestThread implements Runnable {
        String threadName;

        public TestThread(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                randomLangs.add(getRandomLangs());
//                synchronized (randomLangs) {
                    print(randomLangs);
//                }
            }
        }
    }

    public void print(Collection<?> c) {
        StringBuilder builder = new StringBuilder();
        Iterator<?> iterator = c.iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next());
            builder.append(" ");
        }
        System.out.println(builder.toString());
    }

    @Test
    public void testThread() {
        long time = -System.currentTimeMillis();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            threads.add(new Thread(new TestThread("t" + i)));
        }
        System.out.println("Starting threads");
        for (int i = 0; i < 100; i++) {
            threads.get(i).start();
        }
        System.out.println("Waiting for threads");
        try {
            for (int i = 0; i < 100; i++) {
                threads.get(i).join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        time += System.currentTimeMillis();
        System.out.println("time:\t" + time);
    }
}
