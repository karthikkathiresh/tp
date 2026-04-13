# User Guide

## Table of Contents

- [Introduction](#introduction)
- [Quick Start](#quick-start)
- [Features](#features)
  - [Notes on command format](#notes-on-command-format)
- [Medication Commands](#medication-commands)
  - [Add a medication: `add`](#add-a-medication-add)
  - [Delete a medication: `delete`](#delete-a-medication-delete)
  - [List all medications: `list`](#list-all-medications-list)
  - [Find a medication: `find`](#find-a-medication-find)
  - [View medication details: `view`](#view-medication-details-view)
  - [Dispense a medication: `dispense`](#dispense-a-medication-dispense)
  - [Restock a medication: `restock`](#restock-a-medication-restock)
  - [Update a medication: `update`](#update-a-medication-update)
  - [Sort medications by expiry: `sort`](#sort-medications-by-expiry-sort)
  - [Check for expiring medications: `expiring`](#check-for-expiring-medications-expiring)
  - [Check low stock: `lowstock`](#check-low-stock-lowstock)
  - [Print a medication label: `label`](#print-a-medication-label-label)
  - [View daily dispense log: `dispenselog`](#view-daily-dispense-log-dispenselog)
- [Customer Commands](#customer-commands)
  - [Add a customer: `add-customer`](#add-a-customer-add-customer)
  - [Delete a customer: `delete-customer`](#delete-a-customer-delete-customer)
  - [List all customers: `list-customers`](#list-all-customers-list-customers)
  - [Find a customer: `find-customer`](#find-a-customer-find-customer)
  - [View customer details: `view-customer`](#view-customer-details-view-customer)
  - [Update a customer: `update-customer`](#update-a-customer-update-customer)
- [Authentication Commands](#authentication-commands)
  - [Register an account: `register`](#register-an-account-register)
  - [Login: `login`](#login-login)
  - [Logout: `logout`](#logout-logout)
- [Auto Restock Alert Commands](#auto-restock-alert-commands)
  - [Set a medication threshold: `set-threshold`](#set-a-medication-threshold-set-threshold)
  - [View active alerts: `alerts`](#view-active-alerts-alerts)
  - [Acknowledge an alert: `ack-alert`](#acknowledge-an-alert-ack-alert)
  - [View alert history: `alert-history`](#view-alert-history-alert-history)
- [General Commands](#general-commands)
  - [View help: `help`](#view-help-help)
  - [Exit the application: `exit`](#exit-the-application-exit)
- [FAQ](#faq)
- [Command Summary](#command-summary)

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
* `/q QUANTITY`: The number of units in stock. **Must be a non-negative integer** (0 or above).
   A quantity of 0 is valid for registering a medication profile before the first shipment arrives.
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
* Each entry displays labels in this format: `Name: ... | Dosage: ... | Qty: ... | Exp: ...`.
* Optional fields (e.g. `Tag`, `Dosage Form`, `Manufacturer`, `Directions`, `Frequency`, `Route`, `Maximum Daily Dosage`) are shown inline when present.
* Medications with a quantity of **less than 20** are automatically flagged with `[LOW STOCK]` (consistent with the default threshold for the `lowstock` command).
* This list provides the **INDEX** values required for other commands such as `delete`, `view`, and `dispense`.

**Example**:
`list`

**Expected Output**:
```
PharmaTracker Inventory:
1. Name: Amoxicillin | Dosage: 250mg | Qty: 50 | Exp: 2025-06-01 | Tag: antibiotic
2. Name: Ibuprofen | Dosage: 200mg | Qty: 5 | Exp: 2026-06-15 | Tag: painkiller [LOW STOCK]
3. Name: Paracetamol | Dosage: 500mg | Qty: 150 | Exp: 2026-12-31 | Tag: painkiller
------------------------------------------------------
Total Medications: 3
Low Stock Alerts: 1 medication(s) need restocking.
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

**Format**: `dispense INDEX /q QUANTITY [/c CUSTOMER_INDEX]`

- Dispensing fails if the requested quantity exceeds the current stock.
- Dispensing is blocked for expired medications. A warning is shown and stock remains unchanged.
- `/c CUSTOMER_INDEX` is optional. If omitted, no customer record is updated.
- If `/c CUSTOMER_INDEX` is provided but out of range, an error is shown and
  stock remains unchanged.
- If the linked customer has a recorded allergy that matches the medication name,
  **dispensing is aborted** and a warning is shown. Stock is not decremented.

**Example — no customer linked:**

`dispense 2 /q 10`

```
Dispensing successfully!
Medication: Ibuprofen
Amount: 10 units
Updated Stock: 40 units
```

**Example — linked to customer:**

`dispense 1 /q 20 /c 1`

```
Dispensing successfully!
Medication: Paracetamol
Amount: 20 units
Updated Stock: 110 units
Recorded for customer: [C001] John Tan.
```

**Example — allergy conflict detected:**

`dispense 1 /q 5 /c 1` *(where customer C001 is allergic to penicillin and medication 1 is Penicillin V)*

```
WARNING: Allergy conflict detected!
Customer "Alice Tan" has a recorded allergy to "penicillin".
Dispense aborted. Please verify with a pharmacist before proceeding.
```

**Example — expired medication blocked:**

`dispense 3 /q 10`

```
Cannot dispense expired medication: Amoxicillin (Expired on 2024-10-01).
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
- `expiring /days 0` — lists only medications expiring exactly today

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

**Format:** `add-customer /id CUSTOMER_ID /n NAME /p PHONE [/addr ADDRESS] [/allergy ALLERGY1,ALLERGY2,...]`

**Mandatory Parameters:**
* `/id CUSTOMER_ID`: A unique identifier for the customer (e.g., `C001`).
* `/n NAME`: The full name of the customer.
* `/p PHONE`: The customer's contact number.

**Optional Parameters:**
* `/addr ADDRESS`: The customer's residential or mailing address.
* `/allergy ALLERGY1,ALLERGY2,...`: A comma-separated list of known allergens (e.g. `penicillin,aspirin`). Stored in lowercase and checked against medication names during dispensing.

**Examples:**
* `add-customer /id C001 /n John Doe /p 98765432`
* `add-customer /id C002 /n Jane Smith /p 91234567 /addr 123 Clementi Road, #04-56`
* `add-customer /id C003 /n Alice Tan /p 91234567 /allergy penicillin,aspirin`
* `add-customer /id C004 /n Bob Lim /p 87654321 /addr 10 Orchard Road /allergy sulfonamide`

**Example Output:**

```
____________________________________________________________
You have added the following customer:
  [C002] Jane Smith | Phone: 91234567 | Address: 123 Clementi Road, #04-56
You now have 9 customers in your database!
____________________________________________________________
```

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

**Example Output:**
```
____________________________________________________________
You have removed the following customer:
  [C005] Invalid Ian | Phone: 91234567 | Address: 10 Test Street | Allergies: |
You now have 8 customers in your database!
____________________________________________________________
```

---

### List all customers: `list-customers`

Displays a numbered list of all customers currently registered in the system,
showing their customer ID, name, and phone number. If available, each entry
also includes the customer's address and recorded allergies.

**Format**: `list-customers`

**Example — 4 customers registered:**

```
PharmaTracker Customers:
1. [C001] John Tan | Phone: 99887766
2. [C002] Mary Tan | Phone: 87654321 | Address: 10 Orchard Road
3. [C003] David Ng | Phone: 93456789 | Allergies: penicillin
4. [C004] El Primo | Phone: 64363459 | Address: 10 Orchard Road | Allergies: penicillin
------------------------------------------------------
Total Customers: 4.
```

**Example — no customers registered:**

```
PharmaTracker Customers:
No customers registered yet.
```

---

### Find a customer: `find-customer`

Searches the customer list for customers whose names contain the given keyword.
The search is case-insensitive and supports partial matches.

**Format:** `find-customer KEYWORD`

**Rules & Constraints:**
* `KEYWORD` is matched against each customer's name using a case-insensitive partial match.
* A keyword like `tan` will return all customers whose name contains "tan" (e.g., `John Tan`, `Mary Tan`).
* If no customers match, a message is shown and no list is displayed.
* `KEYWORD` cannot be empty. Entering `find-customer` with no keyword will display an error message and no search is performed.

**Examples:**
* `find-customer John`
* `find-customer tan`

**Example — matches found:**

```
Customers matching "tan":
1. [C001] John Tan | Phone: 99887766
2. [C002] Mary Tan | Phone: 87654321
------------------------------------------------------
2 customer(s) found.
```

**Example — no matches:**

```
No customers found matching: xyz
```

---

### View customer details: `view-customer`

Displays the full details of a specific customer, including their ID, name, phone number, address, and full dispensing history.

Format: `view-customer INDEX`

- `INDEX` must be a positive integer corresponding to a customer shown in `list-customers`.
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
Allergies:           penicillin, aspirin
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
Allergies:           None
----------------------------------------
DISPENSING HISTORY
----------------------------------------
No medications dispensed yet.
========================================
```

---

### Update a customer: `update-customer`

Updates one or more fields of an existing customer record. Only the fields you provide are changed; all others remain unchanged.

Format: `update-customer INDEX [/n NAME] [/p PHONE] [/addr ADDRESS] [/allergy ALLERGY1,ALLERGY2,...]`

- At least one of `/n`, `/p`, `/addr`, or `/allergy` must be provided.
- `INDEX` is the 1-based position of the customer as shown in `list-customers`.
- `/allergy` replaces the customer's entire allergy list with the new values provided.

Examples:
- `update-customer 1 /n Alice Tan` — updates name only
- `update-customer 2 /p 81234567 /addr 99 Clementi Ave` — updates phone and address
- `update-customer 1 /allergy penicillin,ibuprofen` — sets allergies to penicillin and ibuprofen
- `update-customer 1 /n Bob /p 98765432 /addr 5 Bukit Timah Road` — updates name, phone, and address

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

A: Copy the entire `data/` folder from your current machine to the same relative path on the
new machine before launching the application. The folder contains all persistent data files:

- `data/pharmatracker.txt` — medication inventory
- `data/customers.txt` — customer profiles and dispensing histories
- `data/dispense_log.txt` — daily dispense log records
- `data/users.txt` — registered user accounts

Copying only `data/pharmatracker.txt` will result in the loss of all customer records,
dispense logs, and user accounts.

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
| Dispense medication | `dispense INDEX /q QUANTITY [/c CUSTOMER_INDEX]` |
| Restock medication  | `restock INDEX /q QUANTITY` |
| Sort by expiry      | `sort` |
| Check expiring      | `expiring [/days DAYS]` |
| Check low stock     | `lowstock [/threshold NUMBER]` |
| Print label         | `label INDEX` |
| Daily dispense log  | `dispenselog [/date YYYY-MM-DD]` |
| Add customer        | `add-customer /id ID /n NAME /p PHONE [/addr ADDRESS] [/allergy ALLERGY,...]` |
| List customers      | `list-customers` |
| Find customer       | `find-customer KEYWORD` |
| View customer       | `view-customer INDEX` |
| Update customer     | `update-customer INDEX [/n NAME] [/p PHONE] [/addr ADDRESS] [/allergy ALLERGY,...]` |
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
