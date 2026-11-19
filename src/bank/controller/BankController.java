package bank.controller;

import bank.algorithm.BinarySearch;
import bank.algorithm.MergeSort;
import bank.model.Department;
import bank.model.Employee;
import bank.model.EmployeeBinaryTree;
import bank.model.EmployeeRepository;
import bank.model.LoadResult;
import bank.model.ManagerType;
import bank.view.ConsoleView;
import bank.view.MenuOption;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This section defines the BankController class, which connects the view layer and the model layer.
 * Data decisions are handled through coordination because user input is read by the view, models and algorithms are invoked, and results are sent back to the view.
 */
public class BankController {

    private static final int SORT_DISPLAY_LIMIT = 20;
    private static final int MIN_TREE_RECORDS = 20;

    private final EmployeeRepository repository;
    private final ConsoleView view;
    // All employee records added during the current session are stored in this list.
    private final List<Employee> newRecords = new ArrayList<>();

    public BankController(EmployeeRepository repository, ConsoleView view) {
        this.repository = repository;
        this.view = view;
    }

    /**
     * This method starts the application execution.
     * The data file is loaded first because the menu loop requires loaded records to operate.
     */
    public void run() {
        if (!loadFile()) {
            return; // Execution is stopped because the input stream was closed.
        }
        boolean running = true;
        while (running) {
            String input = view.askMenuChoice();
            // A null input means no more lines remain, so the exit option is selected.
            Optional<MenuOption> choice = (input == null)
                    ? Optional.of(MenuOption.EXIT)
                    : MenuOption.fromInput(input);

            if (choice.isEmpty()) {
                view.showError("Invalid option. Please choose a number from 1 to "
                        + MenuOption.values().length + ".");
                continue;
            }
            switch (choice.get()) {
                case SORT:
                    handleSort();
                    break;
                case SEARCH:
                    handleSearch();
                    break;
                case ADD_RECORDS:
                    handleAddRecords();
                    break;
                case CREATE_BINARY_TREE:
                    handleBinaryTree();
                    break;
                case EXIT:
                    view.showMessage("Goodbye!");
                    running = false;
                    break;
            }
        }
    }

    // ---------- loading ----------

    /**
     * This method prompts for a file name until a valid file is loaded.
     * A false value is returned when the input ends because the application cannot continue without input.
     */
    private boolean loadFile() {
        while (true) {
            String name = view.askFileName();
            if (name == null || name.trim().equalsIgnoreCase("exit")) {
                return false;
            }
            Path path = findFile(name.trim());
            if (path == null) {
                view.showError("File not found: " + name + " (type 'exit' to quit)");
                continue;
            }
            try {
                LoadResult result = repository.loadFromFile(path);
                view.showMessage("File read successfully");
                view.showMessage(result.loaded() + " record(s) loaded.");
                for (String problem : result.problems()) {
                    view.showMessage("  Skipped - " + problem);
                }
                return true;
            } catch (IOException e) {
                view.showError("Could not read the file: " + e.getMessage());
            }
        }
    }

    /**
     * This method searches for the target file in the current directory and inside the data folder.
     */
    private Path findFile(String name) {
        Path direct = Paths.get(name);
        if (Files.isRegularFile(direct)) {
            return direct;
        }
        Path inData = Paths.get("data", name);
        return Files.isRegularFile(inData) ? inData : null;
    }

    // ---------- menu actions ----------

    /**
     * This method sorts the employee records from A to Z using recursive Merge Sort and displays the first 20 entries.
     */
    private void handleSort() {
        view.showMessage("SORT selected");
        sortRepository();
        view.showEmployees("First " + SORT_DISPLAY_LIMIT + " names in alphabetical order:",
                repository.getAll(), SORT_DISPLAY_LIMIT);
    }

    /**
     * This method searches for an employee name using recursive Binary Search and displays the matching manager type and department.
     */
    private void handleSearch() {
        view.showMessage("SEARCH selected");
        String name = view.askSearchName();
        if (name == null || name.trim().isEmpty()) {
            view.showError("The name cannot be empty.");
            return;
        }
        // The list is sorted when needed because binary search requires sorted data to return accurate results.
        if (!repository.isSorted()) {
            sortRepository();
        }
        int index = BinarySearch.search(repository.getAll(), name);
        if (index >= 0) {
            view.showSearchResult(repository.getAll().get(index));
        } else {
            view.showMessage("No employee found with the name '" + name.trim() + "'.");
        }
    }

    /**
     * This method reads a new employee record, validates each field, and stores the record in memory.
     */
    private void handleAddRecords() {
        view.showMessage("ADD RECORDS selected");

        String name = readValidName();
        if (name == null) {
            return;
        }
        ManagerType type = readValidManagerType();
        if (type == null) {
            return;
        }
        Department department = readValidDepartment();
        if (department == null) {
            return;
        }
        Employee employee = new Employee(name, type, department);
        repository.add(employee);
        newRecords.add(employee);
        sortRepository(); // The repository is sorted after insertion because alphabetical order must be maintained.
        view.showMessage("Record added successfully.");
        view.showNewRecords(newRecords);
    }

    /**
     * This method constructs the binary tree using level-order insertion and displays the resulting hierarchy.
     */
    private void handleBinaryTree() {
        view.showMessage("Create a binary tree selected");
        if (repository.size() < MIN_TREE_RECORDS) {
            view.showError("At least " + MIN_TREE_RECORDS + " records are needed. Now there are "
                    + repository.size() + ".");
            return;
        }
        // Records are ordered by rank first and then by name because senior roles should appear closer to the root.
        List<Employee> ordered = MergeSort.sort(repository.getAll(), Employee.BY_RANK_THEN_NAME);
        EmployeeBinaryTree tree = new EmployeeBinaryTree();
        for (Employee employee : ordered) {
            tree.insert(employee);
        }
        view.showTree(tree.levelOrder(), tree.height(), tree.size());
    }

    // ---------- helpers ----------

    private void sortRepository() {
        repository.replaceWithSorted(
                MergeSort.sort(repository.getAll(), Employee.BY_NAME));
    }

    /**
     * This method prompts the user until a valid and unique name is provided.
     * A null value is returned when input ends because the prompt loop must terminate cleanly.
     */
    private String readValidName() {
        while (true) {
            String name = view.askNewName();
            if (name == null) {
                return null;
            }
            if (!Employee.isValidName(name)) {
                view.showError("Invalid name. Use letters, spaces, hyphens or apostrophes (max 50).");
            } else if (repository.containsName(name)) {
                view.showError("An employee with this name already exists.");
            } else {
                return name.trim();
            }
        }
    }

    /**
     * This method prompts the user until a valid manager type is selected.
     * A null value is returned when input ends because the prompt loop must stop safely.
     */
    private ManagerType readValidManagerType() {
        while (true) {
            String text = view.askManagerType();
            if (text == null) {
                return null;
            }
            Optional<ManagerType> type = ManagerType.fromInput(text);
            if (type.isPresent()) {
                return type.get();
            }
            view.showError("This Manager Type does not exist. Please try again.");
        }
    }

    /**
     * This method prompts the user until a valid department is selected.
     * A null value is returned when input ends because the prompt loop must stop safely.
     */
    private Department readValidDepartment() {
        while (true) {
            String text = view.askDepartment();
            if (text == null) {
                return null;
            }
            Optional<Department> department = Department.fromInput(text);
            if (department.isPresent()) {
                return department.get();
            }
            view.showError("This Department does not exist. Please try again.");
        }
    }
}