package bank.model;

import java.util.Comparator;
import java.util.Objects;

/**
 * This section defines the employee entity for the bank system.
 * The object is created as immutable because the values cannot be altered after the initial creation.
 */
public class Employee {

    /**
     * This comparator is used to sort the employees by name.
     * Upper and lower case letters are ignored because a standard alphabetical order is required by the system.
     */
    public static final Comparator<Employee> BY_NAME =
            Comparator.comparing(Employee::getName, String.CASE_INSENSITIVE_ORDER);

    /**
     * This comparator is configured to organize the employees based on their hierarchy level.
     * The name is subsequently used as a secondary sorting parameter.
     */
    public static final Comparator<Employee> BY_RANK_THEN_NAME =
            Comparator.comparingInt((Employee e) -> e.getManagerType().getRank())
                    .thenComparing(BY_NAME);

    private final String name;
    private final ManagerType managerType;
    private final Department department;

    public Employee(String name, ManagerType managerType, Department department) {
        this.name = Objects.requireNonNull(name).trim();
        this.managerType = Objects.requireNonNull(managerType);
        this.department = Objects.requireNonNull(department);
    }

    public String getName() {
        return name;
    }

    public ManagerType getManagerType() {
        return managerType;
    }

    public Department getDepartment() {
        return department;
    }

    /**
     * This method is used to verify if the provided name is valid.
     * The input is restricted to a maximum length of 50 characters, and specific characters are permitted including letters, spaces, and hyphens, because the database requires standardized text inputs.
     */
    public static boolean isValidName(String name) {
        if (name == null) {
            return false;
        }
        String clean = name.trim();
        return !clean.isEmpty()
                && clean.length() <= 50
                && clean.matches("[\\p{L}][\\p{L} .'-]*");
    }

    @Override
    public String toString() {
        return String.format("%-22s | %-15s | %s",
                name, managerType.getLabel(), department.getLabel());
    }
}