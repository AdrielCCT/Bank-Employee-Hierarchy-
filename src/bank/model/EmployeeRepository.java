package bank.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * This section defines the EmployeeRepository class, which stores employee records in memory and reads data from a text file.
 * User output and terminal interaction are excluded from this class because it is part of the Model layer in the MVC design.
 */
public class EmployeeRepository {

    private List<Employee> employees = new ArrayList<>();
    // The sorted flag is set to true when the list is ordered alphabetically because binary search requires a sorted list.
    private boolean sorted = false;

    /**
     * This method reads employee records from a file where each line follows the format Name,Manager Type,Department.
     * Blank lines, comment lines starting with #, and header lines are skipped because they do not contain employee records, and invalid lines are recorded in the result instead of being added.
     *
     * @throws IOException if the file cannot be read
     */
    public LoadResult loadFromFile(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        List<String> problems = new ArrayList<>();
        int loaded = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            int lineNumber = i + 1;

            if (line.isEmpty() || line.startsWith("#") || line.toLowerCase().startsWith("name,")) {
                continue; // This line is skipped because no employee data is present.
            }
            try {
                Employee employee = parseLine(line);
                if (add(employee)) {
                    loaded++;
                } else {
                    problems.add("Line " + lineNumber + ": duplicate name '" + employee.getName() + "'");
                }
            } catch (IllegalArgumentException e) {
                problems.add("Line " + lineNumber + ": " + e.getMessage());
            }
        }
        return new LoadResult(loaded, problems);
    }

    /**
     * This helper method converts a text line into an Employee object or throws an IllegalArgumentException when the format is invalid.
     */
    private Employee parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length != 3) {
            throw new IllegalArgumentException("expected 3 values separated by commas");
        }
        String name = parts[0].trim();
        if (!Employee.isValidName(name)) {
            throw new IllegalArgumentException("invalid name '" + name + "'");
        }
        Optional<ManagerType> type = ManagerType.fromInput(parts[1]);
        if (type.isEmpty()) {
            throw new IllegalArgumentException("unknown manager type '" + parts[1].trim() + "'");
        }
        Optional<Department> department = Department.fromInput(parts[2]);
        if (department.isEmpty()) {
            throw new IllegalArgumentException("unknown department '" + parts[2].trim() + "'");
        }
        return new Employee(name, type.get(), department.get());
    }

    /**
     * This method adds an employee record to the in-memory list.
     *
     * @return false if an employee with the same name already exists
     */
    public boolean add(Employee employee) {
        if (containsName(employee.getName())) {
            return false;
        }
        employees.add(employee);
        sorted = false; // The sorted flag is reset to false because the new record may not be in alphabetical order.
        return true;
    }

    public boolean containsName(String name) {
        for (Employee e : employees) {
            if (e.getName().equalsIgnoreCase(name.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method replaces the internal list with a list that is already sorted from A to Z.
     */
    public void replaceWithSorted(List<Employee> sortedList) {
        this.employees = new ArrayList<>(sortedList);
        this.sorted = true;
    }

    public boolean isSorted() {
        return sorted;
    }

    public int size() {
        return employees.size();
    }

    /**
     * This method returns an unmodifiable view of the employee list because external classes should not modify the internal collection directly.
     */
    public List<Employee> getAll() {
        return Collections.unmodifiableList(employees);
    }
}