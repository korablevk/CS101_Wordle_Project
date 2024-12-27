package best_algorithm;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

class WordList {

    public List<String> wordList;
    private Map<Character, Integer> letterCount;
    private Map<String, Integer> wordScores;
    private List<Map<Character, Integer>> positionLetterCount;
    private Map<String, Integer> positionWordScores;

    public WordList(String... files) throws IOException {
        // list of all the words
        wordList = new ArrayList<>();
        for (String file : files) {
            try (BufferedReader inFile = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                String line;
                while ((line = inFile.readLine()) != null) {
                    wordList.add(line.trim());
                }
            }
        }

        letterCount = new HashMap<>();
        wordScores = new HashMap<>();
        positionLetterCount = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            positionLetterCount.add(new HashMap<>());
        }
        positionWordScores = new HashMap<>();

        genWordScores();
        genPositionalWordScores();
    }

    public WordList copy() throws IOException {
        WordList newWordList = new WordList();
        newWordList.wordList = new ArrayList<>(this.wordList);
        newWordList.wordScores = new HashMap<>(this.wordScores);
        newWordList.positionWordScores = new HashMap<>(this.positionWordScores);
        return newWordList;
    }

    public String getWord(int index) {
        return wordList.get(index);
    }

    public List<String> getWordList() {
        return wordList;
    }

    public int size() {
        return wordList.size();
    }

    public String getRandomWord() {
        Random random = new Random();
        return wordList.get(random.nextInt(wordList.size()));
    }

    public String getHiscoreWord(boolean usePosition) {
        Map<String, Integer> scores = usePosition ? positionWordScores : wordScores;
        String bestWord = "";
        int bestScore = 0;
        for (String word : wordList) {
            if (scores.getOrDefault(word, 0) > bestScore) {
                bestScore = scores.get(word);
                bestWord = word;
            }
        }
        return bestWord;
    }

    public String getMaximizedWord(Set<Character> maximizedLetters) {
        genLetterCount();
        String bestWord = "";
        int bestScore = 0;
        for (String word : wordList) {
            int thisScore = 0;
            for (char letter : maximizedLetters) {
                if (word.indexOf(letter) != -1) {
                    thisScore++;
                }
            }
            if (thisScore > bestScore) {
                bestScore = thisScore;
                bestWord = word;
            }
        }
        return bestWord;
    }

    public void genLetterCount() {
        letterCount.clear();
        for (char c = 'a'; c <= 'z'; c++) {
            letterCount.put(c, 0);
        }
        for (String word : wordList) {
            for (char letter : word.toCharArray()) {
                letterCount.put(letter, letterCount.getOrDefault(letter, 0) + 1);
            }
        }
    }

    public void genPositionalLetterCount() {
        for (int i = 0; i < 5; i++) {
            positionLetterCount.get(i).clear();
            for (char c = 'a'; c <= 'z'; c++) {
                positionLetterCount.get(i).put(c, 0);
            }
        }
        for (String word : wordList) {
            for (int i = 0; i < word.length(); i++) {
                char letter = word.charAt(i);
                positionLetterCount.get(i).put(letter, positionLetterCount.get(i).get(letter) + 1);
            }
        }
    }

    public void genWordScores() {
        genLetterCount();
        wordScores.clear();
        for (String word : wordList) {
            int wordScore = 0;
            for (char letter : word.toCharArray()) {
                wordScore += letterCount.get(letter);
            }
            wordScores.put(word, wordScore);
        }
    }

    public void genPositionalWordScores() {
        genPositionalLetterCount();
        positionWordScores.clear();
        for (String word : wordList) {
            Map<Character, Integer> wordScore = new HashMap<>();
            for (int i = 0; i < word.length(); i++) {
                char letter = word.charAt(i);
                wordScore.put(letter, Math.max(wordScore.getOrDefault(letter, 0), positionLetterCount.get(i).get(letter)));
            }
            positionWordScores.put(word, wordScore.values().stream().mapToInt(Integer::intValue).sum());
        }
    }

    public void filterByMask(List<List<Character>> yesMask, List<List<Character>> noMask, List<Set<Character>> allowedMask) {
        List<String> newWords = new ArrayList<>();
        for (String word : wordList) {
            boolean valid = true;
            for (int n = 0; n < yesMask.size(); n++) {
                List<Character> mustHaveLetters = yesMask.get(n);
                if (!mustHaveLetters.isEmpty() && word.charAt(n) != mustHaveLetters.get(0)) {
                    valid = false;
                    break;
                }
            }
            if (!valid) continue;

            for (int n = 0; n < noMask.size(); n++) {
                List<Character> forbiddenLetters = noMask.get(n);
                for (char forbiddenLetter : forbiddenLetters) {
                    if (word.charAt(n) == forbiddenLetter) {
                        valid = false;
                        break;
                    }
                }
                if (!valid) break;
            }
            if (!valid) continue;

            for (char letter = 'a'; letter <= 'z'; letter++) {
                // Count the occurrences of the current letter in the word
                int count = 0;
                for (char ch : word.toCharArray()) {
                    if (ch == letter) {
                        count++;
                    }
                }

                // Check if the count of occurrences is allowed for this letter
                if (!allowedMask.get(count).contains(letter)) {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                newWords.add(word);
            }
        }
        wordList = newWords;
    }
}

// Main function for testing
public class WordleSimulator {
    public static void main(String[] args) {
        try {
            // Initialize WordList with a test file
            WordList wordList = new WordList("src/best_algorithm/words-guess.txt");

            // Display random word
            System.out.println("Random Word: " + wordList.getRandomWord());

            // Display high-score word
            System.out.println("High-Score Word (Non-positional): " + wordList.getHiscoreWord(false));

            // Test maximized word
            Set<Character> testLetters = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u'));
            System.out.println("Maximized Word: " + wordList.getMaximizedWord(testLetters));
        } catch (IOException e) {
            System.err.println("Error initializing WordList: " + e.getMessage());
        }
    }
}
