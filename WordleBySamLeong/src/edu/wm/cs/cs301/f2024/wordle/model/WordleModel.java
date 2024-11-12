package edu.wm.cs.cs301.f2024.wordle.model;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.JDialog;

import edu.wm.cs.cs301.f2024.wordle.controller.ReadWordsRunnable;

public class WordleModel extends Model {
	/*
	 * a random value for selecting a random word
	 */
	private Random random;

	public WordleModel() {
		super();
		this.random = new Random();
	}

	/*
	 * for setting the word for testing
	 */
	public void setCurrentWordForTesting(String word) {
		try {
			wordsThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.currentWord = word.toUpperCase().toCharArray();
	}

	/*
	 * resets the game for next game
	 */
	public void initialize() {
		/*
		 * makes empty grids and resets current position
		 */
		this.wordleGrid = initializeWordleGrid();
		this.currentColumn = -1;
		this.currentRow = 0;
		/*
		 * get a new word and make reset the guess char[]
		 */
		generateCurrentWord();
		this.guess = new char[columnCount];
	}

	/*
	 * selects a random viable word to be the goal word and updates currentWord
	 */
	public void generateCurrentWord() {
		String word = getCurrentWord();
		/*
		 * updates currentWord with the randomly picked word
		 */
		this.currentWord = word.toUpperCase().toCharArray();
	}

	private String getCurrentWord() {
		return wordList.get(getRandomIndex());
	}

	private int getRandomIndex() {
		int size = wordList.size();
		return random.nextInt(size);
	}

	/*
	 * chooses a random word from the wordlist and sets currentWord to it
	 */
	public void setCurrentWord() {
		int index = getRandomIndex();
		currentWord = wordList.get(index).toCharArray();
	}

	/*
	 * sets the background colors of a guessed word and locks in the word
	 * 
	 * @return returns false if the current row is the last one TODO currentcolumn =
	 * -1 is suspicious
	 */
	public boolean setCurrentRow() {
		/*
		 * waits for the ReadWordsRunnable thread to be finished by entering into a
		 * thread that waits till wordsThread is done
		 */
		try {
			this.wordsThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (isWordViable()) {
			for (int column = 0; column < guess.length; column++) {
				Color backgroundColor = AppColors.GRAY;
				Color foregroundColor = Color.WHITE;
				System.out.println("GuessChar: " + guess[column] + " currentChar: " + currentWord[column]);
				if (guess[column] == currentWord[column]) {
					backgroundColor = AppColors.GREEN;
				} else if (contains(currentWord, guess, column)) {
					backgroundColor = AppColors.YELLOW;
				}

				wordleGrid[currentRow][column] = new WordleResponse(guess[column], backgroundColor, foregroundColor);
			}

			currentColumn = -1;
			currentRow++;
			guess = new char[columnCount];

			return currentRow < maximumRows;
		}
		return currentRow < maximumRows;
	}

	/*
	 * for testing
	 */
	public boolean isCharacterInCurrent(char c) {
		for (char a : currentWord) {
			if (a == c) {
				return true;
			}
		}
		return false;
	}

	/*
	 * for testing
	 */
	public char[] getAnswer() {
		return currentWord;
	}
}
