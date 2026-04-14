# Project Portfolio Page — Yi Herng (@yihernggggg)

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
- Two-stage input validation in `execute()`: first checks if the customer list is empty, then
  checks if the index is within the valid range. This produces clearer error messages and avoids
  `IndexOutOfBoundsException`.
- Parser-level validation throws `PharmaTrackerException` for a non-integer index value,
  failing fast before a `ViewCustomerCommand` object is created.

#### 2. `expiring` Command
Implemented the `ExpiringCommand`, which scans the inventory and reports medications that have already expired and those expiring within a configurable window of days. Defaults to 30 days when no argument is provided.

Key implementation details:
- Medications stored with a `yyyy-MM-dd` expiry date are parsed correctly. Medications with a
  null or unparseable expiry date are silently skipped rather than crashing the scan, logged
  at WARNING level for debugging.
- `expiring /days 0` is a valid and meaningful query. When `days = 0`, `cutoff` is set to
  `today.plusDays(0)` which equals today. A medication expiring today passes the check
  `!expiryDate.isAfter(cutoff)` (since it is not after today) and does not pass
  `expiryDate.isBefore(today)` (since it is not before today), so it correctly appears in
  the "expiring soon" section rather than the "already expired" section. This edge case
  requires no special handling — the existing date arithmetic handles it naturally.
- Medications with a null or unparseable expiry date are silently skipped rather than crashing the scan.
- Results are split into two separate lists — `expiredMeds` and `expiringMeds` — allowing `Ui.showExpiringMedications()` to present them in clearly labelled sections, giving staff immediately actionable information.

#### 3. `find` Command
Implemented the `FindCommand`, which searches the inventory for medications whose **name**
contains a given keyword (case-insensitive). The search uses substring matching, so partial
keywords (e.g. `cillin` matching `Amoxicillin`) are supported.

Key implementation details:
- An empty keyword is rejected in the parser by throwing a `PharmaTrackerException` before
  a `FindCommand` object is ever created. This prevents an empty string from matching every
  medication in the inventory (since `"".contains("")` is always true in Java).
- The no-results path prints through `Ui.printMessage()` consistent with the no-direct-printing
  rule enforced across the codebase.

#### 4. `find-customer` Command
Implemented the `FindCustomerCommand`, which searches the customer list for customers whose
**name** contains a given keyword (case-insensitive). The search uses substring matching,
so partial keywords (e.g. `tan` matching `John Tan` and `Mary Tan`) are supported.

Key implementation details:
- An empty keyword is rejected in the parser by throwing a `PharmaTrackerException` before
  a `FindCustomerCommand` object is ever created. Without this guard, an empty string would
  match every customer in the list since `"".contains("")` is always true in Java, causing
  the entire customer database to be returned for an invalid query.
- Search is delegated to `CustomerList.findByName(keyword)`, which iterates over all customers
  and performs a case-insensitive `contains()` check on each name. This keeps the search logic
  in the model layer rather than the command class.
- Results are displayed via `Ui.printFindCustomerResults(keyword, matches)`, consistent with
  the no-direct-printing rule enforced across the codebase.

#### 5. `view` Command
Implemented the `ViewCommand`, which retrieves and displays the full details of a specific medication in the inventory by its 1-based index.

Key implementation details:
- Two-stage input validation in `execute()`: first checks if the inventory is empty, then checks
  if the index is within the valid range, producing a clear error message at each stage.
- Parser-level validation throws `PharmaTrackerException` for a missing index or a non-integer
  value, failing fast before a `ViewCommand` object is created. This is consistent with how
  other mandatory-argument commands handle invalid input.


---

### Contributions to Testing

Wrote JUnit tests for all owned command features, following the project's
`ByteArrayOutputStream` output-capture pattern to verify printed output without
requiring a running application.

**`FindCommandTest`** — 8 tests covering:
- Null `Inventory` and null `Ui` inputs throw `AssertionError`
- Exact keyword match, case-insensitive match, and partial suffix keyword match
  (e.g. `cillin` matching `Amoxicillin`) all return correct results
- Multiple simultaneous matches are all returned with the correct count
- No-match and empty inventory both print the not-found message correctly

**`ExpiringCommandTest`** — 16 tests covering:
- Null `Inventory` and null `Ui` inputs throw `AssertionError`
- Empty inventory produces no medication output
- Expired medications in `yyyy-MM-dd` format are detected; medications with a
  past date in an alternate format are also correctly identified as expired
- Medications expiring within the default 30-day window are detected
- Medications expiring within a custom `/days` window are included; those outside
  (e.g. expiring in 7 days with a 3-day window) are excluded
- Mixed inventory correctly separates expired and expiring-soon medications into
  two labelled sections
- All safe medications produce a no-results message
- Malformed and null expiry dates are skipped gracefully without crashing the scan,
  and remaining valid medications are still shown
- `expiring` with no argument and `expiring /days 30` produce identical output,
  confirming the default constructor matches the explicit 30-day constructor

**`ViewCommandTest`** — 12 tests covering:
- Null `Inventory` and null `Ui` inputs throw `AssertionError`
- Valid index prints all set optional fields (dosage form, manufacturer, directions,
  frequency, route, max daily dose, warnings) correctly
- Missing optional fields display as `N/A` and `None`
- Partial optional fields show set fields normally and absent ones as `N/A`
- Invalid index (too high, zero, negative) all print the appropriate error message
- Empty inventory prints the empty-inventory message
- Parser returns correct `ViewCommand` type for valid input
- Parser throws `PharmaTrackerException` for missing index (`view` with no argument)
  and for non-integer index (`view abc`), reflecting the exception-based validation
  introduced during bug fixing

**`ViewCustomerCommandTest`** — 17 tests covering:
- Null `CustomerList` and null `Ui` inputs throw `AssertionError`
- Empty customer list prints the no-customers message and does not throw
- Valid index correctly prints customer ID, name, and phone number as separate assertions
- Index 1 and index 2 in a two-customer list each resolve to the correct customer
  and do not leak the other customer's data
- Last valid index (boundary check) resolves correctly in a three-customer list
- Index zero, negative index, and index beyond list size all print the invalid-index
  message
- Out-of-bounds index does not print any customer name or ID (no partial output leakage)
- Customer with no dispensing history shows name correctly with no error
- Customer with dispensing history shows medication name, quantity, and date after a
  `DispenseCommand` is executed against that customer
- Two customers with different dispense histories show only the correct customer's
  history when viewed individually

**`FindCustomerCommandTest`** — 6 tests covering:
- Exact name match, case-insensitive match, and partial keyword match all return the
  correct customer and exclude non-matching customers
- Multiple customers matching the keyword are all displayed; non-matching customers
  are excluded
- No-match prints a message containing the search keyword
- Empty customer list prints a message containing the search keyword without crashing

---

### Contributions to the User Guide

- Authored the full entries for the following commands: `view-customer`, `expiring`, `find`, `find-customer`, and `view`.
- For each command, wrote the format description, parameter constraints, usage examples, and expected output blocks (including edge cases such as empty lists and no-results messages).
- Ensured all expected outputs match the exact formatting produced by the `Ui` class (e.g., the `========`/`--------` border style for `view-customer` and `view`, the split expired/expiring sections for `expiring`).

---

### Contributions to the Developer Guide

- Authored the full implementation sections for:
  - **View Customer Feature** — including a detailed "How it works" walkthrough, sequence
    diagram reference (`ViewCustomerCommandSequence.png`), and a Design Considerations table.
  - **Expiring Medications Feature** — including "How it works" covering both the no-argument
    and `/days` parsing branches, the skip-on-null-expiry behaviour, and the two-list split
    design; sequence diagram reference (`ExpiringCommandSequence.png`); and Design
    Considerations table.
  - **Find Medication Feature** — including "How it works", sequence diagram reference
    (`FindCommandSequence.png`), and Design Considerations table.
  - **Find Customer Feature** — including "How it works" covering the empty-keyword guard,
    the delegation to `CustomerList.findByName()`, and the two-branch result handling;
    sequence diagram reference (`FindCustomerCommandSequence.png`); and Design Considerations
    table.
  - **View Medication Feature** — including "How it works" covering the three-branch
    validation in `execute()`, sequence diagram reference (`ViewCommandSequence.png`), and
    Design Considerations table.
- Designed and authored the top-level Architecture section of the Developer Guide, including
  the component responsibility table, the Architecture Component Diagram, and the overall
  Architecture Sequence Diagram illustrating the full runtime flow from startup through the
  continuous command execution loop.
- Added manual testing instructions for `view-customer`, `expiring`, `find`, `find-customer`,
  and `view` in the Instructions for Manual Testing section.

---

### Contributions to Team-Based Tasks

- Maintained the `UserGuide.md` and `DeveloperGuide.md`, ensuring consistent formatting across all sections.
- Set up the `LoggerSetup` utility class to redirect all logging output to `pharmatracker.log`,
  eliminating console noise during normal operation and test runs.
- Identified and fixed the `ExitCommand` / `System.exit(0)` issue that was silently killing
  the JVM mid-test and blocking all subsequent test execution; replaced with an `isExit()`
  flag pattern.
- Fixed a storage corruption bug where customer dispensing history and allergies were loaded
  into the wrong columns after restart. Root cause: the dispense history record format uses
  `" | "` as internal separators, which collided with the `" | "` column delimiter used by
  `saveCustomers()`. Fixed by switching the column delimiter to `\t` (tab) in both
  `saveCustomers()` and `loadCustomers()`.
- Fixed a silent data loss bug in `loadCustomers()` where customers with no address, history,
  or allergies (trailing empty columns) were dropped on load. Root cause: `.trim()` stripped
  the trailing tab characters that represented empty columns, leaving fewer than 4 parts after
  splitting. Fixed by replacing `.trim()` with `.stripLeading()`.
- Fixed a flag collision bug where `FLAG_DOSAGE = "/d"` would match inside `FLAG_DOSAGE_FORM = 
  "/df"` when both flags appeared in the same `update` or `add` command. Fixed by redefining
  `FLAG_DOSAGE` as `"/d "` (with a trailing space).
- Fixed a warning-clear bug in `UpdateCommand` where `update INDEX /warn` with no value
  appeared to succeed but left existing warnings unchanged. Root cause: `extractWarnings()`
  returned an empty list for both "flag absent" and "flag present but empty", making the two
  states indistinguishable. Fixed by checking for flag presence in `UpdateCommandParser` before
  calling `extractWarnings()`, and changing the guard in `UpdateCommand.execute()` from
  `warnings != null && !warnings.isEmpty()` to `warnings != null`.
- Fixed a duplicate allergy entry bug where `/allergy peanuts,PEANUTS,peanuts` would store
  the same allergen three times. Fixed by adding a `!allergies.contains(trimmed)` check in
  `extractCustomerAllergies()`.
- Updated five PlantUML sequence diagrams (`ExpiringCommandSequence`, `FindCommandSequence`,
  `FindCustomerCommandSequence`, `ViewCommandSequence`, `ViewCustomerCommandSequence`) to
  reflect bug fixes, remove `System.out` as a direct participant, route all output through
  `Ui`, remove `destroy` markers, and add parser-level `alt` blocks for exception paths.

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
> - `DAYS` must be a non-negative integer. A value of `0` is valid and lists medications
    >   expiring exactly today. Negative values are rejected with a `PharmaTrackerException`.
>
> Expected output:
> ```
> ____________________________________________________________
> Already expired:
> 1. Name: Aspirin | Dosage: 100mg | Qty: 30 | Exp: 2025-01-01 | [EXPIRED] | Tag: painkiller
> Total: 1 medication(s) expired.
> ----------------------------------------
> Expiring within 14 days:
> 1. Name: Ibuprofen | Dosage: 200mg | Qty: 12 | Exp: 2026-04-10 | Tag: painkiller
> Total: 1 medication(s) expiring soon.
> ____________________________________________________________
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
> Allergies:           penicillin, aspirin
> ----------------------------------------
> DISPENSING HISTORY
> ----------------------------------------
> 1. Paracetamol | 500mg | Qty: 20
> ========================================
> ```
