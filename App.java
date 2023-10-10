package org.example;

import java.util.Currency;
import java.util.List;
import java.util.concurrent.*;

/**
 * Hello world!
 *
 */
public class App 
{

    public static final int numThreads = Runtime.getRuntime().availableProcessors();
    public static void main( String[] args ) throws InterruptedException {
        System.out.println( "This will be some example problems as outlined in what could potentially be on the midterm " );
        //we will first do an atomic counter, then an example of using multiple threads w/ Executor and termination,
        //and then we will write an example about ordering locks, and then some shit w barriers(single use
        // only barriers and then phaser, but can also reference my project 1 for the phasers 'help'), and then use of
        //ConVars in a context other than resource management/semaphores

        //for the atomic counter and using mult threads, we'll intertwine them since itll show
        //atomicity for the counter and using executor/terminating
        AtomicCounter atomicCounter = new AtomicCounter();
        ExecutorService ex = Executors.newFixedThreadPool(numThreads);

        for(int i=0; i<numThreads; ++i){
            ex.execute(new Runnable() {
                @Override
                public void run() {
                    for(int i=0; i<10; ++i) {
                        atomicCounter.increment();
                        atomicCounter.incrementWithLock();
                    }

                }
            });
        }

        try{
            ex.awaitTermination(1, TimeUnit.SECONDS);
            System.out.println("count of atomic1: " + atomicCounter.getCount() + ", count of lock based: " + atomicCounter.getCount2());

        } catch (InterruptedException e) {
           List<Runnable> remaining =  ex.shutdownNow();
           System.out.println("runnables that didnt finish: " + remaining);
            throw new RuntimeException(e);
        }

        System.out.println("after");
        ex.shutdown();


        OrderingLocks o = new OrderingLocks();

        //this is jyst an example using runnables for practice, a more concise way of doing this would be something like |
        ExecutorService someOtherThreads = Executors.newFixedThreadPool(2);
        Runnable[] someIncrements = new Runnable[numThreads];
        Runnable[] someDecrements = new Runnable[numThreads];
        for(int i=0; i<someIncrements.length; ++i){
            someIncrements[i] = new Runnable() {
                @Override
                public void run() {
                    for(int i=0; i<10; ++i)
                    o.increment();
                }
            };
        }

        for(int i=0; i<someDecrements.length; ++i){
            someDecrements[i] = new Runnable() {
                @Override
                public void run() {
                    for(int i=0; i<10; ++i)
                        o.decrement();
                }
            };
        }

        for(Runnable r : someIncrements)
            someOtherThreads.execute(r);


        for(Runnable r : someDecrements)
            someOtherThreads.execute(r);



        someOtherThreads.awaitTermination(1, TimeUnit.SECONDS);


        System.out.println("value of ordered locks (should be 0): " + o.getCount());


        someOtherThreads.shutdown();


        //| more concise w/ countdown latch(COUNTS DOWN COUNTER NOT NUMBER OF THREADS, ONE THREAD CSN COUNTDOWN > 1)
        CountDownLatch countDownLatch = new CountDownLatch(7);
        ExecutorService es = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 100; i++) {
            es.execute(() -> {
                long prevValue = countDownLatch.getCount();
                countDownLatch.countDown();
                if (countDownLatch.getCount() != prevValue) {
                    outputScraper.add("Count Updated");
                }
            });
        }
        es.shutdown();

        //example w cyclic barrier(reusable)
        CyclicBarrier cyclicBarrier = new CyclicBarrier(numThreads);
        ExecutorService anotherOne = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < 100; i++) {
            es.execute(() -> {
                if(cyclicBarrier.getNumberWaiting() < 1)
                    System.out.println("No more peeps are waiting, count updated");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }

            });
        }
        anotherOne.shutdown();

        //now if we swapped the order of the locks, theres a good chance it would have deadlocked(i tested it later
        // it did indeed deadlock if you swap one of the methods locks to lock on lock 2 first ;))














    }
}
