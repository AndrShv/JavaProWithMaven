public class JewelsAndStones {
    String jewels = "aA";
    String stones = "aaaaAAbbbb";

    public int numJewelsInStones(String  jewels, String stones) {
        if (jewels == null || stones == null) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < stones.length(); i++) {
            if (jewels.indexOf(stones.charAt(i)) != -1) {
                count++;
            }
        }
        return count;
    }
    public static void main(String args[]){

        JewelsAndStones js = new JewelsAndStones();
        System.out.println(js.numJewelsInStones(js.jewels, js.stones));
    }
}
