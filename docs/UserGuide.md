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
- Most commands require an authenticated session. Use `register` and `login` first.

---

## Medication Commands

### Add a medication: `add`
Adds a new medication to the inventory.

**Format:** `add /n NAME /d DOSAGE /q QUANTITY /e EXPIRY [/t TAG] [/df DOSAGE_FORM] [/mfr MANUFACTURER] [/dir DIRECTIONS] [/freq FREQUENCY] [/route ROUTE] [/max MAX_DAILY_DOSE] [/warn WARNING]`

**Mandatory Parameters:**
* `/n NAME`: The name of the medication. 
* `/d DOSAGE`: The strength or dosage (e.g., 500mg).
* `/q QUANTITY`: The number of units in stock. **Must be a positive integer**.
* `/e EXPIRY`: The expiration date. **Must be in `DD/MM/YYYY` or `DD-MM-YYYY` or `YYYY-MM-DD` format**.

**Optional Parameters:**
* `/t TAG`: A category to group the medication (e.g., `antibiotic`, `painkiller`).
* `/df DOSAGE_FORM`: The physical form of the medication (e.g., `Tablet`, `Syrup`, `Ointment`).
* `/mfr MANUFACTURER`: The company that produces the medication (e.g., `Pfizer`, `GSK`).
* `/dir DIRECTIONS`: Specific instructions for consumption (e.g., `Take after meals`, `Dissolve in water`).
* `/freq FREQUENCY`: How often the medication should be taken (e.g., `Twice a day`, `Every 8 hours`).
* `/route ROUTE`: The method of administration (e.g., `Oral`, `Topical`, `Intravenous`).
* `/max MAX_DAILY_DOSE`: The maximum safe limit to consume within 24 hours (e.g., `4000mg`, `4 tablets`).
* `/warn WARNING`: Important safety warnings or side effects. This parameter can be repeated to add multiple warnings (e.g., `/warn Drowsiness /warn Avoid alcohol`).

**Examples:**
* `add /n Paracetamol /d 500mg /q 100 /e 31/12/2026 /t painkiller`
* `add /n Amoxicillin /d 250mg /q 50 /e 01/06/2026 /t antibiotic /df Capsule /mfr Pfizer /warn May cause allergic reactions`
* `add /n Cough Syrup /d 15mg/5ml /q 20 /e 15/10/2025 /df Syrup /dir Shake well before use /freq Every 6 hours /route Oral`

**Expected Output:**

```
____________________________________________________________
You have added the following medication:
  Name: Paracetamol | Dosage: 500mg | Qty: 100 | Exp: 2026-12-31 | Tag: painkiller
You now have 4 medications in your inventory!
____________________________________________________________
```
---

### Delete a medication: `delete`

Removes a medication from the inventory. 

**Format:** `delete INDEX`

- Deletes the medication at the specified `INDEX`. 
- The `INDEX` refers to the index number shown in the **most recently displayed** medication list.
- The index **must be a positive integer**, and must not exceed the number of medications currently in the list.

**Examples:**
- `list` followed by `delete 5` (This deletes the 5th medication in the inventory.)

**Expected Output:**

```
____________________________________________________________
You have deleted the following medication:
  Name: Paracetamol | Dosage: 500mg | Qty: 100 | Exp: 2026-12-31 | Tag: painkiller
You now have 6 medications in your inventory!
____________________________________________________________
```

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
### Dispense a medication: `dispense`

Reduces the stock of a medication by the specified quantity. Optionally links the
dispense event to a registered customer — when a customer index is provided, the
dispensed medication is automatically recorded in that customer's dispensing
history. If `/c CUSTOMER_INDEX` is omitted, the command behaves exactly as before.

**Format**: `dispense INDEX q/QUANTITY [/c CUSTOMER_INDEX]`

- Dispensing fails if the requested quantity exceeds the current stock.
- `/c CUSTOMER_INDEX` is optional. If omitted, no customer record is updated.
- If `/c CUSTOMER_INDEX` is provided but out of range, an error is shown and
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

`dispense 1 q/20 /c 1`

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

### Update a medication: `update`

Edits the details of an existing medication in the inventory.

**Format:** `update INDEX [/n NAME] [/d DOSAGE] [/q QUANTITY] [/e EXPIRY] [/t TAG] [/df DOSAGE_FORM] [/mfr MANUFACTURER] [/dir DIRECTIONS] [/freq FREQUENCY] [/route ROUTE] [/max MAX_DAILY_DOSE] [/warn WARNING]...`

**Rules & Constraints:**
* Updates the medication at the specified `INDEX`.
* The index refers to the **most recently displayed** medication list.
* The index **must be a positive integer** (e.g., 1, 2, 3) and must not exceed the total number of medications in the list.
* **At least one** of the optional fields must be provided.
* Existing values will be completely overwritten by the newly provided values.

**Examples:**
* `update 1 /e 31/12/2027`: Updates the expiry date of the 1st medication in the list to `31/12/2027`. All other details remain unchanged.
* `update 2 /n Amoxicillin Forte /d 500mg`: Updates both the name and the dosage of the 2nd medication in the list.

**Notes:**
* **Updating quantities:** While you *can* use this command to overwrite the quantity (`/q`), it is highly recommended to use the `restock` and `dispense` commands for everyday inventory management, as they safely add to or subtract from the current stock.
* **Replacing lists (Warnings/Tags):** When updating fields that can have multiple values (like `/warn` or `/t`), the existing values are **completely replaced** by the new ones.
  * *Example:* If a medication has the warnings "Drowsiness" and "Take with food", typing `update 1 /warn Avoid alcohol` will remove the first two warnings and leave *only* "Avoid alcohol".
* **Clearing lists:** *(Optional depending on your implementation)* To clear all warnings or tags from a medication without adding new ones, type the parameter without any text after it (e.g., `update 1 /warn`).

**Example Output:**

```
____________________________________________________________
Medication updated: Zyrtec | Expiry updated to 31/12/2027
____________________________________________________________
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

### View daily dispense log: `dispenselog`

Displays a summary of all medications dispensed on a given date. Defaults to today if no date is provided. Each dispense event is automatically recorded whenever the `dispense` command is used, and the log persists across sessions.

**Format:** `dispenselog [/date YYYY-MM-DD]`

- Each entry shows the time, medication name, dosage, quantity dispensed, and patient name (if the dispense was linked to a customer).
- A total event count and unit count are shown at the bottom.
- The date must be in `YYYY-MM-DD` format (e.g. `2026-04-09`).

**Examples:**
- `dispenselog`
- `dispenselog /date 2026-04-09`

**Expected output (today's log):**
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

**Expected output (no events recorded):**
```
-------------------------------
Dispense Log for 2026-04-09
-------------------------------
No dispense events recorded for 2026-04-09.
-------------------------------
```

---

## Customer Commands

### Add a customer: `add-customer`

Registers a new customer into the pharmacy's database.

**Format:** `add-customer /id CUSTOMER_ID /n NAME /p PHONE [/addr ADDRESS]`

**Mandatory Parameters:**
* `/id CUSTOMER_ID`: A unique identifier for the customer (e.g., `C001`).
* `/n NAME`: The full name of the customer.
* `/p PHONE`: The customer's contact number.

**Optional Parameters:**
* `/addr ADDRESS`: The customer's residential or mailing address.

**Examples:**
* `add-customer /id C001 /n John Doe /p 98765432`
* `add-customer /id C002 /n Jane Smith /p 91234567 /a 123 Clementi Road, #04-56`

---

### Delete a customer: `delete-customer`

Removes an existing customer from the pharmacy's database.

**Format:** `delete-customer INDEX`

**Rules & Constraints:**
* Deletes the customer at the specified `INDEX`.
* The `INDEX` refers to the index number shown in the **most recently displayed** customer list (e.g., after running a `list-customers` or `find-customer` command).
* The index **must be a positive integer** and must not exceed the total number of customers currently in the list.

**Examples:**
* `list-customers` followed by `delete-customer 2`: Deletes the 2nd customer shown in the complete customer list.

---

### List all customers: `list-customers`

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

### Update a customer: `update-customer`

Updates one or more fields of an existing customer record. Only the fields you provide are changed; all others remain unchanged.

Format: `update-customer INDEX [/n NAME] [/p PHONE] [/a ADDRESS]`

- At least one of `/n`, `/p`, or `/a` must be provided.
- `INDEX` is the 1-based position of the customer as shown in `list-customers`.

Examples:
- `update-customer 1 /n Alice Tan` — updates name only
- `update-customer 2 /p 81234567 /a 99 Clementi Ave` — updates phone and address
- `update-customer 1 /n Bob /p 98765432 /a 5 Bukit Timah Road` — updates all three fields

Expected output:
```
-------------------------------
Customer updated: [C001] Alice Tan | Phone: 81234567 | Address: 99 Clementi Ave
-------------------------------
```

---

## Authentication Commands

### Register an account: `register`

Creates a new user account.

Format: `register USERNAME /p PASSWORD`

- `USERNAME` must be non-empty.
- `PASSWORD` must be non-empty and follow validation rules enforced by the app.

Example:
- `register pharmacist1 /p S3curePass!`

---

### Login: `login`

Authenticates an existing user and starts a session.

Format: `login USERNAME /p PASSWORD`

Example:
- `login pharmacist1 /p S3curePass!`

---

### Logout: `logout`

Ends the current authenticated session.

Format: `logout`

---

## Auto Restock Alert Commands

### Set a medication threshold: `set-threshold`

Sets a medication-specific minimum stock threshold used by the automatic restock alert system.

Format: `set-threshold INDEX /threshold NUMBER`

- `INDEX` must point to a valid medication.
- `NUMBER` must be a positive integer.

Examples:
- `set-threshold 1 /threshold 30`
- `set-threshold 3 /threshold 12`

---

### View active alerts: `alerts`

Displays all currently active (unacknowledged) restock alerts.

Format: `alerts`

---

### Acknowledge an alert: `ack-alert`

Marks an active alert as acknowledged and removes it from the active list.

Format: `ack-alert ALERT_INDEX`

Example:
- `ack-alert 2`

---

### View alert history: `alert-history`

Displays persisted alert history, including active, acknowledged, and auto-resolved alerts.

Format: `alert-history`

---

## General Commands

### View help: `help`

Displays a summary of all available commands.

Format: `help`

---

### Exit the application: `exit`

Exits PharmaTracker. Data is automatically saved before the application closes.

Format: `exit`

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

| Command             | Format |
|---------------------|--------|
| Add medication      | `add /n NAME /d DOSAGE /q QUANTITY /e EXPIRY [/t TAG] [...]` |
| Update medication   | `update INDEX [/n NAME] [/d DOSAGE] [/q QUANTITY] [/e EXPIRY]...` |
| List medications    | `list` |
| Find medication     | `find KEYWORD` |
| View medication     | `view INDEX` |
| Delete medication   | `delete INDEX` |
| Dispense medication | `dispense INDEX q/QUANTITY [/c CUSTOMER_INDEX]` |
| Restock medication  | `restock INDEX /q QUANTITY` |
| Sort by expiry      | `sort` |
| Check expiring      | `expiring [/days DAYS]` |
| Check low stock     | `lowstock [/threshold NUMBER]` |
| Print label         | `label INDEX` |
| Daily dispense log  | `dispenselog [/date YYYY-MM-DD]` |
| Add customer        | `add-customer /id ID /n NAME /p PHONE /addr ADDRESS` |
| List customers      | `list-customers` |
| View customer       | `view-customer INDEX` |
| Update customer     | `update-customer INDEX [/n NAME] [/p PHONE] [/a ADDRESS]` |
| Delete customer     | `delete-customer INDEX`     |
| Register            | `register USERNAME /p PASSWORD` |
| Login               | `login USERNAME /p PASSWORD` |
| Logout              | `logout` |
| Set threshold       | `set-threshold INDEX /threshold NUMBER` |
| View active alerts  | `alerts` |
| Acknowledge alert   | `ack-alert ALERT_INDEX` |
| Alert history       | `alert-history` |
| Help                | `help` |
| Exit                | `exit` |
