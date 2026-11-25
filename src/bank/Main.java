package bank;

import bank.controller.BankController;
import bank.model.EmployeeRepository;
import bank.view.ConsoleView;

/**
 * Entry point of the Bank Employee Hierarchy program.
 * It only creates the three MVC parts and starts the controller.
 */
public class Main {
    public static void main(String[] args) {
        EmployeeRepository model = new EmployeeRepository();
        ConsoleView view = new ConsoleView();
        BankController controller = new BankController(model, view);
        controller.run();
    }
}
