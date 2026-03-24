package seedu.pharmatracker.data;

import java.util.ArrayList;

/**
 * Manages a list of customers.
 * Provides methods for adding, deleting, and searching for customers.
 */
public class CustomerList {
    private ArrayList<Customer> customers;
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
    }

    /**
     * Deletes a customer from the list based on their index.
     *
     * @param index Zero-based index of the customer.
     */
    public void deleteCustomer(int index) {
        assert index >= 0 && index < customers.size() : "Index out of bounds";
        customers.remove(index);
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
}
