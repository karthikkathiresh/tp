package seedu.pharmatracker.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Maintains a chronological list of all dispense events across all sessions.
 * Supports filtering records by date for daily summary reports.
 */
public class DispenseLog {

    private final ArrayList<DispenseRecord> records;

    /**
     * Constructs an empty DispenseLog.
     */
    public DispenseLog() {
        this.records = new ArrayList<>();
    }

    /**
     * Appends a new dispense record to the log.
     *
     * @param record The record to add.
     */
    public void addRecord(DispenseRecord record) {
        assert record != null : "DispenseRecord must not be null";
        records.add(record);
    }

    /**
     * Returns all records in the log.
     *
     * @return The full list of dispense records.
     */
    public ArrayList<DispenseRecord> getAllRecords() {
        return records;
    }

    /**
     * Returns only the records that belong to the given date.
     *
     * @param date The date to filter by.
     * @return A list of records whose date matches the given date.
     */
    public List<DispenseRecord> getRecordsByDate(LocalDate date) {
        return records.stream()
                .filter(r -> r.getDate().equals(date))
                .collect(Collectors.toList());
    }

    /**
     * Returns the total number of records stored in the log.
     *
     * @return The record count.
     */
    public int size() {
        return records.size();
    }
}
