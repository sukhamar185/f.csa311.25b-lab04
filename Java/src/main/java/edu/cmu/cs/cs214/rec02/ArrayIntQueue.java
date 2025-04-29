package edu.cmu.cs.cs214.rec02;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A resizable-array implementation of the {@link IntQueue} interface. The head of
 * the queue starts out at the head of the array, allowing the queue to grow and
 * shrink in constant time.
 */
public class ArrayIntQueue implements IntQueue {

    private static final Logger logger = LogManager.getLogger(ArrayIntQueue.class);

    private int[] elementData;
    private int head;
    private int size;
    private static final int INITIAL_SIZE = 10;

    public ArrayIntQueue() {
        elementData = new int[INITIAL_SIZE];
        head = 0;
        size = 0;
        logger.debug("ArrayIntQueue created with initial size {}", INITIAL_SIZE);
    }

    @Override
    public void clear() {
        Arrays.fill(elementData, 0);
        size = 0;
        head = 0;
        logger.info("Queue cleared");
    }

    @Override
    public Integer dequeue() {
        if (isEmpty()) {
            logger.warn("Attempt to dequeue from empty queue");
            return null;
        }
        Integer value = elementData[head];
        head = (head + 1) % elementData.length;
        size--;
        logger.debug("Dequeued value: {}, new head: {}, new size: {}", value, head, size);
        return value;
    }

    @Override
    public boolean enqueue(Integer value) {
        ensureCapacity();
        int tail = (head + size) % elementData.length;
        elementData[tail] = value;
        size++;
        logger.debug("Enqueued value: {}, at index: {}, new size: {}", value, tail, size);
        return true;
    }

    @Override
    public boolean isEmpty() {
        boolean empty = size == 0;
        logger.trace("Queue isEmpty check: {}", empty);
        return empty;
    }

    @Override
    public Integer peek() {
        if (isEmpty()) {
            logger.warn("Attempt to peek from empty queue");
            return null;
        }
        logger.debug("Peeked value: {}", elementData[head]);
        return elementData[head];
    }

    @Override
    public int size() {
        logger.trace("Queue size requested: {}", size);
        return size;
    }

    private void ensureCapacity() {
        if (size == elementData.length) {
            int oldCapacity = elementData.length;
            int newCapacity = 2 * oldCapacity + 1;
            int[] newData = new int[newCapacity];
            for (int i = 0; i < size; i++) {
                newData[i] = elementData[(head + i) % oldCapacity];
            }
            elementData = newData;
            head = 0;
            logger.info("Queue capacity increased from {} to {}", oldCapacity, newCapacity);
        }
    }
}
