# Project Portfolio Page: Shamit (@ShamitGupta)

## Overview
PharmaTracker is a Java-based CLI application designed for pharmacy professionals to manage medication inventory and patient records with high precision and speed. The system enables users to track stock, monitor expiry dates, and maintain detailed clinical histories for individual customers through efficient, flag-based command inputs.

---

## Summary of Contributions

### Code Contributed
[View my full code contributions on the PharmaTracker Dashboard](https://github.com/ShamitGupta/tp)

### Enhancements Implemented

#### 1. Customer Data Infrastructure (Backend)
I designed and implemented the core data layer for the patient management system, creating the foundation for all v2.0 features.
* **Customer Model**: Developed the `Customer` class to encapsulate patient identities (ID, Name, Phone, Address) and a dynamic `ArrayList<String>` for dispensing histories.
* **CustomerList Manager**: Created the `CustomerList` wrapper to provide a standardized internal API for adding, deleting, and searching patient records, ensuring data encapsulation and validation.

#### 2. Persistence Layer for Patient Records
I engineered the serialization logic within the `Storage` class to ensure customer data persists across application sessions.
* **Data Serialization**: Implemented `saveCustomers` and `loadCustomers` methods using a pipe-delimited format (`|`) stored in `data/customers.txt`.
* **History Handling**: Developed a specialized logic using a semicolon-separator (`;`) to collapse multiple dispensing history entries into a single string segment, preventing file corruption when patients have extensive medication records.

#### 3. Architectural Integration (Integration Glue)
I implemented the system "glue" that allows medication and customer datasets to interact seamlessly.
* **Boot Sequence**: Modified the `PharmaTracker` constructor to ensure the `CustomerList` is instantiated and loaded alongside the medication inventory.
* **Parameter Injection**: Refactored the `Command.execute()` method signature and the main loop to inject `CustomerList` into every command execution. This enables atomic operations where inventory reduction and clinical record creation happen in a single, inseparable step.

---

## Implementation Detail: Customer Backend and Persistence

### Description
The customer management enhancement transitioned PharmaTracker from a simple drug tracker into a relational management tool. By building a dedicated backend infrastructure early, I provided a stable set of methods for my teammates to build features like patient viewing and dispensing history tracking.

### Implementation Logic
The system uses the `CustomerList` as a high-level data repository. When a transaction occurs, the system utilizes the methods I provided to update the relevant `Customer` object's internal list. For persistence, the `Storage` class iterates through the `CustomerList`, converting object attributes and the list of history strings into a single, parseable line for the text file.

### Rationale
* **Encapsulation**: Separating the model (`Customer`) from the collection (`CustomerList`) follows the Single Responsibility Principle, making the code more maintainable.
* **Atomicity**: Passing both the inventory and customer list to commands ensures that a mistake in input fails the entire operation, preventing "ghost" transactions where inventory is deducted but no history is recorded.

### Alternatives Considered
* **Unified Storage**: I considered storing customers inside the `Inventory` class. This was rejected as a "God Class" anti-pattern; keeping entities separate improves modularity and testing.
* **Stateless File Access**: I considered writing to disk directly without an in-memory model. This was rejected because searching a large patient database would have become prohibitively slow in a high-speed CLI environment.

---

## Technical Documentation Contributions

### User Guide
* **Logic Audits**: I performed a comprehensive audit of the manual to ensure command formats matched the source code.
* **Critical Fixes**: I identified and corrected a discrepancy where the command to close the application was listed as `bye` in the manual but implemented as `exit` in the code.

### Developer Guide
* **Architectural Walkthroughs**: I authored the sections detailing the **Customer Data Layer Infrastructure** and the **Integration Glue**.
* **Visual Documentation**: I designed and integrated the PUML class diagrams visualizing the relationship between the list manager and the data model.

---