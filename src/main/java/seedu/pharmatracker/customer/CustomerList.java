package seedu.pharmatracker.customer;

import java.util.ArrayList;

/**
 * Manages a list of customers.
 * Provides methods for adding, deleting, and searching for customers.
 */
public class CustomerList {
    private ArrayList<Customer> customers;
    private int customerCount;

    /**
     * Initializes an empty list of customers.
     */
    public CustomerList() {
        this.customers = new ArrayList<>();
    }

    /**
     * Adds a new customer to the list.
     *
     * @param customer The customer object to add.
     */
    public void addCustomer(Customer customer) {
        assert customer != null : "Cannot add a null customer";
        customers.add(customer);
        customerCount++;
    }

    /**
     * Removes a customer from the list.
     *
     * @param customer The customer object to be removed.
     */
    public void removeCustomer(Customer customer) {
        customers.remove(customer);
        customerCount--;
    }

    /**
     * Retrieves a customer by their index.
     *
     * @param index Zero-based index.
     * @return The Customer at that index.
     */
    public Customer getCustomer(int index) {
        assert index >= 0 && index < customers.size() : "Index out of bounds";
        return customers.get(index);
    }

    public int getCustomerCount() {
        return customerCount;
    }

    /**
     * Searches for customers whose names contain the keyword.
     *
     * @param keyword The search term (case-insensitive).
     * @return A list of matching customers.
     */
    public ArrayList<Customer> findByName(String keyword) {
        assert keyword != null : "Search keyword cannot be null";
        ArrayList<Customer> matches = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer.getName().toLowerCase().contains(keyword.toLowerCase())) {
                matches.add(customer);
            }
        }
        return matches;
    }

    /**
     * Returns the number of customers currently in the system.
     *
     * @return The total count of customers.
     */
    public int size() {
        return customers.size();
    }

    /**
     * Checks if a customer with the specified ID already exists in the list.
     *
     * @param customerId The ID to check (case-insensitive).
     * @return true if the ID already exists, false otherwise.
     */
    public boolean containsCustomerId(String customerId) {
        if (customerId == null) {
            return false;
        }

        for (Customer customer : customers) {
            if (customer.getCustomerId().equalsIgnoreCase(customerId.trim())) {
                return true;
            }
        }

        return false;
    }
}
