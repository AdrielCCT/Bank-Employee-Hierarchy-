# Bank Employee Hierarchy

This project is a Java console application for a bank system. The program reads employee records from a text file and allows the user to sort records, search by name, add new records, and build an employee hierarchy using a binary tree.

## Features

| Menu option | What it does | Algorithm / construct |
|---|---|---|
| 1. SORT | Sorts all names from A to Z and shows the first 20 records | Recursive **Merge Sort** |
| 2. SEARCH | Finds an employee by name and shows the Manager Type and Department | Recursive **Binary Search** |
| 3. ADD RECORDS | Adds a new employee with Name, Manager Type, and Department after input validation | **Enums** and input validation |
| 4. Create a binary tree | Builds the hierarchy with level-order insertion and shows levels, height, and node count | **Binary tree** and queue (BFS) |
| 5. EXIT | Closes the program | **Enum** `MenuOption` |

## Project structure (MVC)

BankEmployeeHierarchy/
├── data/Applicants_Form.txt        <- 40 sample employees (Name,Manager Type,Department)
├── src/bank/
│   ├── Main.java                   <- starts the program
│   ├── model/                      <- MODEL: data and business rules
│   │   ├── Employee.java
│   │   ├── ManagerType.java        (enum)
│   │   ├── Department.java         (enum)
│   │   ├── EmployeeRepository.java (list in memory and file loading)
│   │   ├── LoadResult.java
│   │   └── EmployeeBinaryTree.java
│   ├── algorithm/                  <- recursive algorithms (part of the model logic)
│   │   ├── MergeSort.java
│   │   └── BinarySearch.java
│   ├── view/                       <- VIEW: everything the user sees or types
│   │   ├── ConsoleView.java
│   │   └── MenuOption.java         (enum for the menu)
│   └── controller/
│       └── BankController.java     <- CONTROLLER: connects view and model
├── test/bank/test/AlgorithmTests.java
├── build.sh / build.bat            <- compile
├── run.sh / run.bat                <- run
└── run_tests.sh                    <- run the tests


## How to run

Requirements: **JDK 17 or newer** because the code uses `record`.

```bash
./build.sh        # Windows: build.bat
./run.sh          # Windows: run.bat
./run_tests.sh    # optional: run the tests
When the program starts, the user types the file name: Applicants_Form.txt. The file is searched in the current folder and inside the data/ folder because the application must locate the input file easily.

Data file format
One employee record is placed per line in the format Name,Manager Type,Department. Lines starting with #, blank lines, and the header line are skipped because they do not contain valid employee records. Valid Manager Types are Branch Manager, Senior Manager, Team Lead, Supervisor, and Staff Member. Valid Departments are IT, HR, Finance, Retail Banking, Loans and Mortgages, Risk and Compliance, and Customer Service.

Why these algorithms?
This section explains why these algorithms and structures were chosen for the application. Merge Sort is used because it always runs in O(n log n) time and keeps equal items in order without changing the original list, while Quick Sort can degrade to O(n²) and Bubble, Insertion, and Selection Sort are slower at O(n²). Binary Search is used because the list is already sorted from A to Z and searching takes O(log n) steps (around 20 steps for 1,000,000 names), whereas linear search takes O(n). The binary tree with level-order insertion is used because it is simple, uses a queue, inserts in O(1) time, and keeps at most two children per node.

Author
Name: Adriel Kampa de Gois  -  Student ID: 2024378