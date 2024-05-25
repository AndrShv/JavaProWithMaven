import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SumFounderTest {
    @Test
    public void testTwoSum() {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        List<int[]> result = SumFounder.twoSum(nums, target);

        assertEquals(1, result.size());
        assertEquals(0, result.get(0)[0]);
        assertEquals(1, result.get(0)[1]);
    }

    @Test
    public void testTwoSumWithMultiplePairs() {
        int[] nums = {2, 7, 11, 15, 5, 7, 9, 13, 24, 10, 6, 2, 1, 3};
        int target = 34;
        List<int[]> result = SumFounder.twoSum(nums, target);

        Set<Set<Integer>> expectedPairs = new HashSet<>();
        expectedPairs.add(Set.of(8, 9));


        Set<Set<Integer>> resultPairs = new HashSet<>();
        for (int[] pair : result) {
            resultPairs.add(Set.of(pair[0], pair[1]));
        }

        assertEquals(expectedPairs, resultPairs);
    }

    @Test
    public void testTwoSumWithNoPairs() {
        int[] nums = {1, 2, 3, 4, 5};
        int target = 10;
        List<int[]> result = SumFounder.twoSum(nums, target);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testTwoSumWithNegativeNumbers() {
        int[] nums = {-3, 4, 3, 90};
        int target = 0;
        List<int[]> result = SumFounder.twoSum(nums, target);

        assertEquals(1, result.size());
        assertEquals(0, result.get(0)[0]);
        assertEquals(2, result.get(0)[1]);
    }

    @Test
    public void testTwoSumWithDuplicates() {
        int[] nums = {3, 3, 4, 4};
        int target = 6;
        List<int[]> result = SumFounder.twoSum(nums, target);

        Set<Set<Integer>> expectedPairs = new HashSet<>();
        expectedPairs.add(Set.of(0, 1));

        Set<Set<Integer>> resultPairs = new HashSet<>();
        for (int[] pair : result) {
            resultPairs.add(Set.of(pair[0], pair[1]));
        }
        assertEquals(expectedPairs, resultPairs);
    }

    @Test
    public void testTwoSumWithNullArray() {
        int[] nums = null;
        int target = 10;
        List<int[]> result = SumFounder.twoSum(nums, target);

        assertTrue(result.isEmpty());
    }
}
