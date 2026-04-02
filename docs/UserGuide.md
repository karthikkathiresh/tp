# User Guide

## Introduction

PharmaTracker is a command-line application for pharmacy staff to manage medication inventory and customer records. It supports adding, finding, dispensing, and restocking medications, as well as managing customer information and tracking dispensing history.

## Quick Start

1. Ensure Java 17 or above is installed on your computer.
2. Download the latest `pharmatracker.jar` from the releases page.
3. Open a terminal in the folder containing the jar file.
4. Run `java -jar pharmatracker.jar` to start the application.
5. Type a command and press Enter to execute it. Type `help` to see available commands.

> **Note for developers:** Run `.\gradlew run` (Windows) or `./gradlew run` (macOS/Linux) from the project root to launch the app.

---

## Features

### Notes on command format

- Words in `UPPER_CASE` are parameters to be supplied by the user.
- Items in square brackets `[...]` are optional.
- Parameters with a `/` prefix are flags (e.g. `/n NAME`).
- INDEX refers to the 1-based position of an item as shown in the list/inventory.

---

## Medication Commands

### Add a medication: `add`

Adds a new medication to the inventory.

Format: `add /n NAME /d DOSAGE /q QUANTITY /e EXPIRY [/t TAG] [/df DOSAGE_FORM] [/mfr MANUFACTURER] [/dir DIRECTIONS] [/freq FREQUENCY] [/route ROUTE] [/max MAX_DAILY_DOSE] [/warn WARNING]`

- `EXPIRY` must be in `dd/MM/yyyy` format.
- `/warn` can be repeated for multiple warnings.

Examples:
- `add /n Paracetamol /d 500mg /q 100 /e 31/12/2026 /t painkiller`
- `add /n Amoxicillin /d 250mg /q 50 /e 01/06/2026 /t antibiotic /df Capsule /mfr Pfizer /warn May cause allergic reactions`

---

### List all medications: `list`

Displays a high-level summary of all medications currently stored in the system. This provides a quick overview of drug names and current stock levels for the pharmacist.

**Format**: `list`

* Shows a numbered list of all medication records currently in the inventory.
* Each entry displays the name, dosage, current quantity, and expiry date.
* Medications with a quantity of **10 or less** are automatically flagged with `[LOW STOCK]`.
* This list provides the **INDEX** values required for other commands such as `delete`, `view`, and `dispense`.

**Example**:
`list`

**Expected Output**:
```
PharmaTracker Inventory:
1. Amoxicillin | 250mg | Qty: 50  | Expiry: 01/06/2025
2. Ibuprofen   | 200mg | Qty: 5   | Expiry: 15/06/2026 [LOW STOCK]
3. Paracetamol | 500mg | Qty: 150 | Expiry: 31/12/2026
------------------------------------------------------
Total Medications: 3
```
---

### Find a medication: `find`

Searches for medications whose names contain the given keyword (case-insensitive).

Format: `find KEYWORD`

- Partial matches are supported (e.g. `Para` matches `Paracetamol`).
- At least one character must be provided as the keyword.
- If no medications match, a message indicating this is shown instead.

Example: `find Para`

Expected output:
```
Found 2 matching medication(s):
1. Paracetamol | 500mg | Qty: 100 | Expiry: 31/12/2026 | Tag: painkiller
2. Paracetamol Junior | 250mg | Qty: 40 | Expiry: 15/06/2026 | Tag: painkiller
```

If no match is found:
```
No medications found matching: Para
```

---

### View medication details: `view`

Displays the full details of a specific medication, including dosage form, manufacturer, directions, warnings, and more.

Format: `view INDEX`

- `INDEX` must be a positive integer corresponding to a medication shown in `list`.

Example: `view 1`

Expected output:
```
========================================
MEDICATION DETAILS
========================================
Drug Name:           Amoxicillin
Strength:            250mg
Dosage Form:         Capsule
Manufacturer:        Pfizer
----------------------------------------
DOSAGE & ADMINISTRATION
----------------------------------------
Directions:          Take with food
Frequency:           Twice daily
Route:               Oral
Max Daily Dose:      500mg
----------------------------------------
WARNINGS & PRECAUTIONS
----------------------------------------
- May cause allergic reactions
========================================
```

---

### Delete a medication: `delete`

Removes a medication from the inventory by its index.

Format: `delete INDEX`

Example: `delete 2`

---

### Dispense a medication: `dispense`

Reduces the stock of a medication by the specified quantity. Optionally links the
dispense event to a registered customer — when a customer index is provided, the
dispensed medication is automatically recorded in that customer's dispensing
history. If `c/CUSTOMER_INDEX` is omitted, the command behaves exactly as before.

**Format**: `dispense INDEX q/QUANTITY [c/CUSTOMER_INDEX]`

- Dispensing fails if the requested quantity exceeds the current stock.
- `c/CUSTOMER_INDEX` is optional. If omitted, no customer record is updated.
- If `c/CUSTOMER_INDEX` is provided but out of range, an error is shown and
  stock remains unchanged.

**Example — no customer linked:**

`dispense 2 q/10`

```
Dispensing successfully!
Medication: Ibuprofen
Amount: 10 units
Updated Stock: 40 units
```

**Example — linked to customer:**

`dispense 1 q/20 c/1`

```
Dispensing successfully!
Medication: Paracetamol
Amount: 20 units
Updated Stock: 110 units
Recorded for customer: [C001] John Tan.
```

---

### Restock a medication: `restock`

Additively increases the stock of an existing medication. Unlike `update`, which
overwrites the quantity, `restock` tops up on top of the current stock level.
Useful when a new shipment of medication arrives.

**Format**: `restock INDEX /q QUANTITY`

- `INDEX` must be a positive integer corresponding to a medication shown in `list`.
- `QUANTITY` must be a positive integer.

**Examples:**

- `restock 1 /q 50`

  Adds 50 units to the 1st medication. If Paracetamol had 130 units:

  ```
  Restocked successfully!
  Medication: Paracetamol | Added: 50 units | Updated Stock: 180 units.
  ```

- `restock 3 /q 100`

  Adds 100 units to the 3rd medication. If Amoxicillin had 20 units:

  ```
  Restocked successfully!
  Medication: Amoxicillin | Added: 100 units | Updated Stock: 120 units.
  ```

---

### Sort medications by expiry: `sort`

Sorts all medications in the inventory by expiry date in ascending order (earliest expiry first).

Format: `sort`

---

### Check for expiring medications: `expiring`

Lists all medications that have already expired, and those expiring within a specified number of days. Defaults to 30 days if no value is provided.

Format: `expiring [/days DAYS]`

- `DAYS` must be a non-negative integer.
- Expired medications are shown separately from soon-to-expire medications.
- If there are no expired or expiring medications, a message indicating this is shown.

Examples:
- `expiring`
- `expiring /days 14`

Expected output:
```
Already expired:
1. Aspirin | 100mg | Qty: 30 | Expiry: 01/01/2025 | Tag: painkiller
Total: 1 medication(s) expired.
----------------------------------------
Expiring within 14 days:
1. Ibuprofen | 200mg | Qty: 12 | Expiry: 10/04/2026 | Tag: painkiller
Total: 1 medication(s) expiring soon.
```

If no results are found:
```
No expired or expiring medications found.
```

---

### Check low stock: `lowstock`

Lists all medications whose quantity falls below a threshold. The default threshold is 20 units.

Format: `lowstock [/threshold NUMBER]`

- Medications with `quantity < NUMBER` are shown.
- A medication at exactly the threshold is **not** considered low stock.

Examples:
- `lowstock`
- `lowstock /threshold 10`

Expected output (example):
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

### Print a medication label: `label`

Prints a formatted medication label for a specific medication, suitable for attaching to dispensed packages.

Format: `label INDEX`

Example: `label 1`

---

## Customer Commands

### Add a customer: `add-customer`

Registers a new customer in the system.

Format: `add-customer /id CUSTOMER_ID /n NAME /p PHONE /addr ADDRESS`

Example: `add-customer /id C001 /n John Tan /p 99887766 /addr 10 Orchard Road`

---

## List all customers: `list-customers`

Displays a numbered list of all customers currently registered in the system,
showing their customer ID, name, and phone number.

**Format**: `list-customers`

**Example — 3 customers registered:**

```
PharmaTracker Customers:
1. [C001] John Tan | Phone: 99887766
2. [C002] Mary Tan | Phone: 87654321
3. [C003] David Ng | Phone: 93456789
------------------------------------------------------
Total Customers: 3.
```

**Example — no customers registered:**

```
PharmaTracker Customers:
No customers registered yet.
```

---

### View customer details: `view-customer`

Displays the full details of a specific customer, including their ID, name, phone number, address, and full dispensing history.

Format: `view-customer INDEX`

- `INDEX` must be a positive integer corresponding to a customer shown in `listcustomers`.
- If the customer has no dispensing history, a message indicating this is shown instead.

Example: `view-customer 1`

Expected output:
```
========================================
CUSTOMER DETAILS
========================================
Customer ID:         C001
Name:                John Tan
Phone:               99887766
Address:             10 Orchard Road
----------------------------------------
DISPENSING HISTORY
----------------------------------------
1. Paracetamol | 500mg | Qty dispensed: 20 | Date: 01/04/2026
2. Amoxicillin | 250mg | Qty dispensed: 10 | Date: 03/04/2026
========================================
```

If the customer has no dispensing history:
```
========================================
CUSTOMER DETAILS
========================================
Customer ID:         C001
Name:                John Tan
Phone:               99887766
Address:             10 Orchard Road
----------------------------------------
DISPENSING HISTORY
----------------------------------------
No medications dispensed yet.
========================================
```

---

### Update a customer: `updatecustomer`

Updates one or more fields of an existing customer record. Only the fields you provide are changed; all others remain unchanged.

Format: `updatecustomer INDEX [/n NAME] [/p PHONE] [/a ADDRESS]`

- At least one of `/n`, `/p`, or `/a` must be provided.
- `INDEX` is the 1-based position of the customer as shown in `listcustomers`.

Examples:
- `updatecustomer 1 /n Alice Tan` — updates name only
- `updatecustomer 2 /p 81234567 /a 99 Clementi Ave` — updates phone and address
- `updatecustomer 1 /n Bob /p 98765432 /a 5 Bukit Timah Road` — updates all three fields

Expected output:
```
-------------------------------
Customer updated: [C001] Alice Tan | Phone: 81234567 | Address: 99 Clementi Ave
-------------------------------
```

---

## General Commands

### View help: `help`

Displays a summary of all available commands.

Format: `help`

---

### Exit the application: `bye`

Exits PharmaTracker. Data is automatically saved before the application closes.

Format: `bye`

---

## FAQ

**Q: How do I transfer my data to another computer?**

A: Copy the `data/pharmatracker.txt` file from your current machine to the same relative path on the new machine before launching the application.

**Q: What date format does PharmaTracker use for expiry dates?**

A: Expiry dates must be entered in `dd/MM/yyyy` format (e.g. `31/12/2026`).

**Q: What happens if I enter an invalid index?**

A: PharmaTracker will display an error message and leave the inventory or customer list unchanged.

---

## Command Summary

| Command | Format |
|---------|--------|
| Add medication | `add /n NAME /d DOSAGE /q QUANTITY /e EXPIRY [/t TAG] [...]` |
| List medications | `list` |
| Find medication | `find KEYWORD` |
| View medication | `view INDEX` |
| Delete medication | `delete INDEX` |
| Dispense medication | `dispense INDEX q/QUANTITY [c/CUSTOMER_INDEX]` |
| Restock medication | `restock INDEX /q QUANTITY` |
| Sort by expiry | `sort` |
| Check expiring | `expiring [/days DAYS]` |
| Check low stock | `lowstock [/threshold NUMBER]` |
| Print label | `label INDEX` |
| Add customer | `add-customer /id ID /n NAME /p PHONE /addr ADDRESS` |
| List customers | `listcustomers` |
| View customer | `view-customer INDEX` |
| Update customer | `updatecustomer INDEX [/n NAME] [/p PHONE] [/a ADDRESS]` |
| Help | `help` |
| Exit | `bye` |
