package bank.view;

import bank.model.Department;
import bank.model.Employee;
import bank.model.ManagerType;
import java.util.List;
import java.util.Scanner;

/**
 * This section defines the ConsoleView class, which manages all terminal input and output operations for the system.
 * Business logic processing is excluded from this class because the component is restricted to displaying information and reading user inputs.
 */
public class ConsoleView {

    private final Scanner scanner = new Scanner(System.in);

    // ---------- reading input ----------

    /**
     * This method reads a single input line from the terminal.
     * A null value is returned when no further input lines are available because end-of-file conditions must be handled.
     */
    private String readLine() {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }

    public String askFileName() {
        System.out.print("Please enter the filename to read: ");
        return readLine();
    }

    public String askMenuChoice() {
        System.out.println();
        System.out.println("Do You wish to SORT or SEARCH:");
        for (MenuOption option : MenuOption.values()) {
            System.out.println(option.getNumber() + ". " + option.getLabel());
        }
        System.out.println();
        return readLine();
    }

    public String askSearchName() {
        System.out.print("Enter the full name to search: ");
        return readLine();
    }

    public String askNewName() {
        System.out.print("Enter the new employee's name: ");
        return readLine();
    }

    public String askManagerType() {
        System.out.println("Choose a Manager Type:");
        for (ManagerType type : ManagerType.values()) {
            System.out.println("  " + type.getRank() + ". " + type.getLabel());
        }
        System.out.print("Manager Type (number or name): ");
        return readLine();
    }

    public String askDepartment() {
        System.out.println("Choose a Department:");
        Department[] departments = Department.values();
        for (int i = 0; i < departments.length; i++) {
            System.out.println("  " + (i + 1) + ". " + departments[i].getLabel());
        }
        System.out.print("Department (number or name): ");
        return readLine();
    }

    // ---------- showing output ----------

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String message) {
        System.out.println("ERROR: " + message);
    }

    /**
     * This method displays the specified limit of employee records formatted as a numbered table.
     */
    public void showEmployees(String title, List<Employee> employees, int limit) {
        System.out.println(title);
        printTableHeader();
        int count = Math.min(limit, employees.size());
        for (int i = 0; i < count; i++) {
            System.out.printf("%3d. %s%n", i + 1, employees.get(i));
        }
        System.out.println("Showing " + count + " of " + employees.size() + " records.");
    }

    public void showSearchResult(Employee employee) {
        System.out.println("Person found!");
        System.out.println("  Name         : " + employee.getName());
        System.out.println("  Manager Type : " + employee.getManagerType().getLabel());
        System.out.println("  Department   : " + employee.getDepartment().getLabel());
    }

    public void showNewRecords(List<Employee> newRecords) {
        System.out.println("Newly added records (" + newRecords.size() + "):");
        printTableHeader();
        for (int i = 0; i < newRecords.size(); i++) {
            System.out.printf("%3d. %s%n", i + 1, newRecords.get(i));
        }
    }

    /**
     * This method displays the hierarchical tree structure level by level, followed by the computed height and total node count.
     */
    public void showTree(List<List<Employee>> levels, int height, int nodeCount) {
        System.out.println("Bank hierarchy - level order traversal:");
        for (int i = 0; i < levels.size(); i++) {
            System.out.println("Level " + i + " (" + levels.get(i).size() + " node(s)):");
            for (Employee employee : levels.get(i)) {
                System.out.println("    " + employee);
            }
        }
        System.out.println("Tree height (levels): " + height);
        System.out.println("Total node count    : " + nodeCount);
    }

    private void printTableHeader() {
        System.out.printf("%3s  %-22s | %-15s | %s%n", "#", "Name", "Manager Type", "Department");
        System.out.println("-".repeat(70));
    }
}