/**
 * ============================================================
 * MAIN CLASS - UseCase6RoomAllocationService
 * ============================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Description:
 * This use case processes booking requests and:
 * - Allocates rooms safely
 * - Prevents duplicate room assignment
 * - Updates inventory immediately
 *
 * Concepts:
 * - Queue (FIFO request handling)
 * - HashMap (inventory + allocation mapping)
 * - Set (unique room IDs)
 *
 * @author Developer
 * @version 6.0
 */

import java.util.*;

// ------------------------------------------------------------
// Reservation Class
// ------------------------------------------------------------
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// ------------------------------------------------------------
// Booking Queue (FIFO)
// ------------------------------------------------------------
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // removes from queue
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ------------------------------------------------------------
// Inventory Service
// ------------------------------------------------------------
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrement(String type) {
        int current = inventory.getOrDefault(type, 0);
        if (current > 0) {
            inventory.put(type, current - 1);
        }
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " -> " + inventory.get(type));
        }
    }
}

// ------------------------------------------------------------
// Booking Service (Allocation Logic)
// ------------------------------------------------------------
class BookingService {

    private RoomInventory inventory;

    // Track allocated rooms
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type -> allocated IDs
    private Map<String, Set<String>> allocationMap = new HashMap<>();

    private int idCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation r) {

        String type = r.getRoomType();

        System.out.println("\nProcessing request for " + r.getGuestName());

        // Check availability
        if (inventory.getAvailability(type) <= 0) {
            System.out.println("Booking FAILED: No rooms available for " + type);
            return;
        }

        // Generate unique room ID
        String roomId = type.replace(" ", "").substring(0, 2).toUpperCase() + idCounter++;

        // Ensure uniqueness (extra safety)
        while (allocatedRoomIds.contains(roomId)) {
            roomId = type.substring(0, 2).toUpperCase() + idCounter++;
        }

        // Allocate room
        allocatedRoomIds.add(roomId);

        allocationMap.putIfAbsent(type, new HashSet<>());
        allocationMap.get(type).add(roomId);

        // Update inventory immediately
        inventory.decrement(type);

        // Confirm reservation
        System.out.println("Booking CONFIRMED for " + r.getGuestName());
        System.out.println("Room Type: " + type);
        System.out.println("Allocated Room ID: " + roomId);
    }

    public void displayAllocations() {
        System.out.println("\n===== Allocated Rooms =====");

        for (String type : allocationMap.keySet()) {
            System.out.println(type + " -> " + allocationMap.get(type));
        }
    }
}

// ------------------------------------------------------------
// Main Class
// ------------------------------------------------------------
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();
        BookingService service = new BookingService(inventory);

        // Add booking requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        queue.addRequest(new Reservation("David", "Double Room"));

        // Process queue (FIFO)
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            service.processBooking(r);
        }

        // Display results
        service.displayAllocations();
        inventory.displayInventory();

        System.out.println("\nAll bookings processed. No double-booking occurred.");
    }
}