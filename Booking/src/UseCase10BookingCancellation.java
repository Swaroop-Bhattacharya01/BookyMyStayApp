import java.util.*;

// Custom Exception
class CancellationException extends Exception {
    public CancellationException(String message) {
        super(message);
    }
}

// Reservation model
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }

    public String getReservationId() { return reservationId; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }
    public boolean isCancelled() { return isCancelled; }

    public void cancel() {
        this.isCancelled = true;
    }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType + " | Room: " + roomId +
                (isCancelled ? " | CANCELLED" : " | ACTIVE");
    }
}

// Inventory system
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("Inventory: " + inventory);
    }
}

// Booking history
class BookingHistory {
    private Map<String, Reservation> reservations = new HashMap<>();

    public void addReservation(Reservation r) {
        reservations.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return reservations.get(id);
    }

    public void displayAll() {
        for (Reservation r : reservations.values()) {
            System.out.println(r);
        }
    }
}

// Cancellation Service with rollback
class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;

    // Stack for rollback tracking (LIFO)
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String reservationId) {
        try {
            // Step 1: Validate
            Reservation reservation = history.getReservation(reservationId);

            if (reservation == null) {
                throw new CancellationException("Reservation does not exist.");
            }

            if (reservation.isCancelled()) {
                throw new CancellationException("Reservation already cancelled.");
            }

            // Step 2: Record for rollback
            rollbackStack.push(reservation.getRoomId());

            // Step 3: Restore inventory
            inventory.increment(reservation.getRoomType());

            // Step 4: Mark reservation as cancelled
            reservation.cancel();

            System.out.println("Cancellation successful for: " + reservationId);

        } catch (CancellationException e) {
            System.out.println("Cancellation failed: " + e.getMessage());
        }
    }

    public void showRollbackStack() {
        System.out.println("Rollback Stack (recent releases): " + rollbackStack);
    }
}

// Main class
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("RES201", "Alice", "Deluxe", "D1");
        Reservation r2 = new Reservation("RES202", "Bob", "Suite", "S1");

        history.addReservation(r1);
        history.addReservation(r2);

        CancellationService service = new CancellationService(inventory, history);

        System.out.println("--- Initial State ---");
        inventory.displayInventory();
        history.displayAll();

        System.out.println("\n--- Cancellation Attempts ---");

        // Valid cancellation
        service.cancelBooking("RES201");

        // Invalid (already cancelled)
        service.cancelBooking("RES201");

        // Invalid (non-existent)
        service.cancelBooking("RES999");

        System.out.println("\n--- Final State ---");
        inventory.displayInventory();
        history.displayAll();

        System.out.println();
        service.showRollbackStack();
    }
}