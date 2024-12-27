# Wordle Solver Project

## Overview
This project is a Wordle-solving algorithm implemented in Java. It systematically guesses words based on feedback from the user and solves the puzzle efficiently. The project includes a simulation system, a tree-based optimization algorithm, and interactive user input functionality.

---

## Key Features
- **Interactive Mode**: The `UserInterface` class allows users to input feedback and receive the next best guess.
- **Decision Tree Algorithm**: The `WordleTree` class builds an optimized decision tree for solving Wordle puzzles efficiently.
- **Simulation**: The `WordleSimulator` class tests different strategies and evaluates performance metrics.
- **Feedback Matching**: Uses the `results.txt` file to match guesses with feedback and select the best next word.

---

## Project Components

### 1. `Guess.java`
- Represents a single guess in the Wordle game.
- Evaluates the guess against the correct word and provides feedback.

### 2. `Wordle.java`
- Manages the core Wordle game logic, including tracking guesses and the correct word.

### 3. `WordList.java`
- Manages lists of valid words and provides filtering and scoring utilities.

### 4. `WordleTree.java`
- Builds an optimized decision tree to minimize the number of guesses required to solve a Wordle puzzle.

### 5. `WordleSimulator.java`
- Simulates Wordle games to evaluate and refine strategies.

### 6. `results.txt`
- Stores precomputed feedback and corresponding next guesses for different starting words.

## Documentation: 
https://docs.google.com/document/d/1r7TAagnsaEGEyXmbmzDMw4qruF0KA7gDqJQNCp-leeQ/edit?tab=t.0#heading=h.v8otpqnpcujy

