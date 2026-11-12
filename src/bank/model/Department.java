package bank.model;

import java.util.Optional;

/**
 * This section outlines the departments for the bank system.
 * The enum structure is used because the system must ensure that only valid departments are selected by the user.
 */
public enum Department {
    IT("IT"),
    HR("HR"),
    FINANCE("Finance"),
    RETAIL_BANKING("Retail Banking"),
    LOANS_AND_MORTGAGES("Loans and Mortgages"),
    RISK_AND_COMPLIANCE("Risk and Compliance"),
    CUSTOMER_SERVICE("Customer Service");

    private final String label;

    Department(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * This method converts the user input into a Department object.
     * The text is accepted as a menu number, a label, and an enum name because the system supports different input formats.
     *
     * @return the department, or an empty Optional if the input is not valid
     */
    public static Optional<Department> fromInput(String text) {
        if (text == null) {
            return Optional.empty();
        }
        String clean = text.trim().replace('_', ' ');

        // The system checks if the user provided a valid menu number.
        try {
            int number = Integer.parseInt(clean);
            if (number >= 1 && number <= values().length) {
                return Optional.of(values()[number - 1]);
            }
            return Optional.empty();
        } catch (NumberFormatException e) {
            // The input is not a number, so the system attempts to match the text format.
        }

        // The system evaluates if the text matches the department label or the enum name.
        for (Department department : values()) {
            if (department.label.equalsIgnoreCase(clean)
                    || department.name().replace('_', ' ').equalsIgnoreCase(clean)) {
                return Optional.of(department);
            }
        }
        return Optional.empty();
    }
}