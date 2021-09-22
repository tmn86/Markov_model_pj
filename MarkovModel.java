/* *****************************************************************************
 *
 *  Description:  Use a Markov chain to create a statistical model of a piece of English text.
 *  Simulate the Markov chain to generate stylized pseudo-random text.
 *
 **************************************************************************** */

public class MarkovModel {
    private static final int ASCII = 128; // size of ASCII alphabet
    private int order; // k order
    // ST for freqency of a kgram (freq(kgram))
    private ST<String, Integer> kgram1;
    // ST for frequencies of characters associate with a kgram (freq(kgram,c))
    private ST<String, int[]> kgram2;

    /**
     * creates a Markov model of order k for the specified text
     *
     * @param text input text to be used in model
     * @param k    order of Markov model
     */
    public MarkovModel(String text, int k) {

        order = k;
        text = text + text.substring(0, order); // get the first subtring of order k
        kgram1 = new ST<String, Integer>();
        kgram2 = new ST<String, int[]>();

        for (int i = 0; i < text.length() - order; i++) {
            int[] c; // array contain frequencies of each ASCII character
            String key = text.substring(i, i + order); // subtring start at i, length k
            char ch = text.charAt(i + order); // get character after a substring

            // increase value of a kgram if it existed
            if (kgram1.contains(key)) {
                kgram1.put(key, kgram1.get(key) + 1);
                // update value of a character in value array for that kgram
                if (kgram2.contains(key)) {
                    c = kgram2.get(key);
                    c[ch]++;
                    kgram2.put(key, c);
                }
            }

            else {
                // create new array for new kgram
                c = new int[ASCII];
                // increase the value of the character after the new kgram
                c[ch]++;
                kgram2.put(key, c);
                // initializa value of the new kgram is 1
                kgram1.put(key, 1);
            }

        }

    }

    // returns the order k of this Markov model
    public int order() {
        return order;
    }

    // returns a string representation of the Markov model (as described below)
    public String toString() {

        String result = "";
        for (String key : kgram2.keys()) {
            result += key + ": ";

            // get the character frequency array
            int[] frequency = kgram2.get(key);

            // for each non-zero entry, append the character and the frequency
            for (int i = 0; i < ASCII; i++) {
                String character = Character.toString((char) i);
                if (frequency[i] != 0) {
                    result += character + " " + frequency[i] + " ";
                }
            }
            result += "\n";
        }
        return result;
    }

    /**
     * returns the number of times the specified kgram appears in the text
     *
     * @param kgram a substring from input text with order of k
     */
    public int freq(String kgram) {
        if (kgram.length() != order())
            throw new IllegalArgumentException("not length k");
        if (!kgram1.contains(kgram))
            return 0;
        return kgram1.get(kgram);
    }

    /**
     * returns the number of times the character c follows the specified kgram in the text
     *
     * @param kgram a substring from input text with order of k
     * @param c     the character follow the kgram
     */
    public int freq(String kgram, char c) {
        if (kgram.length() != order())
            throw new IllegalArgumentException("not length k");
        int[] chr = kgram2.get(kgram);
        return chr[c];

    }

    /**
     * returns a random character that follows the specified kgram in the text, chosen with weight
     * proportional to the number of times that character follows the specified kgram in the text
     *
     * @param kgram a substring from input text with order of k
     */
    public char random(String kgram) {
        if (kgram.length() != order())
            throw new IllegalArgumentException("not length k");
        if (!kgram2.contains(kgram))
            throw new IllegalArgumentException("not contain kgram");
        int[] frequency = kgram2.get(kgram);
        // I used the StdRandom.discrete(frequencies[]) here
        int rd = StdRandom.discrete(frequency);
        char c = (char) rd;
        return c;
    }

    // Test the methods
    public static void main(String[] args) {
        String text1 = "banana";
        MarkovModel model1 = new MarkovModel(text1, 2);
        StdOut.println("freq(\"an\", 'a')    = " + model1.freq("an", 'a'));
        StdOut.println("freq(\"na\", 'b')    = " + model1.freq("na", 'b'));
        StdOut.println("freq(\"na\", 'a')    = " + model1.freq("na", 'a'));
        StdOut.println("freq(\"na\")         = " + model1.freq("na"));
        StdOut.println();

        String text3 = "one fish two fish red fish blue fish";
        MarkovModel model3 = new MarkovModel(text3, 4);
        StdOut.println("freq(\"ish \", 'r') = " + model3.freq("ish ", 'r'));
        StdOut.println("freq(\"ish \", 'x') = " + model3.freq("ish ", 'x'));
        StdOut.println("freq(\"ish \")      = " + model3.freq("ish "));
        StdOut.println("freq(\"tuna\")      = " + model3.freq("tuna"));

    }
}
