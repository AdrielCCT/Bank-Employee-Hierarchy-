package bank.test;

import bank.algorithm.BinarySearch;
import bank.algorithm.MergeSort;
import bank.model.Department;
import bank.model.Employee;
import bank.model.EmployeeBinaryTree;
import bank.model.ManagerType;
import java.util.ArrayList;
import java.util.List;

/**
 * Small tests without any library

 */
public class AlgorithmTests {

    private static int failures = 0;

    public static void main(String[] args) {
        testMergeSortOrder();
        testMergeSortKeepsOriginalList();
        testBinarySearchFound();
        testBinarySearchNotFound();
        testEnumValidation();
        testNameValidation();
        testTreeCountAndHeight();
        testTreeLevelOrder();

        if (failures == 0) {
            System.out.println("ALL TESTS PASSED");
        } else {
            System.out.println(failures + " TEST(S) FAILED");
            System.exit(1);
        }
    }

    private static Employee emp(String name) {
        return new Employee(name, ManagerType.STAFF_MEMBER, Department.IT);
    }

    private static void check(boolean condition, String testName) {
        if (condition) {
            System.out.println("PASS: " + testName);
        } else {
            System.out.println("FAIL: " + testName);
            failures++;
        }
    }

    private static void testMergeSortOrder() {
        List<Employee> list = List.of(emp("zara"), emp("Adam"), emp("maria"), emp("Bob"));
        List<Employee> sorted = MergeSort.sort(list, Employee.BY_NAME);
        check(sorted.get(0).getName().equals("Adam")
                && sorted.get(1).getName().equals("Bob")
                && sorted.get(2).getName().equals("maria")
                && sorted.get(3).getName().equals("zara"), "Merge sort orders A-Z ignoring case");
    }

    private static void testMergeSortKeepsOriginalList() {
        List<Employee> list = new ArrayList<>(List.of(emp("B"), emp("A")));
        MergeSort.sort(list, Employee.BY_NAME);
        check(list.get(0).getName().equals("B"), "Merge sort does not change the input list");
    }

    private static void testBinarySearchFound() {
        List<Employee> sorted = MergeSort.sort(
                List.of(emp("Eve"), emp("Adam"), emp("Carl"), emp("Dina"), emp("Bob")), Employee.BY_NAME);
        int index = BinarySearch.search(sorted, "  carl ");
        check(index == 2 && sorted.get(index).getName().equals("Carl"),
                "Binary search finds a name (ignores case and spaces)");
    }

    private static void testBinarySearchNotFound() {
        List<Employee> sorted = MergeSort.sort(List.of(emp("Adam"), emp("Bob")), Employee.BY_NAME);
        check(BinarySearch.search(sorted, "Zed") == -1, "Binary search returns -1 when name is missing");
        check(BinarySearch.search(new ArrayList<>(), "Zed") == -1, "Binary search works on an empty list");
    }

    private static void testEnumValidation() {
        check(ManagerType.fromInput("team lead").isPresent(), "Manager type accepts label");
        check(ManagerType.fromInput("3").get() == ManagerType.TEAM_LEAD, "Manager type accepts number");
        check(ManagerType.fromInput("Janitor").isEmpty(), "Manager type rejects unknown value");
        check(Department.fromInput("finance").isPresent(), "Department accepts label");
        check(Department.fromInput("Space Travel").isEmpty(), "Department rejects unknown value");
    }

    private static void testNameValidation() {
        check(Employee.isValidName("Margaret O'Brien"), "Name with apostrophe is valid");
        check(!Employee.isValidName("R2D2 99"), "Name with digits is invalid");
        check(!Employee.isValidName("   "), "Blank name is invalid");
    }

    private static EmployeeBinaryTree buildTree(int count) {
        EmployeeBinaryTree tree = new EmployeeBinaryTree();
        for (int i = 1; i <= count; i++) {
            tree.insert(emp("Person" + (char) ('A' + i)));
        }
        return tree;
    }

    private static void testTreeCountAndHeight() {
        EmployeeBinaryTree tree = buildTree(20);
        check(tree.size() == 20, "Tree with 20 inserts has 20 nodes");
        check(tree.height() == 5, "Tree with 20 nodes has height 5 (levels 1+2+4+8+5)");
        check(new EmployeeBinaryTree().height() == 0, "Empty tree has height 0");
    }

    private static void testTreeLevelOrder() {
        List<List<Employee>> levels = buildTree(7).levelOrder();
        check(levels.size() == 3 && levels.get(0).size() == 1
                        && levels.get(1).size() == 2 && levels.get(2).size() == 4,
                "Level order gives levels of size 1, 2 and 4 for 7 nodes");
    }
}
