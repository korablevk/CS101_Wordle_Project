package best_algorithm;

import java.util.ArrayList;
import java.util.List;

class Wordle {
    /**
     * Class representing one Wordle game.
     * Methods include initiating a secret word,
     * returning green/yellow/grey results,
     * and keeping track of guessed letters.
     */

    private String correctWord;
    private List<Guess> guesses;

    public Wordle(String correctWord, WordList puzzleWords) {
        // Initialize the target word
        if (correctWord != null && puzzleWords.wordList.contains(correctWord)) {
            this.correctWord = correctWord;
        } else {
            this.correctWord = puzzleWords.getRandomWord();
        }

        // Initialize guesses list
        this.guesses = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("::" + correctWord + "::");
        for (int i = 0; i < guesses.size(); i++) {
            out.append("\n").append(i + 1).append(". ").append(guesses.get(i));
        }
        return out.toString();
    }

    public boolean guess(String word) {
        /**
         * One turn of the game:
         * get guessed word, add a new Guess to the guesses list.
         * If guessed correctly, return true; otherwise, false.
         */
        guesses.add(new Guess(word, correctWord));
        return guesses.get(guesses.size() - 1).isGuessedCorrectly();
    }

    public static void main(String[] args) throws Exception {
        // Create WordList instance
        WordList puzzleWords = new WordList("src/best_algorithm/words-all.txt");

        // Initialize Wordle game
        String correctWord = null; // Let the game choose a random word
        Wordle game = new Wordle(correctWord, puzzleWords);

        // Test guesses
        String[] testGuesses = {"apply", "peach", "piano", "alpha", "apple"};

        for (String guess : testGuesses) {
            boolean isCorrect = game.guess(guess);
            System.out.println(game); // Print the game state
            if (isCorrect) {
                System.out.println("Congratulations! You guessed the word correctly.");
                break;
            }
        }
    }
}
