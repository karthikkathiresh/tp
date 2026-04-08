# Karthik Kathiresh - Project Portfolio Page

## Overview
PharmaTracker is a command-line application for pharmacy staff to manage medication inventory
and customer records. It supports adding, finding, dispensing, and restocking medications,
as well as managing customer information and tracking dispensing history. It is optimized for
fast typists who prefer a CLI workflow over GUI applications.

## Summary of Contributions

### Code Contributed: [View my code on RepoSense](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=karthikkathiresh&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=)

### Enhancements Implemented
- **Application Architecture and Base Code Setup** (v1.0, [PR #21](https://github.com/AY2526S2-CS2113-T10-3/tp/pull/21)): Set up the foundational codebase and main execution loop for the `PharmaTracker` application. This involved implementing the main `PharmaTracker` entry class, the `Medication` and `Inventory` classes to manage data, the `Ui` class for input-output handling, and a skeleton for the `Parser` class. This is extremely important so that all team members could begin working on their features independently.
- **Add Medication Feature** (v1.0, [PR #54](https://github.com/AY2526S2-CS2113-T10-3/tp/pull/54)): Allows users to log a new medication into the inventory. Captures mandatory details and optional attributes. This feature is the foundation of populating the inventory with medications, and for enabling other features. 
    - **Technical Highlights**: Implemented `AddCommand` which executes this feature. Updated `Parser` to handle variable-length inputs, and `Ui` to print a confirmation message to the user. 
- **Delete Medication Feature** (v1.0, [PR #47](https://github.com/AY2526S2-CS2113-T10-3/tp/pull/47)): Allows users to remove a medication from the inventory by referencing its 1-based index in the inventory list. This feature allows users to remove items they deem not required anymore. 
    - **Technical Highlights**: Implemented `DeleteCommand` which handles input parsing and translating the 1-based user index to a 0-based system index. 
- **Update Medication Feature** (v2.0, [PR #105](https://github.com/AY2526S2-CS2113-T10-3/tp/pull/105)): Allows users to selectively modify specific fields of an existing medication record. This is essential for maintaining accurate data without having to delete and re-add items. 
    - **Technical Highlights**: Implemented `UpdateCommand` to accept an index alongside any combination of optional update flags. Enhanced `Parser` to flexibly extract these parameters. 
- **Add Customer Feature** (v2.0, [PR #89](https://github.com/AY2526S2-CS2113-T10-3/tp/pull/89)): Registers a new customer profile into the system. This expands the application's scope to a holistic pharmacy management tool, laying the foundation for customer-linked dispensing features.
    - **Technical Highlights**: Implemented `AddCustomerCommand` and updated `Parser` with dedicated extraction methods to parse user inputs safely.
- **Delete Customer Feature** (v2.0): Enables users to permanently remove a customer from the database by referencing their 1-based index in the customer list.
    - **Technical Highlights**: Implemented `DeleteCustomerCommand` which has been interfaced with `CustomerList` and `Ui` securely. 



### Contributions to Testing
*(List the specific test classes you wrote and the edge cases you covered. This proves your commitment to software quality.)*

* **`[ClassName]Test`**: *Detail the methods tested, focusing on edge cases (e.g., testing for valid/invalid indices, empty lists, malformed data files).*
* **`[AnotherClass]Test`**: *Mention parser-level tests, boundary values, or integration tests you authored.*

### Contributions to the User Guide (UG)
*(Specify exactly which sections of the user-facing documentation you authored or updated.)*

* Added documentation for the `[command 1]` and `[command 2]` features, including expected formats, examples, and error cases.
* Authored the section explaining [e.g., auto-save functionality, startup behavior, etc.].
* *List any other cosmetic tweaks, formatting, or summary tables you contributed to.*

### Contributions to the Developer Guide (DG)
*(Showcase your software architecture knowledge and design thinking.)*

* **Architecture:** *Explain which parts of the overarching system design you contributed to or documented.*
* **Implementation Details:** *Detailed the design considerations and execution flow for [Feature Name]. Explain why you chose this specific implementation over alternatives.*
* **UML Diagrams:** * Created the sequence diagram for the `[Command Name]` execution flow.
    * Designed the class diagram illustrating the relationship between `[Component A]` and `[Component B]`.
* **User Stories / Use Cases:** *List the specific user stories or manual testing instructions you added to the appendix.*

### Project Management & Team-Based Tasks
*(Document your roles in managing the repository and ensuring smooth team operations for CS2113.)*

* Managed releases `[v1.0]` - `[v2.0]` on GitHub.
* Maintained the issue tracker by creating and assigning tasks for the `[v1.0]` milestone.
* *Mention any other administrative tasks, like setting up the repository or managing project boards.*

### Community & Tooling
*(Demonstrate your active participation in the broader developer community and tooling setup.)*

* **PRs Reviewed:** Reviewed PRs from teammates with non-trivial feedback (Examples: `#XX`, `#YY`, `#ZZ`).
* **Forum Discussions:** Contributed to class forum discussions or helped troubleshoot classmate issues (Examples: `[Link 1]`, `[Link 2]`).
* **Bug Reporting:** Reported bugs and provided suggestions for other teams during the practical exam / peer testing phase.
* **Tools:** Integrated `[Third-Party Library or CI/CD Plugin]` into the team repository to streamline development.