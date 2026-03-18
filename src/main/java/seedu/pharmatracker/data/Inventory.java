package seedu.pharmatracker.data;

import java.util.ArrayList;

public class Inventory {
    private ArrayList<Medication> medications;
    private int medicationCount;

    public Inventory() {
        this.medications = new ArrayList<>();
        this.medicationCount = 0;
    }

    public void addMedication(Medication medication) {
        assert medication != null : "Cannot add a null medication to the inventory.";
        int initialCount = this.medicationCount;
        medications.add(medication);
        medicationCount++;
        assert this.medicationCount == initialCount + 1 : "Inventory count did not increment correctly!";
    }

    public void removeMedication(Medication medication) {
        assert medication != null : "Cannot remove a null medication from the inventory";
        int initialCount = this.medicationCount;

        medications.remove(medication);
        medicationCount--;

        assert this.medicationCount == initialCount - 1 : "Inventory count did not decrement correctly!";
    }

    public int getMedicationCount() {
        return medicationCount;
    }
    public ArrayList<Medication> getMedications() {
        return this.medications;
    }

    public Medication getMedication(int index) {
        return this.medications.get(index);
    }

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
