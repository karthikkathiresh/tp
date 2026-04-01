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

#### 1. Dispense with Customer Linking (`dispense INDEX q/QUANTITY [c/CUSTOMER_INDEX]`)

Extended the existing `dispense` command with an optional `c/CUSTOMER_INDEX` flag.
When provided, the dispensed medication is automatically recorded in that customer's
dispensing history. If omitted, the command behaves identically to the original.

This required:
- Adding an overloaded constructor to `DispenseCommand` using a sentinel value
  (`NO_CUSTOMER = -1`) to distinguish linked and unlinked dispenses without nullable
  primitives or autoboxing overhead.
- Implementing a pre-decrement validation sequence: medication index → stock sufficiency →
  customer index. This ordering ensures no state is modified if any validation fails,
  preserving atomicity between the stock update and the customer history write.
- Updating `Parser` to detect and extract the optional `c/` flag.
- Writing 4 new JUnit tests covering customer linking, confirmation output, invalid customer
  index rollback, and the no-customer baseline (10 tests total, all passing).

#### 2. List Customers (`listcustomers`)

Implemented a read-only command that displays all registered customers with their customer
ID, name, and phone number, along with a total count. Handles the empty-list case with a
clear user-facing message.

#### 3. Restock Medication (`restock INDEX /q QUANTITY`)

Implemented an additive restock command that tops up an existing medication's stock without
overwriting it. Distinct from `update` by design — prevents accidental stock wipes during
routine restocking workflows. Validates both index and quantity before modifying any data.

---

### Contributions to the User Guide

- `dispense` command section — extended format, both examples (with and without `c/` flag),
  expected output blocks, and error behaviour description
- `listcustomers` command section — format, both examples (with and without customers),
  expected output blocks
- `restock` command section — format, both examples, expected output blocks
- Updated Command Summary table with all three commands

---

### Contributions to the Developer Guide

- **List Customers Feature** — full How It Works walkthrough, sequence diagram
  (`ListCustomersCommandSequence.puml`), and Design Considerations table
- **Restock Medication Feature** — full How It Works walkthrough, sequence diagram
  (`RestockCommandSequence.puml`), and Design Considerations table
- **Dispense with Customer Linking Feature** — full How It Works walkthrough, sequence
  diagram (`DispenseCommandSequence.puml`), and Design Considerations table (5 entries
  covering flag design, sentinel value rationale, pre-decrement guard, record storage
  location, and constructor overloading strategy)

UML diagrams added:
- `docs/diagrams/ListCustomersCommandSequence.puml`
- `docs/diagrams/RestockCommandSequence.puml`
- `docs/diagrams/DispenseCommandSequence.puml`

---

### Contributions to Team-Based Tasks

- Maintained `DispenseCommand.java` as the feature evolved across multiple iterations,
  keeping Javadoc consistent with team conventions
- Ensured backward compatibility of the `dispense` command so existing behaviour and
  existing tests were unaffected by the new `c/` flag

---

### Review / Mentoring Contributions

- Reviewed PRs related to customer feature integration
- Assisted teammates with understanding the `Parser` flag extraction pattern when
  implementing their own optional flags

---

## Contributions to the Developer Guide (Extracts)

### Dispense with Customer Linking Feature

Extends the existing `dispense` command with an optional `c/CUSTOMER_INDEX` flag. When the
flag is provided, the dispensed medication is recorded in that customer's dispensing history.
Omitting `c/` retains the original behaviour exactly.

`dispense INDEX q/QUANTITY [c/CUSTOMER_INDEX]`

**How it works:**

1. The user enters `dispense 1 q/20 c/1`.
2. `Parser.parse()` extracts the medication index, `q/` quantity, and optional `c/` customer index.
3. A `DispenseCommand` is constructed via the 3-arg constructor; the 2-arg constructor
   delegates to it with `NO_CUSTOMER = -1`.
4. `execute()` validates medication index → stock sufficiency → customer index, in that order.
   No state is modified until all three pass.
5. Stock is decremented, then the dispense record is appended to the customer's history.

**Design Considerations:**

| Aspect | Choice | Reason |
|--------|--------|--------|
| Optional flag vs separate command | Optional flag | Backward compatible; no duplication of stock logic |
| Sentinel `NO_CUSTOMER = -1` | `int` sentinel | Avoids autoboxing; self-documenting named constant |
| Customer index validated before stock decrement | Pre-decrement guard | Ensures atomicity — no partial state on failure |
| Record on `Customer`, not `Medication` | `Customer` | Natural query is per-customer history |
| Two constructors | Overloaded | Clean call sites; 2-arg delegates to 3-arg |

---

## Contributions to the User Guide (Extracts)

### Dispense a medication: `dispense`

Reduces the stock of a medication by the specified quantity. Optionally links the dispense
event to a registered customer, recording it in their dispensing history.

Format: `dispense INDEX q/QUANTITY [c/CUSTOMER_INDEX]`

- Dispensing fails if the requested quantity exceeds current stock.
- `c/CUSTOMER_INDEX` is optional. If omitted, no customer record is updated.
- If `c/CUSTOMER_INDEX` is out of range, an error is shown and stock is unchanged.

**Example — no customer linked:** `dispense 2 q/10`

```text
Dispensing successfully!
Medication: Ibuprofen
Amount: 10 units
Updated Stock: 40 units
```

**Example — linked to customer:** `dispense 1 q/20 c/1`

```text
Dispensing successfully!
Medication: Paracetamol
Amount: 20 units
Updated Stock: 110 units
Recorded for customer: [C001] John Tan.
```
