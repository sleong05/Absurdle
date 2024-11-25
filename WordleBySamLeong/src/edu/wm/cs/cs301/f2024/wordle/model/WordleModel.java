package edu.wm.cs.cs301.f2024.wordle.model;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JDialog;

import edu.wm.cs.cs301.f2024.wordle.controller.ReadWordsRunnable;

public class WordleModel extends Model {
	/*
	 * a random value for selecting a random word
	 */
	private Random random;

	private char[] currentWord;

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

	// returns the size of the wordlist
	public int getTotalWordCount() {
		try {
			wordsThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wordList.size();
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
			statsThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (checkAcceptanceRules()) {
			WordleResponse[] row = wordleGrid[currentRow];
			for (int index = 0; index < getColumnCount(); index++) {
				wordleGrid[currentRow][index] = new WordleResponse(row[index].getChar(), Color.WHITE, Color.BLACK);
				System.out.println(wordleGrid[currentRow][index].getChar() + " and "
						+ wordleGrid[currentRow][index].getBackgroundColor());
			}
			for (int column = 0; column < guess.length; column++) {
				// resets to default colors before adjusting

				Color backgroundColor = AppColors.GRAY;
				Color foregroundColor = Color.WHITE;
				System.out.println(guess[column] + " ========= " + currentWord[column]);
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

	@Override
	public WordleResponse onceButton() {
		int currentPosition = getCurrentColumn() + 1;
		WordleResponse response = setCurrentColumnAsColor(currentWord[currentPosition], AppColors.GREEN);
		onceLeft -= 1;
		return response;

	}

	@Override
	public WordleResponse twiceButton() {
		char letterInWord = getPossibleYellow();
		if (letterInWord == '$') {
			return new WordleResponse('$', null, null);
		}
		WordleResponse response = setCurrentColumnAsColor(letterInWord, AppColors.YELLOW);
		twiceLeft -= 1;
		return response;

	}

	// finds what letters are on the grid and returns the four other letters of the
	// currentWord that are not in the currenposition that also have not been seen
	// on the grid before
	private char getPossibleYellow() {
		List<Character> responses = new ArrayList<>();
		List<Character> guesses = new ArrayList<>();
		for (int i = 0; i < getMaximumRows(); i++) {
			for (int j = 0; j < getColumnCount(); j++) {
				if (wordleGrid[i][j] != null) {
					WordleResponse response = wordleGrid[i][j];
					if (response.getBackgroundColor() == AppColors.GRAY
							|| response.getBackgroundColor() == AppColors.GREEN
							|| response.getBackgroundColor() == AppColors.YELLOW) {
						responses.add(response.getChar());
					}

				}
			}
		}
		int columPosition = getCurrentColumn() + 1;
		int index = -1;
		for (char letter : currentWord) {
			index += 1;
			if (letter == currentWord[columPosition]) { // skips positionwhere itself is
				continue;
			}
			guesses.add(letter);
			System.out.println(responses + " letter is " + letter);
			if (responses.contains(letter)) {
				guesses.removeLast();
			}
		}
		if (guesses.size() == 0) {
			return '$';
		}
		Random random = new Random();
		thriceLeft -= 1;
		return guesses.get(random.nextInt(guesses.size()));
	}

	@Override
	public char thriceButton(JButton[] buttons) {
		List<JButton> possibleButtons = new ArrayList<>();
		String currentWordAsString = "";
		for (char c : currentWord) {
			currentWordAsString += c;
		}
		for (JButton button1 : buttons) {
			if (button1.getBackground() != AppColors.GRAY && button1.getBackground() != AppColors.GREEN
					&& button1.getBackground() != AppColors.YELLOW) {
				if (!(button1.getActionCommand().equals("Backspace")) && !(button1.getActionCommand().equals("Enter"))
						&& !(currentWordAsString.contains(Character.toString(button1.getActionCommand().charAt(0))))) {
					possibleButtons.add(button1);
				}
			}
		}

		char buttonToGray = '$';
		if (possibleButtons.size() != 0) {
			Random random = new Random();
			buttonToGray = possibleButtons.get(random.nextInt(possibleButtons.size())).getActionCommand().charAt(0);
		}
		return buttonToGray;
	}
}
