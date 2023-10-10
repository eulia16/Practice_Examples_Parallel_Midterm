package org.example;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class AtomicCounter {
    private AtomicInteger count = new AtomicInteger(0);

    public void increment(){
        this.count.getAndIncrement();//we can throw away the value in this function
    }
    public int incrementAndReceive(){
        return this.count.getAndIncrement();
    }
    public AtomicInteger getCount(){return this.count;}

    //some decrement methods as well
    public void decrement(){
        this.count.decrementAndGet();
    }

    public int decrementAndReceive(){
        return this.count.decrementAndGet();
    }

    //now without using an atomicInteger is a tad harder and wastes quite a few cycles(we'll implement one
    // that will spin/wait on a lock while its waiting to incremeent)

    private int count2 =0;
    //i know java locks are reentrant, but i like the look of this better tgan lock = new reentrant()...
    private final ReentrantLock lock = new ReentrantLock();

    public void incrementWithLock(){
        this.lock.lock();//lock the bitch
        try{
            count2++;
        }
        finally{
            this.lock.unlock();
        }
    }
    public void decrementWithLock(){
        this.lock.lock();//lock the bitch
        try{
            count2--;
        }
        finally{
            this.lock.unlock();
        }
    }

    public int getCount2(){
        this.lock.lock();
        try{
            return this.count2;
        }
        finally{
            this.lock.unlock();
        }
    }


}
