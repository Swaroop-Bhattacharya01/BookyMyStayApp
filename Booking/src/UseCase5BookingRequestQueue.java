/**
 * ============================================================
 * MAIN CLASS - UseCase5BookingRequestQueue
 * ============================================================
 *
 * Use Case 5: Booking Request (First-Come-First-Served)
 *
 * Description:
 * This use case introduces a booking request queue to:
 * - Accept booking requests
 * - Store them in arrival order
 * - Ensure FIFO-based fair processing
 *
 * NOTE:
 * - No inventory updates are performed here
 * - Only request intake and ordering is handled
 *
 * @author Developer
 * @version 5.0
 */

import java.util.*;

// ------------------------------------------------------------
// Reservation Class (Represents a booking request)
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

    public void displayReservation() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// ------------------------------------------------------------
// Booking Request Queue (FIFO)
// ------------------------------------------------------------
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request to queue
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all queued requests
    public void displayQueue() {
        System.out.println("\n===== Booking Request Queue =====");

        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (Reservation r : queue) {
            r.displayReservation();
        }
    }

    // Get next request (without removing)
    public Reservation peekNext() {
        return queue.peek();
    }
}

// ------------------------------------------------------------
// Main Class
// ------------------------------------------------------------
public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulate booking requests
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");
        Reservation r3 = new Reservation("Charlie", "Suite Room");

        // Add requests to queue (FIFO order)
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Display queue
        bookingQueue.displayQueue();

        // Peek next request (FIFO check)
        System.out.println("\nNext request to process:");
        Reservation next = bookingQueue.peekNext();
        if (next != null) {
            next.displayReservation();
        }

        System.out.println("\nAll requests stored in arrival order. No inventory changes made.");
    }
}