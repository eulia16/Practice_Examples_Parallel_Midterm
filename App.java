package org.example;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App 
{

    public static final int numThreads = Runtime.getRuntime().availableProcessors();
    public static void main( String[] args )
    {
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











    }
}
