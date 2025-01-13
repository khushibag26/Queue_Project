/*
 * Checkout.java
 * 
 * 
 * 
 * 
 */

// code team: Zainab Arain, Khushi Bagri, Aadrit Talukdar, Kristine Nguyen, Ryan Kumar
// created three different models


import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;

class Customer {
    int arriveTime;
    int endQueueTime;

    public Customer(int arriveTime, int endQueueTime) {
        this.arriveTime = arriveTime;
        this.endQueueTime = endQueueTime;
    }
}

class Queue<T> {

	// queue starts from the beginning to the last item
    
    ArrayList<T> items;

    public Queue() {
        this.items = new ArrayList<T>();
    }

    /*
     * If no customer in line, then the list comes out as empty.
     */
    public boolean isEmpty() {
        return (this.items.isEmpty());
    }

    /*
     * Within the queue, each item is added
     */
    public void enqueue(T item) {
        this.items.add(0, item);
    }

    /*
     * Remove the item at the head of the queue and return it.
     * If the queue is empty, throws an exception.
     */
    public T dequeue() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }
        return this.items.remove(this.size() - 1);
    }

    /*
     * Return the item at the head of the queue, but do not remove it.
     * If the queue is empty, throws an exception.
     */
    public T peek() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }
        return this.items.get(this.size() - 1);
    }

    /*
     * Returns the number of items in the queue.
     */
    public int size() {
        return this.items.size();
    }

    /*
     * Convert to string as an array from tail to head
     */
    public String toString() {

        if (!this.items.isEmpty()) {
            String arrString = this.items.toString();
            return "tail ->" + arrString + "-> head";
        } else {
            return "<<empty queue>>";
        }
    }
}

public class Checkout {
    public static final int SIMULATION_TIME = 2 * 60 * 60; // 2 hours in seconds
    public static final int MAX_ENDQUEUE_TIME = 300; // Maximum service time in seconds (5 minutes)
    public static final int CUSTOMER_ARRIVE_RATE = 10; // On average, a new customer every 10 seconds

    public static void main(String[] args) {
        int numberOfCheckouts = 3;
        simulateCheckoutSystem(numberOfCheckouts);
    }

    public static void simulateCheckoutSystem(int numberOfCheckouts) {
        Queue<Customer>[] checkouts = new Queue[numberOfCheckouts];
        for (int i = 0; i < numberOfCheckouts; i++) {
            checkouts[i] = new Queue<>();
        }

        int customersServed = 0;
        int totalWaitTime = 0;
        int maxQueueLength = 0;

        Random random = new Random();
        for (int currentTime = 0; currentTime < SIMULATION_TIME; currentTime++) {
            if (random.nextInt(CUSTOMER_ARRIVE_RATE) == 0) {
                int minQueueIndex = 0;
                int minQueueSize = checkouts[0].size();
                for (int i = 1; i < numberOfCheckouts; i++) {
                    if (checkouts[i].size() < minQueueSize) {
                        minQueueIndex = i;
                        minQueueSize = checkouts[i].size();
                    }
                }
                Customer newCustomer = new Customer(currentTime, random.nextInt(MAX_ENDQUEUE_TIME) + 1);
                checkouts[minQueueIndex].enqueue(newCustomer);
                maxQueueLength = Math.max(maxQueueLength, minQueueSize + 1);
            }

            for (int i = 0; i < numberOfCheckouts; i++) {
                if (!checkouts[i].isEmpty()) {
                    Customer frontCustomer = checkouts[i].peek();
                    if (currentTime - frontCustomer.arriveTime >= frontCustomer.endQueueTime) {
                        checkouts[i].dequeue();
                        customersServed++;
                        totalWaitTime += (currentTime - frontCustomer.arriveTime);
                    }
                }
            }
        }

        double averageWaitTime = customersServed == 0 ? 0 : (double) totalWaitTime / customersServed;

        System.out.println("Number of customers served: " + customersServed);
        System.out.println("Maximum queue length: " + maxQueueLength);
        System.out.println("Average customer waiting time: " + averageWaitTime + " seconds");
    }
}
