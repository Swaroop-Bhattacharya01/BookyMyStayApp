/**
 * ============================================================
 * MAIN CLASS - UseCase4RoomSearch
 * ============================================================
 *
 * Use Case 4: Room Search & Availability Check
 *
 * Description:
 * This use case introduces a read-only search service that:
 * - Retrieves availability from inventory
 * - Displays only available rooms
 * - Uses room objects for details
 * - Does NOT modify system state
 *
 * @author Developer
 * @version 4.0
 */

import java.util.*;

// ------------------------------------------------------------
// Abstract Room Class (Domain Model)
// ------------------------------------------------------------
abstract class Room {
    private String roomType;
    private int beds;
    private double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getBeds() {
        return beds;
    }

    public double getPrice() {
        return price;
    }

    public abstract void displayDetails();
}

// ------------------------------------------------------------
// Concrete Room Types
// ------------------------------------------------------------
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 100.0);
    }

    public void displayDetails() {
        System.out.println("Room: " + getRoomType());
        System.out.println("Beds: " + getBeds());
        System.out.println("Price: $" + getPrice());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 180.0);
    }

    public void displayDetails() {
        System.out.println("Room: " + getRoomType());
        System.out.println("Beds: " + getBeds());
        System.out.println("Price: $" + getPrice());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 300.0);
    }

    public void displayDetails() {
        System.out.println("Room: " + getRoomType());
        System.out.println("Beds: " + getBeds());
        System.out.println("Price: $" + getPrice());
    }
}

// ------------------------------------------------------------
// Inventory Class (Read-Only Access Here)
// ------------------------------------------------------------
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // Example: unavailable
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Set<String> getRoomTypes() {
        return inventory.keySet();
    }
}

// ------------------------------------------------------------
// Search Service (READ-ONLY)
// ------------------------------------------------------------
class RoomSearchService {

    private RoomInventory inventory;
    private Map<String, Room> roomCatalog;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;

        // Room domain objects
        roomCatalog = new HashMap<>();
        roomCatalog.put("Single Room", new SingleRoom());
        roomCatalog.put("Double Room", new DoubleRoom());
        roomCatalog.put("Suite Room", new SuiteRoom());
    }

    public void searchAvailableRooms() {
        System.out.println("===== Available Rooms =====\n");

        for (String type : inventory.getRoomTypes()) {
            int available = inventory.getAvailability(type);

            // Filter unavailable rooms
            if (available > 0) {
                Room room = roomCatalog.get(type);

                room.displayDetails();
                System.out.println("Available: " + available);
                System.out.println("--------------------------------");
            }
        }
    }
}

// ------------------------------------------------------------
// Main Class
// ------------------------------------------------------------
public class UseCase4RoomSearch {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize search service
        RoomSearchService searchService = new RoomSearchService(inventory);

        // Perform search (READ-ONLY)
        searchService.searchAvailableRooms();

        System.out.println("\nSearch completed. System state unchanged.");
    }
}