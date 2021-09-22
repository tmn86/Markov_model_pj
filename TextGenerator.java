/* *****************************************************************************
 *
 *  Description:  A MarkovModel client that represents a Markov chain
 * which is a stochastic process
 * where the state change depends on only the current states which is
 *  bases on the frequency
 * of a character in the text with order of k.
 *
 **************************************************************************** */

public class TextGenerator {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        String text = StdIn.readAll();

        MarkovModel model = new MarkovModel(text, k);
        // get the starting substring with order of k
        String kgram = text.substring(0, k);
        StdOut.print(kgram);
        String newText = kgram;

        for (int i = 0; i < t - k; i++) {
            char c = model.random(kgram); // get a random character bases on its frequency
            StdOut.print(c);
            newText += c; // add c to new text
            // update kgram after c is added
            kgram = newText.substring(newText.length() - k);
        }
        StdOut.println();
    }
}
