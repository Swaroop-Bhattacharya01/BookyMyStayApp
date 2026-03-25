import java.util.*;

// Represents an optional add-on service
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

// Manages mapping between reservation and services
class AddOnServiceManager {
    private Map<String, List<AddOnService>> reservationServicesMap;

    public AddOnServiceManager() {
        reservationServicesMap = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {
        reservationServicesMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    // Get all services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost of add-ons
    public double calculateTotalCost(String reservationId) {
        List<AddOnService> services = getServices(reservationId);
        double total = 0.0;
        for (AddOnService service : services) {
            total += service.getCost();
        }
        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Add-On Services for Reservation " + reservationId + ":");
        for (AddOnService service : services) {
            System.out.println("- " + service);
        }

        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
    }
}

// Main class
public class UseCase7AddOnServiceSelection {
    public static void main(String[] args) {

        // Simulated reservation ID (already created in previous use case)
        String reservationId = "RES123";

        // Create service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Create add-on services
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 1200);
        AddOnService extraBed = new AddOnService("Extra Bed", 800);

        // Guest selects services
        manager.addService(reservationId, breakfast);
        manager.addService(reservationId, airportPickup);
        manager.addService(reservationId, extraBed);

        // Display selected services and cost
        manager.displayServices(reservationId);

        // Core booking remains unchanged (simulated message)
        System.out.println("\nCore booking and inventory remain unchanged.");
    }
}