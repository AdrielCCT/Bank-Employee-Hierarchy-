package bank.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This section defines the Recursive Merge Sort algorithm for the bank system.
 *
 * THE RATIONALE FOR MERGE SORT:
 * 1. Guaranteed speed: The performance is maintained at O(n log n) even for random data like form submissions because quick sort can degrade to O(n^2) in worst-case scenarios.
 * 2. Stability: Equal keys preserve their original order because stability is required when records are sorted by rank and subsequently by name.
 * 3. Immutability: The original list is not modified because a new sorted list is returned, which is safer for the Model-View-Controller design architecture.
 * 4. Recursion efficiency: The recursion depth is limited to log n because the system processes list structures safely without stack overflow risks.
 * Trade-off: Additional memory of O(n) is required by the algorithm, which is accepted because the bank system processes thousands of employee records efficiently while slower O(n^2) algorithms like bubble or insertion sort are avoided.
 */
public final class MergeSort {

    private MergeSort() {
        // Utility class: Object instantiation is prevented by design.
    }

    /**
     * This method sorts an input list and returns a NEW sorted list.
     * The original list remains unchanged because data integrity must be maintained.
     *
     * @param input      the list to be sorted
     * @param comparator the comparison rule applied between two items
     */
    public static <T> List<T> sort(List<T> input, Comparator<? super T> comparator) {
        // Base case: A list with 0 or 1 item is evaluated as already sorted.
        if (input.size() <= 1) {
            return new ArrayList<>(input);
        }

        // Divide phase: The list is split into two halves.
        int middle = input.size() / 2;
        List<T> left = sort(input.subList(0, middle), comparator);        // Recursive call
        List<T> right = sort(input.subList(middle, input.size()), comparator); // Recursive call

        // Conquer phase: The two sorted halves are joined together.
        return merge(left, right, comparator);
    }

    /**
     * This method joins two sorted lists into a single sorted list.
     */
    private static <T> List<T> merge(List<T> left, List<T> right, Comparator<? super T> comparator) {
        List<T> result = new ArrayList<>(left.size() + right.size());
        int i = 0;
        int j = 0;

        while (i < left.size() && j < right.size()) {
            // The left item is selected when both values are equal because sort stability must be preserved.
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }

        // Remaining elements are copied because one list is finished while items remain in the other.
        while (i < left.size()) {
            result.add(left.get(i++));
        }
        while (j < right.size()) {
            result.add(right.get(j++));
        }
        return result;
    }
}