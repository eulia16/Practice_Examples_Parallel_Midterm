package org.example;

import java.util.concurrent.locks.ReentrantLock;

public class OrderingLocks {
    private final ReentrantLock lock1 = new ReentrantLock();
    private final ReentrantLock lock2 = new ReentrantLock();
    private static int count=0;

    //using two locks here for incrementing a variable is superrrr unuseful but it gives an
    //example of lock ordering

    public  void increment(){
        lock1.lock();
        System.out.println("Lock 1 has been aquired");
        lock2.lock();
        System.out.println("Lock 2 has been aquired");

        try {
            System.out.println("We are now in the critical section/have aquired both locks, we have full access to the state" +
                    " w/ no interuption");
            count++;
        }
        finally{
            //unlock in the reverse order you locked them in
            lock2.unlock();
            lock1.unlock();
        }

    }

    public void decrement(){
        lock1.lock();
        System.out.println("Lock 1 has been aquired");
        lock2.lock();
        System.out.println("Lock 2 has been aquired");

        try {
            System.out.println("We are now in the critical section/have aquired both locks, we have full access to the state" +
                    " w/ no interuption");
            count--;
        }
        finally{
            //unlock in the reverse order you locked them in
            lock2.unlock();
            lock1.unlock();
        }

    }

    public int getCount(){
        lock1.lock();
        System.out.println("Lock 1 has been aquired");
        lock2.lock();
        System.out.println("Lock 2 has been aquired");

        try {
            System.out.println("We are now in the critical section/have aquired both locks, we have full access to the state" +
                    " w/ no interuption");
            return count;
        }
        finally{
            //unlock in the reverse order you locked them in
            lock2.unlock();
            lock1.unlock();
        }


    }




}
