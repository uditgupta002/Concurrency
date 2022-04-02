import java.util.LinkedList;
import java.util.List;

public class CustomBarrier {
    
    public static void main(String a[]) throws InterruptedException {
        Barrier barrier = new Barrier(3);
        List<Thread> barrierThreads = new LinkedList<>();
        
        for(int i = 0; i < 6; i++) {
            barrierThreads.add(new Thread(new Runnable() {
                public void run() {
                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }
        
        try {
            for(Thread barrierThread: barrierThreads) {
                barrierThread.start();
                
            }

            for(Thread barrierThread: barrierThreads) barrierThread.join();

            System.out.println("Completed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

class Barrier {

    private int currentCount = 0;
    private int maxCount;
    private int released;

    public Barrier(int maxCount) {
        this.maxCount = maxCount;
    }

    public synchronized void await() throws InterruptedException {

        while(currentCount == maxCount) {
            System.out.println("Next round thread waiting at barrier : " + Thread.currentThread().getName());
            wait();
        }
        

        currentCount += 1;
        System.out.println("Another thread arrived at barrier : " + Thread.currentThread().getName());
        if(currentCount == maxCount) {
            System.out.println("Barrier Released...." + Thread.currentThread().getName());
            released = currentCount;
            notifyAll();

        } else {
            while(currentCount != maxCount) wait();
        }

        released -= 1;
        System.out.println("Barrier released: Another thread passing from barrier : " + Thread.currentThread().getName());
        if(released == 0) {
            System.out.println("Resetting count");
            currentCount = 0;
            notifyAll();
        }
    }
}