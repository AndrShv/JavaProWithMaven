import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JewelsAndStonesTest {
    @Test
    public void testNumJewelsInStones() {
        JewelsAndStones js = new JewelsAndStones();

        String jewels1 = "aA";
        String stones1 = "aaaaAAbbbb";
        assertEquals(6, js.numJewelsInStones(jewels1, stones1));

        String jewels2 = "z";
        String stones2 = "ZZ";
        assertEquals(0, js.numJewelsInStones(jewels2, stones2));

        String jewels3 = "b";
        String stones3 = "bBBBbb";
        assertEquals(3, js.numJewelsInStones(jewels3, stones3));

        String jewels4 = "abc";
        String stones4 = "aabbccABC";
        assertEquals(6, js.numJewelsInStones(jewels4, stones4));

        String jewels5 = "";
        String stones5 = "abcdef";
        assertEquals(0, js.numJewelsInStones(jewels5, stones5));

        String jewels6 = "a";
        String stones6 = "";
        assertEquals(0, js.numJewelsInStones(jewels6, stones6));

        String jewels7 = null;
        String stones7 = "abcdef";
        assertEquals(0, js.numJewelsInStones(jewels7, stones7));

        String jewels8 = "a";
        String stones8 = null;
        assertEquals(0, js.numJewelsInStones(jewels8, stones8));

        String jewels9 = null;
        String stones9 = null;
        assertEquals(0, js.numJewelsInStones(jewels9, stones9));
    }
}
