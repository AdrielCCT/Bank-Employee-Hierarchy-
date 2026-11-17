package bank.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * This section defines a binary tree structure that represents the organizational hierarchy of the bank.
 * Each node contains a maximum of two child nodes. New nodes are inserted using a level-order traversal approach (breadth-first),
 * where the left child is populated first, followed by the right child, before moving to subsequent levels.
 */
public class EmployeeBinaryTree {

    /**
     * This private static class represents a single node within the tree structure.
     * An employee object is stored inside the node.
     */
    private static class Node {
        final Employee employee;
        Node left;
        Node right;

        Node(Employee employee) {
            this.employee = employee;
        }
    }

    private Node root;
    // Nodes that have available child positions are maintained in a level-order queue.
    private final Queue<Node> waiting = new ArrayDeque<>();

    /**
     * This method inserts an employee at the first available position in level-order.
     * The insertion time complexity is maintained at O(1) because queue operations provide direct access to the parent node.
     */
    public void insert(Employee employee) {
        Node node = new Node(employee);
        if (root == null) {
            root = node;
        } else {
            Node parent = waiting.peek(); // The first node with an available child position is retrieved.
            if (parent.left == null) {
                parent.left = node;
            } else {
                parent.right = node;
                waiting.poll(); // The parent node is removed from the queue because both child positions are now filled.
            }
        }
        waiting.add(node); // The newly added node is appended to the queue because it can accept child nodes later.
    }

    /**
     * This method performs a level-order (breadth-first) traversal of the tree.
     *
     * @return a list of levels, where each level contains a list of employee records ordered from left to right
     */
    public List<List<Employee>> levelOrder() {
        List<List<Employee>> levels = new ArrayList<>();
        if (root == null) {
            return levels;
        }
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size(); // The number of nodes present in the current level is evaluated.
            List<Employee> level = new ArrayList<>();
            for (int i = 0; i < levelSize; i++) {
                Node current = queue.poll();
                level.add(current.employee);
                if (current.left != null) {
                    queue.add(current.left);
                }
                if (current.right != null) {
                    queue.add(current.right);
                }
            }
            levels.add(level);
        }
        return levels;
    }

    /**
     * This method calculates the height of the tree, which corresponds to the total number of levels.
     * A tree containing only a root node returns a height of 1.
     */
    public int height() {
        return height(root);
    }

    /**
     * This recursive helper method computes the height of a node.
     * The height is determined by adding 1 to the height of its taller child because hierarchical depth must be accurately measured.
     */
    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * This method calculates the total number of nodes contained within the tree.
     */
    public int size() {
        return size(root);
    }

    /**
     * This recursive helper method counts the nodes.
     * The current node is summed together with all nodes located in the left and right subtrees because complete tree enumeration is required.
     */
    private int size(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.left) + size(node.right);
    }
}