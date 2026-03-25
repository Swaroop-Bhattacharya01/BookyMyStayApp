import java.util.*;

// Thread-safe Room Inventory
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);
    }

    // Synchronized method to ensure thread safety
    public synchronized boolean bookRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public synchronized void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// Booking Task (Represents a user)
class BookingTask implements Runnable {
    private String guestName;
    private String roomType;
    private RoomInventory inventory;

    public BookingTask(String guestName, String roomType, RoomInventory inventory) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        boolean success = inventory.bookRoom(roomType);

        if (success) {
            System.out.println(Thread.currentThread().getName() +
                    " - Booking SUCCESS for " + guestName + " [" + roomType + "]");
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " - Booking FAILED for " + guestName + " [" + roomType + "]");
        }
    }
}

// Main class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        System.out.println("--- Initial Inventory ---");
        inventory.displayInventory();

        System.out.println("\n--- Concurrent Booking Simulation ---");

        // Simulating multiple users (threads)
        Thread t1 = new Thread(new BookingTask("Alice", "Suite", inventory), "User-1");
        Thread t2 = new Thread(new BookingTask("Bob", "Suite", inventory), "User-2");
        Thread t3 = new Thread(new BookingTask("Charlie", "Deluxe", inventory), "User-3");
        Thread t4 = new Thread(new BookingTask("David", "Deluxe", inventory), "User-4");
        Thread t5 = new Thread(new BookingTask("Eve", "Deluxe", inventory), "User-5");

        // Start threads
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        // Wait for all threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e.getMessage());
        }

        System.out.println("\n--- Final Inventory ---");
        inventory.displayInventory();
    }
}