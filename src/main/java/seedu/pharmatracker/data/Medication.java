package seedu.pharmatracker.data;

public class Medication {
    private String name;
    private String dosage;
    private int quantity;
    private String expiryDate;
    private String tag;

    public Medication(String name, String dosage, int quantity, String expiryDate, String tag) {
        this.name = name;
        this.dosage = dosage;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.tag = tag;
    }

    public String getName() {
        return this.name;
    }

    public String getDosage() {
        return this.dosage;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String getExpiryDate() {
        return this.expiryDate;
    }

    public String getTag() {
        return this.tag;
    }

    @Override
    public String toString() {
        return null;
    }
}
