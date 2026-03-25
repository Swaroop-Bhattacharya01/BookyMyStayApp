import java.io.*;
import java.util.*;

// Reservation class (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

// Inventory class (Serializable)
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);
    }

    public void bookRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("Inventory: " + inventory);
    }
}

// Wrapper class to persist full system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    List<Reservation> reservations;
    RoomInventory inventory;

    public SystemState(List<Reservation> reservations, RoomInventory inventory) {
        this.reservations = reservations;
        this.inventory = inventory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state
    public static void save(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load state
    public static SystemState load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println("System state loaded successfully.");
            return (SystemState) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No previous state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state. Starting with safe defaults.");
        }
        return null;
    }
}

// Main class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        List<Reservation> reservations;
        RoomInventory inventory;

        // Step 1: Load existing state
        SystemState state = PersistenceService.load();

        if (state != null) {
            reservations = state.reservations;
            inventory = state.inventory;
        } else {
            reservations = new ArrayList<>();
            inventory = new RoomInventory();
        }

        System.out.println("\n--- Current System State ---");
        inventory.displayInventory();
        System.out.println("Reservations: " + reservations);

        // Step 2: Simulate new booking
        Reservation r1 = new Reservation("RES301", "Alice", "Deluxe");
        reservations.add(r1);
        inventory.bookRoom("Deluxe");

        System.out.println("\n--- After New Booking ---");
        inventory.displayInventory();
        System.out.println("Reservations: " + reservations);

        // Step 3: Save state before shutdown
        SystemState newState = new SystemState(reservations, inventory);
        PersistenceService.save(newState);

        System.out.println("\n--- Restart the application to see recovery ---");
    }
}