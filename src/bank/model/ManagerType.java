package bank.model;

import java.util.Optional;

/**
 * This section defines the manager types for the bank system.
 * The enum structure is used because the system must prevent the creation of invalid manager types.
 * The rank indicates the hierarchy level, where the number 1 represents the highest position.
 */
public enum ManagerType {
    BRANCH_MANAGER(1, "Branch Manager"),
    SENIOR_MANAGER(2, "Senior Manager"),
    TEAM_LEAD(3, "Team Lead"),
    SUPERVISOR(4, "Supervisor"),
    STAFF_MEMBER(5, "Staff Member");

    private final int rank;
    private final String label;

    ManagerType(int rank, String label) {
        this.rank = rank;
        this.label = label;
    }

    public int getRank() {
        return rank;
    }

    public String getLabel() {
        return label;
    }

    /**
     * This method converts the text from the user into a ManagerType object.
     * The input is accepted as a menu number, a label, and an enum name because the system supports different user inputs.
     * The text is processed without case sensitivity.
     *
     * @return the manager type, or an empty Optional if the input is not valid
     */
    public static Optional<ManagerType> fromInput(String text) {
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
            // The input is not a number, so the system tries to match the text format.
        }

        // The system checks if the text matches the manager label or the enum name.
        for (ManagerType type : values()) {
            if (type.label.equalsIgnoreCase(clean)
                    || type.name().replace('_', ' ').equalsIgnoreCase(clean)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}