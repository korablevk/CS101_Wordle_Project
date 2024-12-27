package best_algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Guess {
    /** Class for one guess attempt
     Contains the guessed word and list of results
     */

    private String word;
    private boolean guessedCorrectly;
    private List<Integer> result;

    public Guess(String guessWord, String correctWord) {
        this.word = guessWord;
        this.result = calculateResult(correctWord);
        this.guessedCorrectly = this.result.equals(Arrays.asList(2, 2, 2, 2, 2));
    }

    @Override
    public String toString() {
        /** String representation looks like: ducky: G__Y_
         G, Y, _ is for green / yellow / grey
         */
        StringBuilder out = new StringBuilder(word + ": ");
        for (int letterResult : result) {
            if (letterResult == 2) {
                out.append("G");
            } else if (letterResult == 1) {
                out.append("Y");
            } else {
                out.append("_");
            }
        }
        return out.toString();
    }

    private List<Integer> calculateResult(String correctWord) {
        /** Given the guessed and the right word
         generate the list of letter results:
         0/1/2 meaning no/misplaced/correct
         */
        List<Integer> result = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0));
        // we are using a copy to blank guessed green and yellow
        // letters (to correctly display doubles)
        List<Character> correctCopy = new ArrayList<>();
        for (char c : correctWord.toCharArray()) {
            correctCopy.add(c);
        }

        for (int i = 0; i < word.length(); i++) {
            char guessedChar = word.charAt(i);
            if (guessedChar == correctCopy.get(i)) {
                result.set(i, 2);
                correctCopy.set(i, '\0'); // blank out the matched character
            }
        }
        for (int i = 0; i < word.length(); i++) {
            char guessedChar = word.charAt(i);
            if (correctCopy.contains(guessedChar) && result.get(i) != 2) {
                result.set(i, 1);
                for (int j = 0; j < correctCopy.size(); j++) {
                    if (correctCopy.get(j) == guessedChar) {
                        correctCopy.set(j, '\0'); // blank out the matched character
                        break;
                    }
                }
            }
        }
        return result;
    }

    public List<Integer> getResult() {
        return result;
    }

    public boolean isGuessedCorrectly() {
        return guessedCorrectly;
    }

    public static void main(String[] args) {
        // Correct word for the test
        String correctWord = "apple";

        // Test guesses
        String[] testGuesses = {"apply", "peach", "piano", "alpha", "apple"};

        // Iterate over each guess and create a Guess object
        for (String guess : testGuesses) {
            Guess guessAttempt = new Guess(guess, correctWord);
            // Print the result of the guess
            System.out.println(guessAttempt);
        }
    }
}

