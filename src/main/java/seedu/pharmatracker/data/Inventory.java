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
        medications.add(medication);
        medicationCount++;
    }

    public ArrayList<Medication> getMedications() {
        return this.medications;
    }

    public Medication getMedication(int index) {
        return this.medications.get(index);
    }
}
