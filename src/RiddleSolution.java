import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RiddleSolution {

    private static final Logger logger = Logger.getLogger(RiddleSolution.class.getName());
    private static final String WORDS_TXT_URI = "https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt";
    private static final Set<String> allWords = loadAllWords();

    public static void main(String[] args) {
        var start = System.nanoTime();

        Set<String> result = allWords.stream().filter(word -> word.length() == 9)
                .filter(RiddleSolution::hasNextValidWord)
                .collect(Collectors.toSet());

        var timeElapsed = System.nanoTime() - start;
        logger.log(Level.INFO, "Words found: {0}", result.size());
        logger.log(Level.INFO, "Duration in nanoseconds: {0}", timeElapsed);
    }

    private static boolean hasNextValidWord(String word) {
        if (word.length() == 1) {
            return true;
        }
        String nextWord;
        for (var i = 0; i < word.length(); i++) {
            nextWord = word.substring(0, i) + word.substring(i + 1);
            if (isValidWord(nextWord) || "a".equalsIgnoreCase(nextWord) || "i".equalsIgnoreCase(nextWord)) {
                if (hasNextValidWord(nextWord)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isValidWord(String word) {
        return allWords.contains(word);
    }

    private static Set<String> loadAllWords() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URI(WORDS_TXT_URI).toURL().openConnection().getInputStream()))) {
            return reader.lines().skip(2).collect(Collectors.toSet());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}