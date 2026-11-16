package bank.model;

import java.util.List;

/**
 * The result of reading the employee file.
 *
 * @param loaded   how many employees were read correctly
 * @param problems one message for each line that could not be used
 */
public record LoadResult(int loaded, List<String> problems) {
}
