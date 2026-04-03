# Karthik Kathiresh - Project Portfolio Page

## Overview
PharmaTracker is a command-line application for pharmacy staff to manage medication inventory
and customer records. It supports adding, finding, dispensing, and restocking medications,
as well as managing customer information and tracking dispensing history. It is optimized for
fast typists who prefer a CLI workflow over GUI applications.

## Summary of Contributions

### Code Contributed
[View my code on RepoSense](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=karthikkathiresh&tabRepo=AY2526S2-CS2113-T10-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)
*(Note: Adjust the date or specific parameters in the URL to match your exact milestone timelines).*

### Enhancements Implemented

* **[Application Architecture and Base Code Setup]** (v1.0):
    * **What it does:** *Established the foundational codebase and the main execution loop required for the overall 
                         PharmaTracker application to run.*
  * **Justification:** *This was a critical initial step. By establishing the core structure early, it enabled all group members to work independently.*
  * **Technical Highlights:** *Implemented the main entry class, `PharmaTracker`. 
                               Created the `Medication` and `Inventory` classes to manage medical data. 
                               Created the `Ui` class to handle inputs and outputs. 
                               Implemented a skeleton of the `Parser` class to define the execution flow for different application commands. 
                               Configured the testing infrastructure by updating `runtest.bat` and `EXPECTED.TXT` for CI validation.*

* **[Add Medication Feature]** (v1.0):
    * **What it does:** *Allows users to log a new medication into the inventory. 
                         It captures mandatory details (name, dosage, quantity, expiry date) while also supporting 
                         a wide array of optional attributes such as dosage form, administration route, frequency, 
                         max daily dose, and multiple specific warnings.*
    * **Technical Highlights:** *Implemented `AddCommand` class which executes the overall feature. 
                                 Updated the `Parser` class to handle variable-length inputs. 
                                 Update the `Ui` class to print a message to the user upon successful addition of the medication.
                                 The `execute` method throws a `PharmaTrackeException` to handle all possible input errors.*

* **Delete Medication Feature** (vX.X):
  * **What it does:** *Allows users to safely remove a specific medication from the inventory by referencing its 
                       1-based index as shown in the inventory list.*
  * **Technical Highlights:** *Implemented the `DeleteCommand` class which handles parsing the user input and 
                               translating the 1-based user index to a 0-based system index. Built in error handling, 
                               including boundary checks to prevent out-of-bounds deletions and try-catch blocks to catch `NumberFormatException` for non-integer inputs.*

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

* Managed releases `[vX.X]` - `[vY.Y]` on GitHub.
* Maintained the issue tracker by creating and assigning tasks for the `[vX.X]` milestone.
* *Mention any other administrative tasks, like setting up the repository or managing project boards.*

### Community & Tooling
*(Demonstrate your active participation in the broader developer community and tooling setup.)*

* **PRs Reviewed:** Reviewed PRs from teammates with non-trivial feedback (Examples: `#XX`, `#YY`, `#ZZ`).
* **Forum Discussions:** Contributed to class forum discussions or helped troubleshoot classmate issues (Examples: `[Link 1]`, `[Link 2]`).
* **Bug Reporting:** Reported bugs and provided suggestions for other teams during the practical exam / peer testing phase.
* **Tools:** Integrated `[Third-Party Library or CI/CD Plugin]` into the team repository to streamline development.