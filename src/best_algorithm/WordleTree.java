package best_algorithm;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class WordleTree {

    public static int[][] generateMatrix(WordList puzzleWords, WordList guessingWords, Map<List<Integer>, Integer> possibleAnswers) {
        int[][] matrix = new int[puzzleWords.size()][guessingWords.size()];

        for (int i = 0; i < puzzleWords.size(); i++) {
            String correctWord = puzzleWords.getWord(i);
            for (int j = 0; j < guessingWords.size(); j++) {
                String guessWord = guessingWords.getWord(j);
                Guess guess = new Guess(guessWord, correctWord);
                List<Integer> result = guess.getResult();
                matrix[i][j] = possibleAnswers.get(result);
            }
        }
        return matrix;
    }

    public static int[][] getMatrix(WordList puzzleWords, WordList guessingWords, Map<List<Integer>, Integer> possibleAnswers) throws IOException {
        int[][] matrix = generateMatrix(puzzleWords, guessingWords, possibleAnswers);
        System.out.println("Matrix generated successfully.");
        return matrix;
    }

    public static Map<List<Integer>, Integer> generatePossibleAnswers() {
        Map<List<Integer>, Integer> possibleAnswers = new HashMap<>();
        for (int i = 0; i < Math.pow(3, 5); i++) {
            List<Integer> mask = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                mask.add((i / (int) Math.pow(3, j)) % 3);
            }
            possibleAnswers.put(mask, i);
        }
        return possibleAnswers;
    }

    public static List<Integer> getDistribution(List<Integer> wordNs, int guessWordN, int[][] matrix) {
        Map<Integer, Integer> answers = new HashMap<>();
        for (int i = 0; i < 243; i++) {
            answers.put(i, 0);
        }

        for (int n : wordNs) {
            answers.put(matrix[n][guessWordN], answers.get(matrix[n][guessWordN]) + 1);
        }

        List<Integer> nonZeroSizes = new ArrayList<>();
        for (int count : answers.values()) {
            if (count != 0) {
                nonZeroSizes.add(count);
            }
        }
        return nonZeroSizes;
    }

    public static int scoreDistribution(List<Integer> distribution) {
        return distribution.size();
    }

    public static List<Integer> getTopGuesses(List<Integer> wordNs, Set<Integer> ignoreNs, List<Integer> guessWordsNs, int[][] matrix) {
        if (wordNs.size() < 500) {
            for (int guessWord : wordNs) {
                List<Integer> distribution = getDistribution(wordNs, guessWord, matrix);
                if (Collections.frequency(distribution, 1) == distribution.size()) {
                    return Collections.singletonList(guessWord);
                }
            }
        }

        int options = wordNs.size() > 300 ? 3 : (wordNs.size() >= 10 ? 10 : 20);

        List<Integer> bestN = new ArrayList<>(Collections.nCopies(options, null));
        List<Integer> bestScore = new ArrayList<>(Collections.nCopies(options, null));

        for (int guessN : guessWordsNs) {
            if (ignoreNs.contains(guessN)) {
                continue;
            }
            List<Integer> distribution = getDistribution(wordNs, guessN, matrix);
            int score = scoreDistribution(distribution);

            for (int i = 0; i < options; i++) {
                if (bestN.get(i) == null || score > bestScore.get(i)) {
                    bestScore.add(i, score);
                    bestN.add(i, guessN);
                    bestScore.remove(options);
                    bestN.remove(options);
                    break;
                }
            }
        }
        return bestN;
    }

    public static Map<Integer, List<Integer>> getValidResults(List<Integer> wordNs, int guessWordN, int[][] matrix) {
        Map<Integer, List<Integer>> answers = new HashMap<>();
        for (int i = 0; i < 243; i++) {
            answers.put(i, new ArrayList<>());
        }

        for (int n : wordNs) {
            answers.get(matrix[n][guessWordN]).add(n);
        }

        Map<Integer, List<Integer>> out = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : answers.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                out.put(entry.getKey(), entry.getValue());
            }
        }
        return out;
    }

    public static int resultLength(List<List<Integer>> result) {
        int count = 0;
        for (List<Integer> line : result) {
            count += line.size();
        }
        return count;
    }

    public static String resultToText(List<List<Integer>> result, WordList guessingWords) {
        StringBuilder out = new StringBuilder();
        for (List<Integer> line : result) {
            int correctWordIndex = line.get(line.size() - 1);
            String correctWord = guessingWords.getWord(correctWordIndex);
            for (int wordIndex : line) {
                String guessWord = guessingWords.getWord(wordIndex);
                out.append(guessWord).append("\t");
                Guess guess = new Guess(guessWord, correctWord);
                for (int r : guess.getResult()) {
                    out.append(r);
                }
                out.append("\t");
            }
            out.append("\n");
        }
        return out.toString();
    }

    public static List<List<Integer>> addNode(List<Integer> wordNs, List<Integer> guessWordsNs, int[][] matrix, List<Integer> previousGuesses) {
        System.out.println("Starting new node for the list of " + wordNs.size());
        List<Integer> bestGuesses = getTopGuesses(wordNs, new HashSet<>(previousGuesses), guessWordsNs, matrix);
        System.out.println("Best are: " + bestGuesses);

        List<List<Integer>> finalResult = null;
        for (int bestGuess : bestGuesses) {
            List<List<Integer>> out = new ArrayList<>();
            Map<Integer, List<Integer>> answers = getValidResults(wordNs, bestGuess, matrix);

            for (Map.Entry<Integer, List<Integer>> entry : answers.entrySet()) {
                int answer = entry.getKey();
                List<Integer> newList = entry.getValue();

                if (newList.size() == 1 || answer == 242) {
                    List<Integer> line = new ArrayList<>(previousGuesses);
                    if (answer != 242) {
                        line.add(bestGuess);
                    }
                    line.add(newList.get(0));
                    out.add(line);
                } else {
                    List<Integer> newGuesses = new ArrayList<>(previousGuesses);
                    newGuesses.add(bestGuess);
                    out.addAll(addNode(newList, guessWordsNs, matrix, newGuesses));
                }
            }

            if (finalResult == null || resultLength(out) < resultLength(finalResult)) {
                finalResult = out;
            }
        }
        return finalResult;
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        WordList puzzleWords = new WordList("src/best_algorithm/words-guess.txt");
        WordList guessingWords = new WordList("src/best_algorithm/words-guess.txt", "src/best_algorithm/words-all.txt");

        Map<List<Integer>, Integer> possibleAnswers = generatePossibleAnswers();
        int[][] matrix = getMatrix(puzzleWords, guessingWords, possibleAnswers);

        List<Integer> wordNs = new ArrayList<>();
        for (int i = 0; i < puzzleWords.size(); i++) {
            wordNs.add(i);
        }

        List<Integer> guessWordsNs = new ArrayList<>();
        for (int i = 0; i < guessingWords.size(); i++) {
            guessWordsNs.add(i);
        }

        List<List<Integer>> result = addNode(wordNs, guessWordsNs, matrix, new ArrayList<>());
        System.out.println("Result: " + result);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt"))) {
            writer.write(resultToText(result, guessingWords));
        }

        System.out.println("Ave: " + ((double) resultLength(result) / 2315));
    }
}
