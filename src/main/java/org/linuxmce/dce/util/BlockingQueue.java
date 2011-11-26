package org.linuxmce.dce.util;

/**
 * A simple blocking queue with a timeout.
 * <p/>
 * This is a re-implementation suitable for a j2me environment.
 */
public class BlockingQueue {
    private Object[] theArray;
    private int currentSize;
    private int front;
    private int back;

    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Construct the queue.
     */
    public BlockingQueue() {
        theArray = new Object[DEFAULT_CAPACITY];
        makeEmpty();
    }

    /**
     * Test if the queue is logically empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return currentSize == 0;
    }

    /**
     * Make the queue logically empty.
     */
    public void makeEmpty() {
        currentSize = 0;
        front = 0;
        back = -1;
    }

    public Object dequeue() {
        return dequeue(-1);
    }
    /**
     * Return and remove the least recently inserted item
     * from the queue.
     *
     * @return the least recently inserted item in the queue.
     */
    public Object dequeue(int timeout) {
        try {
            synchronized (theArray) {
                if (isEmpty()) {
                    //block until we get one
                    if (timeout > 0) {
                        theArray.wait(timeout);
                    } else {
                        theArray.wait();
                    }
                }
                if (isEmpty()) {
                    //We have woken up with nothing in the queue
                    //Probably a timeout.
                    return null;
                }
                currentSize--;

                Object returnValue = theArray[front];
                front = increment(front);
                return returnValue;
            }
        } catch (InterruptedException ex) {
            //hmm, what do we think of this?
            throw new RuntimeException(ex);
        }
    }

    /**
     * Insert a new item into the queue.
     *
     * @param x the item to insert.
     */
    public void enqueue(Object x) {
        if (currentSize == theArray.length)
            doubleQueue();
        back = increment(back);
        theArray[back] = x;
        currentSize++;
    }

    /**
     * Internal method to increment with wraparound.
     *
     * @param x any index in theArray's range.
     * @return x+1, or 0 if x is at the end of theArray.
     */
    private int increment(int x) {
        if (++x == theArray.length)
            x = 0;
        return x;
    }

    /**
     * Internal method to expand theArray.
     */
    private void doubleQueue() {
        Object[] newArray;

        newArray = new Object[theArray.length * 2];

        // Copy elements that are logically in the queue
        for (int i = 0; i < currentSize; i++, front = increment(front))
            newArray[i] = theArray[front];

        theArray = newArray;
        front = 0;
        back = currentSize - 1;
    }


}
