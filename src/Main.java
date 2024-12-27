import java.io.*;
import java.util.*;

class Wordle
{
    public static void main(String[] args)
    {
        final String BG_GREEN = "\u001b[42m";
        final String BG_YELLOW = "\u001b[43m";
        final String RESET = "\u001b[0m";

        System.out.println("WORDLE!");

        // Read words from the file and load them into a list
        List<String> words = loadWordsFromFile("src/best_algorithm/words-guess.txt");

        if (words == null || words.isEmpty()) {
            System.out.println("Error: No words available for the game.");
            return;
        }

        // Randomly select a word from the list
        int wIndex = (int) (Math.random() * words.size());
        String correct = words.get(wIndex);
        System.out.println("DEBUG: Correct word is " + correct); // Debugging line

        Scanner sc = new Scanner(System.in);
        String guess = "";

        // Loop for six guesses
        for (int round = 0; round < 6; round++) {
            System.out.print("Please guess. > ");
            guess = sc.nextLine().toUpperCase();

            // Ensure the guess is a valid 5-letter word
            if (guess.length() != 5) {
                System.out.println("Invalid guess! Please enter a 5-letter word.");
                round--;  // Do not count the invalid guess
                continue;
            }

            // Create a loop to iterate through each letter
            for (int i = 0; i < 5; i++) {
                if (guess.substring(i, i + 1).equals(correct.substring(i, i + 1))) {
                    // Letter matches
                    System.out.print(BG_GREEN + guess.substring(i, i + 1) + RESET);
                } else if (correct.indexOf(guess.substring(i, i + 1)) > -1) {
                    // Letter is in word, but different location
                    System.out.print(BG_YELLOW + guess.substring(i, i + 1) + RESET);
                } else {
                    // Letter not in word
                    System.out.print(guess.substring(i, i + 1));
                }
            }

            System.out.println("");

            // If the guess is correct
            if (guess.equals(correct)) {
                System.out.println("Correct! You win!");
                break;
            }

        }

        // Print correct answer if player loses
        if (!guess.equals(correct)) {
            System.out.println("Wrong! The correct word is " + correct + ".");
        }
    }

    // Load words from the file into a list
    private static List<String> loadWordsFromFile(String filename) {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming each word is on a new line
                words.add(line.trim().toUpperCase());
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            return null;
        }
        return words;
    }
}
