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

Displays all medications in the inventory with their index, name, dosage, quantity, expiry date, and tag.

Format: `list`

- Medications with quantity at or below 10 are flagged `[LOW STOCK]`.

---

### Find a medication: `find`

Searches for medications whose names contain the given keyword (case-insensitive).

Format: `find KEYWORD`

Example: `find Para`

---

### View medication details: `view`

Displays the full details of a specific medication, including dosage form, manufacturer, directions, warnings, and more.

Format: `view INDEX`

Example: `view 1`

---

### Delete a medication: `delete`

Removes a medication from the inventory by its index.

Format: `delete INDEX`

Example: `delete 2`

---

### Dispense a medication: `dispense`

Reduces the stock of a medication by the specified quantity. Optionally links the dispense event to a customer.

Format: `dispense INDEX q/QUANTITY [c/CUSTOMER_INDEX]`

- Dispensing fails if the requested quantity exceeds current stock.
- If `c/CUSTOMER_INDEX` is provided, the event is recorded in that customer's dispensing history.

Examples:
- `dispense 1 q/20`
- `dispense 1 q/20 c/1`

---

### Restock a medication: `restock`

Additively increases the stock of an existing medication (tops up, does not overwrite).

Format: `restock INDEX /q QUANTITY`

- `QUANTITY` must be a positive integer.

Example: `restock 1 /q 50`

---

### Sort medications by expiry: `sort`

Sorts all medications in the inventory by expiry date in ascending order (earliest expiry first).

Format: `sort`

---

### Check for expiring medications: `expiring`

Lists all medications that have already expired and those expiring within a specified number of days. Defaults to 30 days.

Format: `expiring [/days DAYS]`

Examples:
- `expiring`
- `expiring /days 14`

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

### List all customers: `listcustomers`

Displays all registered customers with their ID, name, and phone number.

Format: `listcustomers`

---

### View customer details: `view-customer`

Displays the full details of a specific customer, including their ID, name, phone number, address, and dispensing history.

Format: `view-customer INDEX`

Example: `view-customer 1`

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
