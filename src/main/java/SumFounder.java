import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SumFounder {
    public static List<int[]> twoSum(int[] nums, int target) {
        List<int[]> result = new ArrayList<>();
        if (nums == null) {
            return result;
        }

        HashMap<Integer, List<Integer>> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                for (int index : map.get(complement)) {
                    result.add(new int[]{index, i});
                }
            }
            if (!map.containsKey(nums[i])) {
                map.put(nums[i], new ArrayList<>());
            }
            map.get(nums[i]).add(i);
        }
        return result;
    }

    public static void main(String[] args) {
        int[] nums = {2, 7, 11, 15, 5, 7, 9, 13, 24, 5, 6, 2, 1, 3};
        int target = 34;
        List<int[]> results = twoSum(nums, target);

        if (results.isEmpty()) {
            System.out.println("No pairs found that sum to " + target);
        } else {
            for (int[] result : results) {
                System.out.println("Indices: " + result[0] + ", " + result[1]);
            }
        }
    }
}
