package seedu.pharmatracker.data;

import java.util.ArrayList;

public class Inventory {
    private ArrayList<Medication> medications;

    public Inventory() {
        this.medications = new ArrayList<>();
    }

    public ArrayList<Medication> getMedications() {
        return this.medications;
    }

    public Medication getMedication(int index) {
        return this.medications.get(index);
    }
}
