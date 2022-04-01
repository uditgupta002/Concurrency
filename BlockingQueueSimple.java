import java.util.concurrent.*;

class HelloWorld {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new BlockingQueue(10);
        Thread enqueueThread = new Thread(new Runnable() {
           public void run() {
               try {
                   for(int j = 0; j < 11; j++) {
                       System.out.println("Enqueued : " + j);
                       queue.enqueue(j);
                   }
               } catch (InterruptedException e) {}
           } 
        });
        Thread dequeueThread = new Thread(new Runnable() {
           public void run() {
               try{
                   for(int j = 0; j < 10; j++) {
                       System.out.println("Dequeued : " + queue.dequeue());
                   }
               } catch (InterruptedException e) {}
           } 
        });
        
            
        try{
            enqueueThread.start();
            dequeueThread.start();
            
            enqueueThread.join();
            dequeueThread.join();
        } catch(InterruptedException e) {}
        
        System.out.println("Final Queue Size : " + queue.getSize());
    }
}

class BlockingQueue<T> {
    
    T[] store;
    int size;
    int capacity;
    int front;
    int rear;
    
    BlockingQueue(int capacity) {
        this.capacity = capacity;
        size = 0;
        store = (T[]) new Object[capacity];
        front = 0;
        rear = 0;
    }
    
    public synchronized void enqueue(T value) throws InterruptedException {
        
        // Check if store is full
        while(size == capacity) {
            wait();
        }

        store[rear] = value;
        rear = (rear + 1) % capacity;
        size += 1;
        notifyAll();
    }
    
    public synchronized T dequeue() throws InterruptedException {
        
        T returnValue = null;
        
        // Check if store is empty
        while(size == 0) wait();
        
        returnValue = store[front];
        store[front] = null;
        
        front = (front + 1) % capacity;
        
        size -= 1;
        notifyAll();
        
        return returnValue;
    }
    
    public synchronized int getSize() {
        return this.size;
    }
}
