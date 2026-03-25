import java.util.*;

// Custom Exception for invalid booking scenarios
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Represents a simple inventory system
class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Standard", 2);
        rooms.put("Deluxe", 2);
        rooms.put("Suite", 1);
    }

    public boolean isValidRoomType(String roomType) {
        return rooms.containsKey(roomType);
    }

    public int getAvailableRooms(String roomType) {
        return rooms.getOrDefault(roomType, 0);
    }

    public void bookRoom(String roomType) throws InvalidBookingException {
        if (!isValidRoomType(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        int available = rooms.get(roomType);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }

        // Guard against negative inventory
        rooms.put(roomType, available - 1);
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + rooms);
    }
}

// Validator class (Fail-Fast)
class InvalidBookingValidator {

    public static void validate(String guestName, String roomType, RoomInventory inventory)
            throws InvalidBookingException {

        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (roomType == null || roomType.trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty.");
        }

        if (!inventory.isValidRoomType(roomType)) {
            throw new InvalidBookingException("Room type does not exist: " + roomType);
        }

        if (inventory.getAvailableRooms(roomType) <= 0) {
            throw new InvalidBookingException("Selected room type is fully booked.");
        }
    }
}

// Booking service
class BookingService {

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void createBooking(String guestName, String roomType) {
        try {
            // Step 1: Validate input (Fail Fast)
            InvalidBookingValidator.validate(guestName, roomType, inventory);

            // Step 2: Process booking
            inventory.bookRoom(roomType);

            System.out.println("Booking successful for " + guestName + " [" + roomType + "]");

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking failed: " + e.getMessage());
        }
    }
}

// Main class
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        inventory.displayInventory();

        System.out.println("\n--- Booking Attempts ---");

        // Valid booking
        bookingService.createBooking("Alice", "Deluxe");

        // Invalid room type
        bookingService.createBooking("Bob", "Luxury");

        // Empty guest name
        bookingService.createBooking("", "Standard");

        // Exhaust inventory
        bookingService.createBooking("Charlie", "Suite");
        bookingService.createBooking("David", "Suite"); // Should fail

        System.out.println("\n--- Final Inventory ---");
        inventory.displayInventory();
    }
}