import java.util.HashMap;
import java.util.Map;

public class WordBalloonFounder {

    public int findBalloons(String sentence) {
        if (sentence == null || sentence.isEmpty()) {
            return 0;
        }

        Map<Character, Integer> charCount = new HashMap<>();
        String balloon = "balloon";
        for (char c : sentence.toLowerCase().toCharArray()) {
            if (balloon.indexOf(c) != -1) {
                charCount.put(c, charCount.getOrDefault(c, 0) + 1);
            }
        }

        int b = charCount.getOrDefault('b', 0);
        int a = charCount.getOrDefault('a', 0);
        int l = charCount.getOrDefault('l', 0) / 2;
        int o = charCount.getOrDefault('o', 0) / 2;
        int n = charCount.getOrDefault('n', 0);

        return Math.min(Math.min(Math.min(b, a), l), Math.min(o, n));
    }

    public static void main(String[] args) {
        WordBalloonFounder wbf = new WordBalloonFounder();
        String text1 = "nlaebolko";
        String text2 = "loonbalxballpoon";
        String text3 = "leetcode";
        String text4 = "b@a!l#l$o%o^n&";

        System.out.println(wbf.findBalloons(text1)); // 1
        System.out.println(wbf.findBalloons(text2)); // 2
        System.out.println(wbf.findBalloons(text3)); // 0
        System.out.println(wbf.findBalloons(text4)); // 1
    }
}
