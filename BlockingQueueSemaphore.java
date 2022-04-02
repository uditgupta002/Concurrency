class DriverProgram {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new BlockingQueue(10);
        Thread enqueueThread = new Thread(new Runnable() {
           public void run() {
               try {
                   for(int j = 0; j < 111; j++) {
                       System.out.println("Enqueued : " + j);
                       queue.enqueue(j);
                   }
               } catch (InterruptedException e) {}
           } 
        });
        Thread dequeueThread = new Thread(new Runnable() {
           public void run() {
               try{
                   for(int j = 0; j < 11; j++) {
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
            System.out.println("Final Queue Size : " + queue.getSize());

        } catch(InterruptedException e) {}
        
        
    }
}

@SuppressWarnings("Unchecked Type")
class BlockingQueue<T> {
    
    T[] store;
    int size;
    int capacity;
    int front;
    int rear;
    
    Semaphore mutex;
    Semaphore producer;
    Semaphore consumer;
    
    BlockingQueue(int capacity) {
        this.capacity = capacity;
        size = 0;
        store = (T[]) new Object[capacity];
        front = 0;
        rear = 0;
        mutex = new Semaphore(1);
        producer = new Semaphore(capacity);
        consumer = new Semaphore(0);
    }
    
    public void enqueue(T value) throws InterruptedException {
        
        producer.acquire();
        mutex.acquire();

        store[rear] = value;
        rear = (rear + 1) % capacity;
        size += 1;
        
        mutex.release();
        consumer.release();
    }
    
    public T dequeue() throws InterruptedException {
        
        T returnValue = null;
        
        consumer.acquire();
        mutex.acquire();
        returnValue = store[front];
        store[front] = null;
        
        front = (front + 1) % capacity;
        
        size -= 1;
        mutex.release();
        producer.release();
        
        return returnValue;
    }
    
    public int getSize() throws InterruptedException {
        int value = -1;
        mutex.acquire();
        value = this.size;
        mutex.release();

        return value;
    }
}
