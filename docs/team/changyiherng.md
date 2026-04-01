# Project Portfolio Page — Yi Herng

## Overview

PharmaTracker is a command-line application for pharmacy staff to manage medication inventory and customer records. It supports adding, finding, dispensing, and restocking medications, as well as managing customer information and tracking dispensing history. PharmaTracker is written in Java and targets users who prefer a fast, keyboard-driven workflow.

---

## Summary of Contributions

### Code Contributed

[View my code on the tP Code Dashboard](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other)

---

### Enhancements Implemented

#### 1. `view-customer` Command
Implemented the `ViewCustomerCommand`, which retrieves and displays the full profile of a specific customer by their 1-based index. The command displays the customer's ID, name, phone number, address, and complete dispensing history.

Key implementation details:
- Two-stage input validation: first checks if the customer list is empty, then checks if the index is within the valid range. This produces clearer error messages and avoids `IndexOutOfBoundsException`.
- Delegates display to `Ui.showCustomerDetails(customer)`, consistent with the SRP enforced across the codebase.

#### 2. `expiring` Command
Implemented the `ExpiringCommand`, which scans the inventory and reports medications that have already expired and those expiring within a configurable window of days. Defaults to 30 days when no argument is provided.

Key implementation details:
- Supports two date formats (`yyyy-MM-dd` and `dd/MM/yyyy`) using a try-then-fallback parsing strategy, ensuring robustness across different input styles used elsewhere in the app.
- Medications with a null or unparseable expiry date are silently skipped rather than crashing the scan.
- Results are split into two separate lists — `expiredMeds` and `expiringMeds` — allowing `Ui.showExpiringMedications()` to present them in clearly labelled sections, giving staff immediately actionable information.

#### 3. `find` Command
Implemented the `FindCommand`, which searches the inventory for medications whose tag contains a given keyword (case-insensitive). This complements the existing `find` command which searches by name.

#### 4. `view` Command
Implemented the `ViewCommand`, which retrieves and displays the full details of a specific medication in the inventory by its 1-based index.

Key implementation details:
- Two-stage input validation: first checks if the inventory is empty, then checks if the index is within the valid range, producing a clear error message at each stage.
- Delegates display to `Ui.printMedicationDetails(med)`, which formats the medication's details into three clearly labelled sections: **Medication Details** (name, strength, dosage form, manufacturer), **Dosage & Administration** (directions, frequency, route, max daily dose), and **Warnings & Precautions**. Optional fields absent at add-time are shown as `N/A`.

---

### Contributions to the User Guide

- Authored the full entries for the following commands: `view-customer`, `expiring`, `find`, and `view`.
- For each command, wrote the format description, parameter constraints, usage examples, and expected output blocks (including edge cases such as empty lists and no-results messages).
- Ensured all expected outputs match the exact formatting produced by the `Ui` class (e.g., the `========`/`--------` border style for `view-customer` and `view`, the split expired/expiring sections for `expiring`).

---

### Contributions to the Developer Guide

- Authored the full implementation sections for:
    - **View Customer Feature** — including a detailed "How it works" walkthrough, sequence diagram reference (`ViewCustomerCommandSequence.png`), and a Design Considerations table.
    - **Expiring Medications Feature** — including "How it works" covering both the no-argument and `/days` parsing branches, the skip-on-null-expiry behaviour, and the two-list split design; sequence diagram reference (`ExpiringCommandSequence.png`); and Design Considerations table.
    - **Find Medication Feature** — including "How it works", sequence diagram reference (`FindCommandSequence.png`), and Design Considerations table.
    - **View Medication Feature** — including "How it works" covering the three-branch validation in `execute()`, sequence diagram reference (`ViewCommandSequence.png`), and Design Considerations table.
- Added manual testing instructions for `view-customer`, `expiring`, `find`, and `view` in the Instructions for Manual Testing section.

---

### Contributions to Team-Based Tasks

- Maintained the `UserGuide.md` and `DeveloperGuide.md` for the features owned by other teammates, ensuring consistent formatting across all sections.
- Set up the `LoggerSetup` utility class to redirect all logging output to `pharmatracker.log`, eliminating console noise during normal operation and test runs.
- Identified and fixed the `ExitCommand` / `System.exit(0)` issue that was silently killing the JVM mid-test and blocking all subsequent test execution; replaced with an `isExit()` flag pattern.

---

## Contributions to the Developer Guide (Extracts)

### Expiring Medications Feature (extract)

> The `expiring` command scans the inventory and reports medications that have already passed their expiry date, as well as those expiring within a configurable number of days...
>
> **Step 5** — The command iterates over every `Medication`. For each one, `getExpiryDate()` is called:
> - If the expiry date is `null` or cannot be parsed, the medication is **skipped**.
> - If the expiry date is **before today**, the medication is added to `expiredMeds`.
> - If the expiry date falls **within the cutoff window**, the medication is added to `expiringMeds`.

![Sequence diagram showing the execution flow of the Expiring Command](../images/ExpiringCommandSequence.png)

### View Customer Feature (extract)

> **Step 5** — `ViewCustomerCommand.execute()` first checks `CustomerList.size()` to validate the request:
> - If the customer list is empty, `"No customers registered yet."` is printed and the command returns early.
> - If the index is out of range, an invalid-index error message is printed and the command returns early.

![Sequence diagram showing the execution flow of the View Customer Command](../images/ViewCustomerCommandSequence.png)

---

## Contributions to the User Guide (Extracts)

### `expiring` Command (extract)

> Lists all medications that have already expired, and those expiring within a specified number of days. Defaults to 30 days if no value is provided.
>
> Format: `expiring [/days DAYS]`
>
> Expected output:
> ```
> Already expired:
> 1. Aspirin | 100mg | Qty: 30 | Expiry: 01/01/2025 | Tag: painkiller
> Total: 1 medication(s) expired.
> ----------------------------------------
> Expiring within 14 days:
> 1. Ibuprofen | 200mg | Qty: 12 | Expiry: 10/04/2026 | Tag: painkiller
> Total: 1 medication(s) expiring soon.
> ```

### `view-customer` Command (extract)

> Displays the full details of a specific customer, including their ID, name, phone number, address, and full dispensing history.
>
> Format: `view-customer INDEX`
>
> Expected output:
> ```
> ========================================
> CUSTOMER DETAILS
> ========================================
> Customer ID:         C001
> Name:                John Tan
> Phone:               99887766
> Address:             10 Orchard Road
> ----------------------------------------
> DISPENSING HISTORY
> ----------------------------------------
> 1. Paracetamol | 500mg | Qty dispensed: 20 | Date: 01/04/2026
> ========================================
> ```
