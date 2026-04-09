# Yaqi - Project Portfolio Page

## Overview

PharmaTracker is a command-line application for pharmacy staff to manage medication inventory and customer records. It supports adding, dispensing, restocking, and tracking medications, as well as managing customer information and dispensing history. PharmaTracker is written in Java and targets users who prefer a fast, keyboard-driven workflow over a GUI.

---

## Summary of Contributions

### Code Contributed

[View my code on the tP Code Dashboard](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=yaqi&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=)

---

### Enhancements Implemented

#### 1. Low Stock Command (`lowstock`)

- **What it does:** Lists all medications whose quantity falls below a configurable threshold. The default threshold is 20 units. Users may specify a custom threshold with the optional `/threshold` flag.
- **Format:** `lowstock [/threshold NUMBER]`
- **Justification:** Pharmacy staff need to be alerted when stock levels are running low so they can reorder before supplies run out. This command provides a quick at-a-glance report without manually scanning the full inventory.

#### 2. Update Customer Command (`updatecustomer`)

- **What it does:** Updates one or more fields (name, phone number, address) of an existing customer record. Only the fields explicitly provided are changed; all others remain unchanged.
- **Format:** `updatecustomer INDEX [/n NAME] [/p PHONE] [/a ADDRESS]`

#### 3. Label Command (`label`) — Earlier Contribution

- Implemented the `label` command (originally named `PrintDescriptionCommand`, later renamed) that generates a formatted printable medication label for dispensing or packaging.
- Added the corresponding JUnit test.

#### 4. Expiry Date Handling

- Implemented `Medication` and related parsing to handle both `yyyy-MM-dd` and `dd/MM/yyyy` expiry date formats, improving robustness when loading saved data or accepting user input.

#### 5. Sort Command Enhancements

- Implemented `Sort` command

#### 6. Daily Dispense Log (`dispenselog`)

- **What it does:** Records every dispense event (medication name, dosage, quantity, time, and optional patient name) and displays a formatted daily summary on demand. Defaults to today's date; an optional `/date YYYY-MM-DD` flag lets staff view logs for any past date.
- **Format:** `dispenselog [/date YYYY-MM-DD]`
- **Justification:** Pharmacies are often required to maintain a daily dispensing record for audit and compliance purposes. This feature auto-captures each dispense in the background and persists the log to `data/dispense_log.txt` across sessions so nothing is lost on restart.
- **Files added/modified:**
  - `DispenseRecord.java` — data class for a single dispense event
  - `DispenseLog.java` — collection of records with date-based filtering
  - `DispenseSummaryCommand.java` — command implementation
  - `Inventory.java` — added `DispenseLog` field
  - `DispenseCommand.java` — appends a record after each successful dispense
  - `Storage.java` — `saveDispenseLog()` / `loadDispenseLog()` for persistence
  - `PharmaTrackerParser.java` — added `dispenselog` command case
  - `Ui.java` — added `printDispenseSummary()` and updated help menu

---

### Contributions to the User Guide

- **Update the User Guide** to cover all 16 commands.
- Authored the following sections in full:
  - **Low Stock** (`lowstock`) — format, usage notes, threshold behaviour, examples, sample output.
  - **Update Customer** (`updatecustomer`) — format, partial-update rules, examples for single/multiple/all fields, sample output.
  - **Command Summary** table — all 16 commands with their formats in one reference table.

---

### Contributions to the Developer Guide

- **Low Stock Feature** section — step-by-step implementation walkthrough, design considerations table (default threshold, strict `<` comparison, optional flag rationale).
- **Update Customer Feature** section — step-by-step implementation walkthrough, design considerations table (null for absent flags, partial update rationale, validation location).

---

## Contributions to the User Guide (Extracts)

### Check Low Stock: `lowstock`

Lists all medications whose quantity falls below a threshold. The default threshold is 20 units.

**Format:** `lowstock [/threshold NUMBER]`

- Medications with `quantity < NUMBER` are shown.
- A medication at exactly the threshold is **not** considered low stock.

**Examples:**
- `lowstock`
- `lowstock /threshold 10`

**Expected output (example):**
```
-------------------------------
Low Stock Report (threshold: 20)
-------------------------------
1. Paracetamol | 500mg | Qty: 5 | Expiry: 31/12/2026
2. Ibuprofen | 200mg | Qty: 12 | Expiry: 01/06/2026
Total: 2 medication(s) low on stock.
-------------------------------
```

---

### Update a Customer: `updatecustomer`

Updates one or more fields of an existing customer record. Only the fields you provide are changed; all others remain unchanged.

**Format:** `updatecustomer INDEX [/n NAME] [/p PHONE] [/a ADDRESS]`

- At least one of `/n`, `/p`, or `/a` must be provided.
- `INDEX` is the 1-based position as shown in `listcustomers`.

**Examples:**
- `updatecustomer 1 /n Alice Tan` — updates name only
- `updatecustomer 2 /p 81234567 /a 99 Clementi Ave` — updates phone and address
- `updatecustomer 1 /n Bob /p 98765432 /a 5 Bukit Timah Road` — updates all three fields

**Expected output:**
```
-------------------------------
Customer updated: [C001] Alice Tan | Phone: 81234567 | Address: 99 Clementi Ave
-------------------------------
```

---

### View Daily Dispense Log: `dispenselog`

Displays a summary of all medications dispensed on a given date. Defaults to today if no date is provided.

**Format:** `dispenselog [/date YYYY-MM-DD]`

- Each entry shows the time, medication name, dosage, quantity dispensed, and patient name (if linked).
- A total unit count and event count are shown at the bottom.
- The log persists across sessions in `data/dispense_log.txt`.

**Examples:**
- `dispenselog`
- `dispenselog /date 2026-04-09`

**Expected output (example):**
```
-------------------------------
Dispense Log for 2026-04-09
-------------------------------
1. 09:15 | Paracetamol | Dosage: 500mg | Qty: 2 | Patient: Alice Tan
2. 11:42 | Ibuprofen | Dosage: 200mg | Qty: 1
3. 14:30 | Amoxicillin | Dosage: 250mg | Qty: 3 | Patient: Bob Lee
-------------------------------
Total: 3 dispense event(s), 6 unit(s) dispensed.
-------------------------------
```
