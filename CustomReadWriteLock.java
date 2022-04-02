public class CustomReadWriteLock {

    public static void main(String a[]) {
        ReadWriteLock lock = new ReadWriteLock(2);
        Thread readerThread = new Thread(new Runnable() {
            public void run() {
                try {
                    for(int j = 0; j < 2; j++) {
                        lock.acquireRead();
                    }
                } catch (InterruptedException e) {}
            } 
        });
        readerThread.setName("ReaderThread1");

        Thread readerThread2 = new Thread(new Runnable() {
            public void run() {
                try {
                    for(int j = 0; j < 2; j++) {
                        lock.acquireRead();
                    }
                } catch (InterruptedException e) {}
            } 
        });
        readerThread2.setName("ReaderThread2");

        Thread writerThread1 = new Thread(new Runnable() {
            public void run() {
                try {
                    lock.acquireWrite();
                } catch (InterruptedException e) {}
            } 
        });
        writerThread1.setName("WriterThread1");

        Thread writerThread2 = new Thread(new Runnable() {
            public void run() {
                try {
                    lock.acquireWrite();
                } catch (InterruptedException e) {}
            }
        });
        writerThread2.setName("WriterThread2");

        Thread releaseReaderThread = new Thread(new Runnable() {
            public void run() {
                try{
                    for(int j = 0; j < 4; j++) {
                        lock.releaseRead();
                    }
                } catch (InterruptedException e) {}
            } 
        });
        releaseReaderThread.setName("ReleaseReaderThread");
        
        Thread releaseWriterThread = new Thread(new Runnable() {
            public void run() {
                try{
                    for(int j = 0; j < 2; j++) {
                        lock.releaseWrite();
                    }
                } catch (InterruptedException e) {}
            } 
        });
        releaseWriterThread.setName("RelaseWriterThread");
             
        try{
            readerThread.start();
            readerThread2.start();
            writerThread1.start();
            writerThread2.start();

            releaseReaderThread.start();
            releaseWriterThread.start();

            readerThread.join();
            readerThread2.join();
            writerThread1.join();
            writerThread2.join();

            releaseReaderThread.join();
            releaseWriterThread.join();
        } catch(InterruptedException e) {}
    }
}

class ReadWriteLock {


    int readerCount;
    boolean writerActive;
    int maxReadersAllowed;

    public ReadWriteLock(int maxReadPermits) {
        readerCount = 0;
        writerActive = false;
        maxReadersAllowed = maxReadPermits;
    }

    public synchronized void acquireRead() throws InterruptedException {
        while(readerCount == maxReadersAllowed || writerActive) wait();
        System.out.println("Read lock acquired by " + Thread.currentThread().getName());
        readerCount += 1;

        // Notifying any threads waiting on releasing reader threads
        notifyAll();
    }

    public synchronized void acquireWrite() throws InterruptedException {
        
        while(writerActive || readerCount > 0) wait();
        System.out.println("Write lock acquired by " + Thread.currentThread().getName());
        writerActive = true;
        
        // Notifying any threads waiting on releasing writer threads
        notifyAll();
    }

    public synchronized void releaseRead() throws InterruptedException {

        while(readerCount == 0) wait();
        System.out.println("Read lock released by " + Thread.currentThread().getName());
        readerCount -= 1;
        notifyAll();
    }

    public synchronized void releaseWrite() throws InterruptedException {

        while(!writerActive) wait();
        System.out.println("Write lock released by " + Thread.currentThread().getName());
        writerActive = false;
        notifyAll();
    }
}
