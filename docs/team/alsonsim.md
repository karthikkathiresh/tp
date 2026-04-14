# Alson Sim - Project Portfolio Page

## Overview

PharmaTracker is a command-line application for pharmacy staff to manage medication inventory
and customer records. It supports adding, finding, dispensing, and restocking medications,
as well as managing customer information and tracking dispensing history. It is optimised for
fast typists who prefer a CLI workflow over GUI applications.

---

## Summary of Contributions

### Code Contributed

[View my code on the tP Code Dashboard](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=alson&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other)

---

### Enhancements Implemented

#### 1. Dispense with Customer Linking (`dispense`)

Extended the existing `dispense` command with an optional `/c CUSTOMER_INDEX` flag. When provided, the dispensed medication is automatically recorded in that customer's dispensing history. If omitted, the command behaves identically to the original.

**Technical Implementation:**
- Added an overloaded constructor to `DispenseCommand` using a sentinel value (`NO_CUSTOMER = -1`) to distinguish linked and unlinked dispenses without nullable primitives or autoboxing overhead.
- Implemented a pre-decrement validation guard sequence (quantity, inventory state, medication index, expiry, stock sufficiency, customer index, and allergy conflict). This ordering ensures no state is modified if any validation fails, preserving atomicity between the stock update and the customer history write.
- Updated `Parser` to detect and extract the optional `/c` flag.
- Wrote 4 new JUnit tests covering customer linking, confirmation output, invalid customer index rollback, and the no-customer baseline (10 tests total, all passing).

#### 2. List Inventory (`list`)

Implemented the core inventory overview command that provides a high-level summary of all medications.

**Key Features:**
- **Visual Cues**: Automatically flags items with a quantity of less than 20 as `[LOW STOCK]` to provide immediate visual priority for replenishment (consistent with the default threshold for the `lowstock` command).
- **Reference Point**: Displays 1-based indices required for all other medication-based commands like `delete`, `view`, and `dispense`.

#### 3. List Customers (`list-customers`)

Implemented a read-only command that displays all registered customers with their customer ID, name, and phone number, along with a total count. Handles the empty-list case with a clear user-facing message.

#### 4. Restock Medication (`restock`)

Implemented an additive restock command that tops up an existing medication's stock without overwriting it. 

**Design Rationale:**
- Distinct from `update` by design — prevents accidental stock wipes during routine restocking workflows.
- Validates both index and quantity before modifying any data.

#### 5. Auto Restock Alert System Improvements

Implemented two critical bug fixes to ensure the auto restock alert feature behaves correctly in edge cases:

**Bug Fix 1: Suppress Alerts When Inventory is Empty**
- **Issue**: Auto restock alert summaries were displayed even when the inventory was empty, showing alerts for non-existent medications and confusing users.
- **Solution**: Added a guard based on `!inventory.getMedications().isEmpty()` (equivalently, checking the medication list size) before displaying auto-generated alert summaries in `PharmaTracker.run()`. This prevents noise and improves user experience.
- **Technical Details**: Both alert display points (startup restoration and post-ListCommand) now validate that `inventory.getMedications()` is not empty before showing the summary.

**Bug Fix 2: Auto-Cleanup of Orphaned Alerts on Medication Deletion**
- **Issue**: When users deleted medications, alerts for those medications persisted in the active alerts list, creating ghost alerts.
- **Solution**: Added a `cleanupOrphanedAlerts()` method to `RestockAlertService` that runs during `evaluateInventory()`. This method identifies alerts whose medications no longer exist in the inventory and automatically removes them, marking them as acknowledged with reason "Auto-resolved: medication deleted from inventory."
- **Technical Details**: 
  - Iterates through active alerts and checks if each medication key matches any current medication.
  - Builds a list of orphaned alert keys and removes them atomically.
  - Preserves deleted alert data in the alert history for audit purposes.

#### 6. Help and Exit Command Implementation (`help`, `exit`)

Implemented and refined `help` and `exit` command behavior, including follow-up fixes and consistency updates with command handling and output flow.

**Scope:**
- Added and updated command logic for help/exit workflows.
- Synced command behavior with documentation and sequence diagrams (`HelpCommandSequence.puml`, `ExitCommandSequence.puml`).

#### 7. Storage Integration and Refactoring (Partial)

Contributed to storage integration work (partial ownership), including package-level refactoring and reliability updates tied to load/save operations.

**Scope:**
- Contributed to storage structure updates and related fixes.
- Documented lifecycle interactions across inventory, customers, auth/session, alerts, and dispense log via `StorageSequence.puml`.

#### 8. Authentication Flow and Access-Control Hardening

Improved authentication behavior by tightening command access control and ensuring protected-command checks are enforced earlier in the command pipeline.

**Scope:**
- Strengthened auth-gating behavior for protected commands.
- Added/updated documentation and sequence flow coverage (`AuthenticationSequence.puml`).

#### 9. Dispense and Parser Robustness Improvements

Extended follow-up robustness work for dispense-related parsing and execution beyond the initial `/c` feature.

**Scope:**
- Standardized flag behavior (`/q`, `/c`) and added parser regression coverage.
- Rejected non-positive customer indices in parser validation.
- Added safeguards such as blocking dispense of expired medication and improved dispense history output details.

#### 10. Restock and Inventory Consistency Fixes

Improved restock and inventory consistency behavior through defensive checks and cross-command alignment fixes.

**Scope:**
- Added boundary handling for restock quantity overflow.
- Aligned low-stock threshold behavior consistently across relevant commands.

---

### Contributions to the User Guide

| Section | Contribution |
|---------|-------------|
| `list` command | Format, descriptive list of fields (name, dosage, quantity, expiry), and example output with low-stock indicators |
| `list-customers` command | Format, both examples (with and without customers), and expected output blocks |
| `restock` command | Format, both examples, and expected output blocks |
| `dispense` command | Extended format with optional `/c` flag, both examples (with and without customer linking), expected output blocks, and error behaviour description |
| Authentication commands | Updated UG content for `register`, `login`, and `logout` flows and command usage consistency |
| Auto restock alert commands | Added and refined UG documentation for `alerts`, `ack-alert`, and `alert-history` command behavior |
| Help and command-reference fixes | Corrected syntax/help references and aligned command naming/flags with runtime behavior |
| Output-format alignment updates | Synced UG examples for `list` and `list-customers` with actual displayed output fields/format |
| Command Summary table | Updated with all implemented commands |

---

### Contributions to the Developer Guide

| Feature | Contribution |
|---------|-------------|
| **List Medication** | Format, "How It Works" walkthrough, sequence diagram (`ListCommandSequence.puml`), and Design Considerations table |
| **List Customers** | Full "How It Works" walkthrough, sequence diagram (`ListCustomersCommandSequence.puml`), and Design Considerations table |
| **Restock Medication** | Full "How It Works" walkthrough, sequence diagram (`RestockCommandSequence.puml`), and Design Considerations table |
| **Dispense with Customer Linking** | Full "How It Works" walkthrough, sequence diagram (`DispenseCommandSequence.puml`), and Design Considerations table (5 entries covering flag design, sentinel value rationale, pre-decrement guard, record storage location, and constructor overloading strategy) |
| **Authentication** | Documented register/login/logout execution flow and added sequence diagram (`AuthenticationSequence.puml`) |
| **Auto Restock Alerts** | Documented alert lifecycle edge cases (empty-inventory suppression + orphaned-alert cleanup) and added sequence diagram (`AutoRestockAlertSequence.puml`) |
| **Help and Exit** | Added sequence diagrams for `help` and `exit` flows (`HelpCommandSequence.puml`, `ExitCommandSequence.puml`) |
| **Storage (Partial)** | Contributed partial storage integration/refactoring work and documented the load/save lifecycle interactions across inventory, customers, auth/session, alerts, and dispense log (`StorageSequence.puml`) |

**UML Sequence Diagrams Added/Updated:**
- `ListCommandSequence.puml`
- `ListCustomersCommandSequence.puml`
- `RestockCommandSequence.puml`
- `DispenseCommandSequence.puml`
- `AuthenticationSequence.puml`
- `AutoRestockAlertSequence.puml`
- `HelpCommandSequence.puml`
- `ExitCommandSequence.puml`
- `StorageSequence.puml`

---

### Contributions to Team-Based Tasks

- Maintained `DispenseCommand.java` as the feature evolved across multiple iterations, keeping Javadoc consistent with team conventions.
- Ensured backward compatibility of the `dispense` command so existing behaviour and existing tests were unaffected by the new `/c` flag.
- Contributed implementation and follow-up hardening work for help/exit, partial storage integration, authentication gating, dispense/parser robustness, and restock boundary handling; also produced supporting DG/UG/diagram updates.

### Review / Mentoring Contributions

- Reviewed PRs related to customer feature integration.
- Assisted teammates with understanding the `Parser` flag extraction pattern when implementing their own optional flags.

---

## Contributions to the Developer Guide (Extracts)

### List Customers Feature

The `list-customers` command retrieves and displays all registered customers with their customer ID, name, and phone number. It is a read-only command that requires no arguments.

**Format:** `list-customers`

#### How it works

1. The user enters `list-customers`.
2. `PharmaTracker.run()` reads the input and passes it to `Parser.parse()`.
3. `Parser.parse()` identifies the command word `list-customers` and returns a new `ListCustomersCommand` object — no arguments are required.
4. `PharmaTracker.run()` calls `ListCustomersCommand.execute()`, which delegates display to `Ui.printCustomerList(customerList)`.
5. `Ui.printCustomerList(...)` retrieves customers and handles both paths:
   - If the list is empty, `"No customers registered yet."` is printed.
   - Otherwise, each `Customer` is printed with a 1-based index, their customer ID, name, and phone number, followed by a total count.

![Sequence diagram showing the execution flow of the List Customers Command](../images/ListCustomersCommandSequence.png)

#### Design Considerations

| Aspect | Choice | Reason |
|--------|--------|--------|
| Formatting location | `ListCustomersCommand`, not `Customer.toString()` | Decouples display format from the model; output format can be changed without touching the data class |
| No arguments | None | Read-only query; the entire customer list is always shown |
| Empty-list guard in `execute()` | Yes | Produces a clear user-facing message rather than displaying an empty list silently |

---

### Restock Medication Feature

The `restock` command **additively** increases the stock of an existing medication by a specified quantity. Unlike `update`, which overwrites the quantity field entirely, `restock` tops up on top of the current stock level — matching the real-world behaviour of receiving a new shipment.

**Format:** `restock INDEX /q QUANTITY`

#### How it works

1. The user enters `restock 1 /q 50`.
2. `PharmaTracker.run()` passes the raw string to `Parser.parse()`.
3. `Parser.parse()` identifies the command word `restock`, then:
   - Extracts the integer index from the leading portion of the description.
   - Locates the `/q` flag and parses its value as a positive integer.
4. A new `RestockCommand(index, quantity)` object is constructed.
5. `PharmaTracker.run()` calls `RestockCommand.execute()`, which validates the index:
   - If the index is less than 1 or exceeds the inventory size, an error message is printed and the command returns early.
   - If the quantity is not a positive integer, an error message is printed and the command returns early.
6. For a valid index, `Inventory.getMedication(index - 1)` retrieves the target `Medication`.
7. `medication.setQuantity(medication.getQuantity() + quantity)` increments the existing stock.
8. `Ui` prints the confirmation message showing the medication name, units added, and updated stock total.

![Sequence diagram showing the execution flow of the Restock Command](../images/RestockCommandSequence.png)

#### Design Considerations

| Aspect | Choice | Reason |
|--------|--------|--------|
| Additive, not overwrite | Additive | Matches real-world shipment top-up behaviour; prevents accidental stock wipes |
| Separate from `update` | Yes | Makes intent explicit; avoids relying on users to do mental arithmetic before entering a quantity |
| Quantity validated as positive | Yes | A zero or negative restock quantity is semantically meaningless and is rejected early |
| Index validation in `execute()` | `execute()` | Index validity depends on the live `Inventory` size, which the stateless `Parser` does not hold |

---

### Dispense with Customer Linking Feature

Extends the existing `dispense` command with an optional `/c CUSTOMER_INDEX` flag. When the flag is provided, the dispensed medication is recorded in that customer's dispensing history. Omitting `/c` retains the original behaviour exactly.

**Format:** `dispense INDEX /q QUANTITY [/c CUSTOMER_INDEX]`

#### How it works

1. The user enters `dispense 1 /q 20 /c 1`.
2. `Parser.parse()` extracts the medication index, `/q` quantity, and optional `/c` customer index.
3. A `DispenseCommand` is constructed via the 3-arg constructor; the 2-arg constructor delegates to it with `NO_CUSTOMER = -1`.
4. `execute()` validates medication index → stock sufficiency → customer index, in that order. No state is modified until all three pass.
5. Stock is decremented, then the dispense record is appended to the customer's history.

#### Design Considerations

| Aspect | Choice | Reason |
|--------|--------|--------|
| Optional flag vs separate command | Optional flag | Backward compatible; no duplication of stock logic |
| Sentinel `NO_CUSTOMER = -1` | `int` sentinel | Avoids autoboxing; self-documenting named constant |
| Customer index validated before stock decrement | Pre-decrement guard | Ensures atomicity — no partial state on failure |
| Record on `Customer`, not `Medication` | `Customer` | Natural query is per-customer history |
| Two constructors | Overloaded | Clean call sites; 2-arg delegates to 3-arg |

---

### List Medication Feature

The list feature provides a summary view of the entire inventory, allowing users to identify item indices for subsequent operations.

**Format:** `list`

#### How it works

1. `Parser.parse()` identifies the list command word and returns a `ListCommand` object.
2. `ListCommand.execute()` retrieves all medications from the `Inventory`.
3. The command iterates through the collection, appending `[LOW STOCK]` if quantity < 20.
4. The formatted list is passed to the `Ui` for display, followed by a total medication count.

#### Design Considerations

| Aspect | Choice | Reason |
|--------|--------|--------|
| Information Density | High-level summary | Keeps output scannable; users use view for full drug details. |
| Stock Warning | Hardcoded threshold | Provides immediate visual cues for items needing replenishment. |
| Index Alignment | 1-based numbering | Ensures the index shown matches input requirements for other commands. |

---

## Contributions to the User Guide (Extracts)

### List all medications: `list`

Displays a high-level summary of all medications currently stored in the system.

**Format:** `list`

**Features:**
- Displays a numbered list including name, dosage, current quantity, and expiry date.
- Items with quantity less than 20 are flagged `[LOW STOCK]`.

**Example:**

```text
PharmaTracker Inventory:
1. Amoxicillin | 250mg | Qty: 50  | Expiry: 01/06/2025
2. Ibuprofen   | 200mg | Qty: 5   | Expiry: 15/06/2026 [LOW STOCK]
3. Paracetamol | 500mg | Qty: 150 | Expiry: 31/12/2026
------------------------------------------------------
Total Medications: 3
```

---

### List all customers: `list-customers`

Displays a numbered list of all customers currently registered in the system, showing their customer ID, name, and phone number.

**Format:** `list-customers`

**Example with customers:**

```text
PharmaTracker Customers:
1. [C001] John Tan | Phone: 99887766
2. [C002] Mary Tan | Phone: 87654321
3. [C003] David Ng | Phone: 93456789
------------------------------------------------------
Total Customers: 3.
```

**Example with no customers:**

```text
PharmaTracker Customers:
No customers registered yet.
```

---

### Restock a medication: `restock`

Additively increases the stock of an existing medication. Unlike `update`, which overwrites the quantity, `restock` tops up on top of the current stock level. Useful when a new shipment of medication arrives.

**Format:** `restock INDEX /q QUANTITY`

**Parameters:**
- `INDEX` must be a positive integer corresponding to a medication shown in `list`.
- `QUANTITY` must be a positive integer.

**Example 1:** Adding 50 units to Paracetamol

Command: `restock 1 /q 50`

```text
Restocked successfully!
Medication: Paracetamol | Added: 50 units | Updated Stock: 180 units.
```

**Example 2:** Adding 100 units to Amoxicillin

Command: `restock 3 /q 100`

```text
Restocked successfully!
Medication: Amoxicillin | Added: 100 units | Updated Stock: 120 units.
```

---

### Dispense a medication: `dispense`

Reduces the stock of a medication by the specified quantity. Optionally links the dispense event to a registered customer, recording it in their dispensing history.

**Format:** `dispense INDEX /q QUANTITY [/c CUSTOMER_INDEX]`

**Behaviour:**
- Dispensing fails if the requested quantity exceeds current stock.
- `/c CUSTOMER_INDEX` is optional. If omitted, no customer record is updated.
- If `/c CUSTOMER_INDEX` is out of range, an error is shown and stock is unchanged.

Note: The following output snippets are simplified excerpts. Actual CLI output may include divider lines added by `Ui.printMessage(...)`.

**Example without customer linking:**

Command: `dispense 2 /q 10`

```text
Dispensing successfully!
Medication: Ibuprofen
Amount: 10 units
Updated Stock: 40 units
```

**Example with customer linking:**

Command: `dispense 1 /q 20 /c 1`

```text
Dispensing successfully!
Medication: Paracetamol
Amount: 20 units
Updated Stock: 110 units
Recorded for customer: [C001] John Tan.
```
