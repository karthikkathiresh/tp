# PharmaTracker

This is a project template for a greenfield Java project. It's named after the Java mascot PharmaTracker. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 17 (use the exact version), update Intellij to the most recent version.

1. **Ensure Intellij JDK 17 is defined as an SDK**, as described [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk) -- this step is not needed if you have used JDK 17 in a previous Intellij project.
1. **Import the project _as a Gradle project_**, as described [here](https://se-education.org/guides/tutorials/intellijImportGradleProject.html).
1. **Run the application**: Locate `src/main/java/seedu/pharmatracker/PharmaTracker.java`, right-click it, and choose `Run 'PharmaTracker.main()'`. You should see output like:

   ```
   Hello from
    _____  _                               _______              _
   |  __ \| |                             |__   __|            | |
   | |__) | |__   __ _ _ __ _ __ ___   __ _  | | _ __ __ _  ___| | _____ _ __
   |  ___/| '_ \ / _` | '__| '_ ` _ \ / _` | | || '__/ _` |/ __| |/ / _ \ '__|
   | |    | | | | (_| | |  | | | | | | (_| | | || | | (_| | (__|   <  __/ |
   |_|    |_| |_|\__,_|_|  |_| |_| |_|\__,_| |_||_|  \__,_|\___|_|\_\___|_|

   ____________________________________________________________
   Welcome to Pharma Tracker!
   What can I do for you today?
   ____________________________________________________________
   Enter command:
   ```

   The application is now ready to accept commands. Type `list` to see all medications or `help` for available commands.

## Commands

All commands use a flag-based format. Here are the available commands:

### 1. Add Medication
Adds a new medication to the inventory.

```
add /n <name> /d <dosage> /q <quantity> /e <yyyy-MM-dd> /t <tag> [optional flags]
```

**Required flags:**
- `/n` - Medication name
- `/d` - Dosage (e.g., 500mg)
- `/q` - Quantity in stock
- `/e` - Expiry date (yyyy-MM-dd format)
- `/t` - Tag/Category

**Optional flags:**
- `/df` - Dosage Form (e.g., Capsule, Tablet)
- `/mfr` - Manufacturer
- `/dir` - Directions (e.g., Take after meals)
- `/freq` - Frequency (e.g., Twice daily)
- `/route` - Route of administration (e.g., Oral)
- `/max` - Max daily dose
- `/warn` - Warnings (can add multiple)

**Examples:**
```
add /n Paracetamol /d 500mg /q 100 /e 2026-12-31 /t painkiller

add /n Amoxicillin /d 250mg /q 50 /e 2026-06-01 /t antibiotic /df Capsule /mfr Pfizer /dir "Take after meals" /freq "Twice daily" /warn "May cause allergic reactions"
```

### 2. List Medications
Displays all medications currently in the inventory.

```
list
```

### 3. Sort by Expiry Date
Sorts all medications by expiry date (ascending order). Medications expiring sooner appear first.

```
sort
```

### 4. Find Medication
Searches for medications by keyword.

```
find <keyword>
```

**Examples:**
```
find Paracetamol
find antibiotic
find 500mg
```

### 5. View Medication Details
Displays complete details of a specific medication by index.

```
view <index>
```

**Example:**
```
view 1
view 3
```

### 6. Dispense Medication
Removes a quantity of medication from inventory (e.g., when dispensing to a patient).

```
dispense <index> /q <quantity>
```

**Examples:**
```
dispense 1 /q 10
dispense 2 /q 50
```

### 7. Print Medication Label
Prints a formatted medication label for the medication at the specified index, showing name, dosage, expiry date, and tag.

```
label <index>
```

**Examples:**
```
label 1
label 3
```

### 8. Help
Displays available commands.

```
help
```

### 9. Exit
Exits the application.

```
exit
```

## Usage Tips

- **Invalid Format**: If you enter a command incorrectly, PharmaTracker will display an error message with guidance
- **Error Codes**: All errors include error codes (e.g., `[INVALID_FORMAT]`) to help identify the issue
- **No Data Loss**: Even if an error occurs, the program continues running and doesn't exit
- **Expiry Dates**: Always use `yyyy-MM-dd` format for dates (e.g., 2026-12-31)
- **Special Characters**: If your input contains spaces, consider using quotes (e.g., `"Take after meals"`)

## Build automation using Gradle

- This project uses Gradle for build automation and dependency management. It includes a basic build script as well (i.e. the `build.gradle` file).
- If you are new to Gradle, refer to the [Gradle Tutorial at se-education.org/guides](https://se-education.org/guides/tutorials/gradle.html).

## Testing

### I/O redirection tests

- To run _I/O redirection_ tests (aka _Text UI tests_), navigate to the `text-ui-test` and run the `runtest(.bat/.sh)` script.

### JUnit tests

- A skeleton JUnit test (`src/test/java/seedu/duke/DukeTest.java`) is provided with this project template.
- If you are new to JUnit, refer to the [JUnit Tutorial at se-education.org/guides](https://se-education.org/guides/tutorials/junit.html).

## Checkstyle

- A sample CheckStyle rule configuration is provided in this project.
- If you are new to Checkstyle, refer to the [Checkstyle Tutorial at se-education.org/guides](https://se-education.org/guides/tutorials/checkstyle.html).

## CI using GitHub Actions

The project uses [GitHub actions](https://github.com/features/actions) for CI. When you push a commit to this repo or PR against it, GitHub actions will run automatically to build and verify the code as updated by the commit/PR.

## Documentation

`/docs` folder contains a skeleton version of the project documentation.

Steps for publishing documentation to the public:

1. If you are using this project template for an individual project, go your fork on GitHub.<br>
   If you are using this project template for a team project, go to the team fork on GitHub.
1. Click on the `settings` tab.
1. Scroll down to the `GitHub Pages` section.
1. Set the `source` as `master branch /docs folder`.
1. Optionally, use the `choose a theme` button to choose a theme for your documentation.
