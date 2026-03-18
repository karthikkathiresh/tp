package seedu.pharmatracker.data;

import java.util.ArrayList;

/**
 * Represents the inventory of medications in the PharmaTracker application.
 * Manages the storage, addition, removal and retrieval of {@link Medication} objects.
 */
public class Inventory {
    private ArrayList<Medication> medications;
    private int medicationCount;

    /**
     * Constructs an empty {@code Inventory}.
     * Initializes the internal list of medications and sets the initial count to zero.
     */
    public Inventory() {
        this.medications = new ArrayList<>();
        this.medicationCount = 0;
    }

    /**
     * Adds a new {@link Medication} to the inventory and increments the medication count.
     *
     * @param medication The medication object to be added.
     */
    public void addMedication(Medication medication) {
        assert medication != null : "Cannot add a null medication to the inventory.";
        int initialCount = this.medicationCount;
        medications.add(medication);
        medicationCount++;
        assert this.medicationCount == initialCount + 1 : "Inventory count did not increment correctly!";
    }

    /**
     * Removes a specified {@link Medication} from the inventory and decrements the medication count.
     *
     * @param medication The medication object to be deleted.
     */
    public void removeMedication(Medication medication) {
        assert medication != null : "Cannot remove a null medication from the inventory";
        int initialCount = this.medicationCount;

        medications.remove(medication);
        medicationCount--;

        assert this.medicationCount == initialCount - 1 : "Inventory count did not decrement correctly!";
    }

    /**
     * Returns the total number of medications currently stored in the inventory.
     *
     * @return The integer count of medications.
     */
    public int getMedicationCount() {
        return medicationCount;
    }

    /**
     * Retrieves the entire list of medications.
     *
     * @return An {@code ArrayList} containing all stored {@link Medication} objects.
     */
    public ArrayList<Medication> getMedications() {
        return this.medications;
    }

    /**
     * Retrieves a specific {@link Medication} from the inventory based on its zero-based index.
     *
     * @param index The zero-based index of the medication to retrieve.
     * @return The {@link Medication} located at the specified index.
     */
    public Medication getMedication(int index) {
        return this.medications.get(index);
    }

    /**
     * Prints a formatted list of all medications currently in the inventory to the console.
     * If the inventory is empty, prints a notification message instead.
     */
    public void listMedications() {
        if (medications.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        for (int i = 0; i < medications.size(); i++) {
            Medication med = medications.get(i);
            System.out.println((i + 1) + ". " + med.getName()
                    + " | Dosage: " + med.getDosage()
                    + " | Qty: " + med.getQuantity()
                    + " | Expiry: " + med.getExpiryDate()
                    + " | Tag: " + med.getTag());
        }
    }

}
