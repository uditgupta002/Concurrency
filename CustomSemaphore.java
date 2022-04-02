public class CustomSemaphore {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1);
        Thread acquireThread = new Thread(new Runnable() {
           public void run() {
               try {
                   for(int j = 0; j < 2; j++) {
                       semaphore.acquire();
                       
                   }
               } catch (InterruptedException e) {}
           } 
        });
        Thread acquireThread2 = new Thread(new Runnable() {
            public void run() {
                try {
                    for(int j = 0; j < 2; j++) {
                        semaphore.acquire();
                    }
                } catch (InterruptedException e) {}
            } 
         });

        Thread releaseThread = new Thread(new Runnable() {
           public void run() {
               try{
                   for(int j = 0; j < 4; j++) {
                       semaphore.release();
                       
                   }
               } catch (InterruptedException e) {}
           } 
        });
        
            
        try{
            acquireThread.start();
            acquireThread2.start();
            releaseThread.start();
            
            acquireThread.join();
            acquireThread2.join();
            releaseThread.join();
        } catch(InterruptedException e) {}
    }

}

class Semaphore {
    
    int maxPermits;
    int usedPermits;

    public Semaphore(int maxPermits) {
        this.maxPermits = maxPermits;
        usedPermits = 0;
    }

    public synchronized void acquire() throws InterruptedException {
        
        // Check if permits are available
        while(usedPermits == maxPermits) wait();
        System.out.println("Acquired : " + Thread.currentThread().getName());
        usedPermits += 1;
        notifyAll();
    }

    public synchronized void release() throws InterruptedException {

        // Check if all permits are already released
        while(usedPermits == 0) wait();
        System.out.println("Released : " + Thread.currentThread().getName());
        usedPermits -= 1;
        notifyAll();
    }

}
