import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordBalloonFounderTest {
    @Test
    public void testFindBalloons() {
        WordBalloonFounder wbf = new WordBalloonFounder();

        assertEquals(1, wbf.findBalloons("nlaebolko"));
        assertEquals(2, wbf.findBalloons("loonbalxballpoon"));
        assertEquals(0, wbf.findBalloons("leetcode"));
        assertEquals(0, wbf.findBalloons(""));
        assertEquals(1, wbf.findBalloons("balloon"));
        assertEquals(0, wbf.findBalloons("balon"));
        assertEquals(3, wbf.findBalloons("balloonballoonballoon"));
    }

    @Test
    public void testFindBalloonsWithMixedCase() {
        WordBalloonFounder wbf = new WordBalloonFounder();

        assertEquals(1, wbf.findBalloons("Nlaebolko"));
        assertEquals(2, wbf.findBalloons("Loonbalxballpoon"));
        assertEquals(0, wbf.findBalloons("Leetcode"));
    }
    @Test
    public void testFindBalloonsWithSpecialCharacters() {
        WordBalloonFounder wbf = new WordBalloonFounder();

        assertEquals(1, wbf.findBalloons("!nlae*bolko@"));
        assertEquals(2, wbf.findBalloons("#loonbalxballpoon$"));
        assertEquals(0, wbf.findBalloons("leet#code$"));
        assertEquals(1, wbf.findBalloons("b@a!l#l$o%o^n&"));
    }

    @Test
    public void testFindBalloonsWithExtraSpaces() {
        WordBalloonFounder wbf = new WordBalloonFounder();

        assertEquals(1, wbf.findBalloons("  nlaebolko  "));
        assertEquals(2, wbf.findBalloons("loonbalxballpoon  "));
        assertEquals(0, wbf.findBalloons("   leetcode"));
    }

    @Test
    public void testFindBalloonsWithNull() {
        WordBalloonFounder wbf = new WordBalloonFounder();

        assertEquals(0, wbf.findBalloons(null));
    }
}
