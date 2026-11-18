package bank.view;

import java.util.Optional;

/**
 * This section defines the menu options for the system application.
 * An enum structure is used because all menu items are centralized, allowing the view to iterate over the values and the controller to utilize switch statements without relying on magic numbers.
 */
public enum MenuOption {
    SORT(1, "SORT"),
    SEARCH(2, "SEARCH"),
    ADD_RECORDS(3, "ADD RECORDS"),
    CREATE_BINARY_TREE(4, "Create a binary tree"),
    EXIT(5, "EXIT");

    private final int number;
    private final String label;

    MenuOption(int number, String label) {
        this.number = number;
        this.label = label;
    }

    public int getNumber() {
        return number;
    }

    public String getLabel() {
        return label;
    }

    /**
     * This method converts the user input into a corresponding menu option.
     * The input is evaluated as a numeric value, a label, or an enum name because the system supports multiple input formats.
     *
     * @return the selected menu option, or an empty Optional if the input is not valid
     */
    public static Optional<MenuOption> fromInput(String text) {
        if (text == null) {
            return Optional.empty();
        }
        String clean = text.trim();
        for (MenuOption option : values()) {
            if (String.valueOf(option.number).equals(clean)
                    || option.label.equalsIgnoreCase(clean)
                    || option.name().replace('_', ' ').equalsIgnoreCase(clean)) {
                return Optional.of(option);
            }
        }
        return Optional.empty();
    }
}