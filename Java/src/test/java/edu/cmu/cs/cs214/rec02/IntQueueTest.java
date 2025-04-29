package edu.cmu.cs.cs214.rec02;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

public class IntQueueTest {

    private static final Logger logger = LogManager.getLogger(IntQueueTest.class);

    private IntQueue mQueue;
    private List<Integer> testList;

    @Before
    public void setUp() {
        mQueue = new ArrayIntQueue();
        testList = new ArrayList<>(List.of(1, 2, 3));
        logger.info("Test setup complete with testList: {}", testList);
    }

    @Test
    public void testIsEmpty() {
        logger.debug("Running testIsEmpty");
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testNotEmpty() {
        logger.debug("Running testNotEmpty");
        mQueue.enqueue(10);
        assertFalse(mQueue.isEmpty());
    }

    @Test
    public void testPeekEmptyQueue() {
        logger.debug("Running testPeekEmptyQueue");
        assertNull(mQueue.peek());
    }

    @Test
    public void testPeekNoEmptyQueue() {
        logger.debug("Running testPeekNoEmptyQueue");
        mQueue.enqueue(99);
        assertEquals((Integer) 99, mQueue.peek());
        assertEquals(1, mQueue.size());
    }

    @Test
    public void testEnqueue() {
        logger.debug("Running testEnqueue");
        for (int i = 0; i < testList.size(); i++) {
            mQueue.enqueue(testList.get(i));
            assertEquals(testList.get(0), mQueue.peek());
            assertEquals(i + 1, mQueue.size());
        }
    }

    @Test
    public void testDequeue() {
        logger.debug("Running testDequeue");
        mQueue.enqueue(5);
        mQueue.enqueue(6);
        assertEquals((Integer) 5, mQueue.dequeue());
        assertEquals((Integer) 6, mQueue.peek());
        assertEquals(1, mQueue.size());
    }

    @Test
    public void testClear() {
        logger.debug("Running testClear");
        mQueue.enqueue(1);
        mQueue.enqueue(2);
        mQueue.clear();
        assertTrue(mQueue.isEmpty());
        assertNull(mQueue.peek());
        assertEquals(0, mQueue.size());
    }

    @Test
    public void testWrapAround() {
        logger.debug("Running testWrapAround");
        for (int i = 0; i < 10; i++) {
            mQueue.enqueue(i);
        }
        for (int i = 0; i < 5; i++) {
            assertEquals((Integer) i, mQueue.dequeue());
        }
        for (int i = 10; i < 15; i++) {
            mQueue.enqueue(i);
        }
        for (int i = 5; i < 15; i++) {
            assertEquals((Integer) i, mQueue.dequeue());
        }
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testEnsureCapacity() {
        logger.debug("Running testEnsureCapacity");
        for (int i = 0; i < 25; i++) {
            mQueue.enqueue(i);
        }
        for (int i = 0; i < 25; i++) {
            assertEquals((Integer) i, mQueue.dequeue());
        }
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testContent() {
        logger.debug("Running testContent");
        InputStream in = null;
        try {
            in = new FileInputStream("src/test/resources/data.txt");
            try (Scanner scanner = new Scanner(in)) {
                scanner.useDelimiter("\\s*fish\\s*");

                List<Integer> correctResult = new ArrayList<>();
                while (scanner.hasNextInt()) {
                    int input = scanner.nextInt();
                    correctResult.add(input);
                    mQueue.enqueue(input);
                }

                for (Integer result : correctResult) {
                    assertEquals(result, mQueue.dequeue());
                }
            }
        } catch (IOException e) {
            logger.error("Failed to read input file for testContent", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("Failed to close InputStream", e);
                }
            }
        }
    }
}
