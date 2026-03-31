package seedu.pharmatracker.customer;

import java.util.ArrayList;

/**
 * Represents a customer in the PharmaTracker system.
 * Holds personal details and a history of medications dispensed to them.
 */
public class Customer {
    private String customerId;
    private String name;
    private String phone;
    private String address; // Optional field
    private ArrayList<String> dispensingHistory;

    /**
     * Constructs a Customer with mandatory and optional details.
     *
     * @param customerId Unique identifier for the customer.
     * @param name       Full name of the customer.
     * @param phone      Contact phone number.
     * @param address    Residential address (can be empty if not provided).
     */
    public Customer(String customerId, String name, String phone, String address) {
        assert customerId != null : "Customer ID cannot be null";
        assert name != null : "Name cannot be null";
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.address = (address == null) ? "" : address;
        this.dispensingHistory = new ArrayList<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = (address == null) ? "" : address;
    }

    public ArrayList<String> getDispensingHistory() {
        return dispensingHistory;
    }

    /**
     * Adds a medication entry to the customer's dispensing history.
     * Called whenever a medication is dispensed to this customer.
     *
     * @param medicationRecord Description of the dispensed medication.
     */
    public void addDispensingHistory(String medicationRecord) {
        assert medicationRecord != null : "Medication record cannot be null";
        this.dispensingHistory.add(medicationRecord);
    }

    /**
     * Returns a short summary string for use in list and find displays.
     *
     * @return A formatted string showing customer ID, name, and phone.
     */
    @Override
    public String toString() {
        String result = "[" + customerId + "] " + name + " | Phone: " + phone;
        if (!address.isEmpty()) {
            result += " | Address: " + address;
        }
        return result;
    }
}
