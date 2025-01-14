/*
 * Customer.java
 * 
 * 
 * main program code made by everyone in the group
 * Zainab Arain, Khushi Bagri, Aadrit Talukdar, Kristine Nguyen, Ryan Kumar
 * 
 * 
 */


import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;

class Customer {
    int arriveTime;
    int items;
    int endQueueTime; // Additional property to track the time when customer will finish

    public Customer(int arriveTime, int items) {
        this.arriveTime = arriveTime;
        this.items = items;
        this.endQueueTime = 0; // Initialized to 0, will be updated when customer is served
    }
}

class Queue<T> {
    ArrayList<T> items;

    public Queue() {
        this.items = new ArrayList<>();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void enqueue(T item) {
        items.add(0, item);
    }

    public T dequeue() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }
        return items.remove(items.size() - 1);
    }

    public T peek() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }
        return items.get(items.size() - 1);
    }

    public int size() {
        return items.size();
    }

    public String toString() {
        if (!items.isEmpty()) {
            return "tail -> " + items.toString() + " -> head";
        } else {
            return "<<empty queue>>";
        }
    }
}

public class Checkout {
    public static final int SIMULATION_TIME = 2 * 60 * 60; // 2 hours in seconds
    public static final int CUSTOMER_ARRIVE_RATE = 30; // On average, a new customer every 30 seconds
    public static final int MIN_ITEMS = 1; // Minimum items a customer can have
    public static final int MAX_ITEMS = 50; // Maximum items a customer can have
    public static final int SCAN_TIME = 2; // Time taken to scan each item in seconds
    public static final int PAYMENT_TIME = 30; // Time taken for the customer to pay in seconds

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
        int totalItems = 0;
        int totalServiceTime = 0;
        int currentCustomerID = 1; // To track each customer uniquely

        Random random = new Random();
        for (int currentTime = 0; currentTime < SIMULATION_TIME; currentTime++) {
            // Customer arrival
            if (random.nextInt(CUSTOMER_ARRIVE_RATE) == 0) {
                int items = random.nextInt(MAX_ITEMS - MIN_ITEMS + 1) + MIN_ITEMS;
                Customer newCustomer = new Customer(currentTime, items);
                int minQueueIndex = 0;
                int minQueueSize = checkouts[0].size();
                for (int i = 1; i < numberOfCheckouts; i++) {
                    if (checkouts[i].size() < minQueueSize) {
                        minQueueIndex = i;
                        minQueueSize = checkouts[i].size();
                    }
                }
                checkouts[minQueueIndex].enqueue(newCustomer);
                maxQueueLength = Math.max(maxQueueLength, checkouts[minQueueIndex].size());
                System.out.println("Customer " + currentCustomerID++ + " arrived at time " + currentTime + " with " + items + " items.");
            }

            // Processing customers at checkout stations
            for (int i = 0; i < numberOfCheckouts; i++) {
                if (!checkouts[i].isEmpty()) {
                    Customer frontCustomer = checkouts[i].peek();
                    int checkoutTime = frontCustomer.items * SCAN_TIME + PAYMENT_TIME;
                    if (currentTime - frontCustomer.arriveTime >= checkoutTime) {
                        checkouts[i].dequeue();
                        customersServed++;
                        totalWaitTime += (currentTime - frontCustomer.arriveTime);
                        totalItems += frontCustomer.items;
                        totalServiceTime += checkoutTime;
                        System.out.println("Customer served at time " + currentTime + ". Wait time: " + (currentTime - frontCustomer.arriveTime) + " seconds.");
                    }
                }
            }
        }

        double averageWaitTime = customersServed == 0 ? 0 : (double) totalWaitTime / customersServed;
        double averageItems = customersServed == 0 ? 0 : (double) totalItems / customersServed;
        double averageServiceTime = customersServed == 0 ? 0 : (double) totalServiceTime / customersServed;

        System.out.println("Simulation complete.");
        System.out.println("Number of customers served: " + customersServed);
        System.out.println("Maximum queue length: " + maxQueueLength);
        System.out.println("Average customer waiting time: " + averageWaitTime + " seconds");
        System.out.println("Average number of items per customer: " + averageItems);
        System.out.println("Average service time per customer: " + averageServiceTime + " seconds");
    }
}


