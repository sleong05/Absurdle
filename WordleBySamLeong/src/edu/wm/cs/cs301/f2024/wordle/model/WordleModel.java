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

public class WordleModel {
	/*
	 * two arrays for the currentWord and the guess for comparison
	 */
	private char[] currentWord, guess;
	/*
	 * size of the game ints
	 */
	private final int columnCount, maximumRows;
	/*
	 * ints for current position in game
	 */
	private int currentColumn, currentRow;
	/*
	 * list of words that are possible guesses
	 */
	private List<String> wordList;
	/*
	 * a random value for selecting a random word
	 */
	private final Random random;
	/*
	 * initilizes a statistics object to store previous game data
	 */
	private final Statistics statistics;
	/*
	 * the game grid
	 */
	private WordleResponse[][] wordleGrid;

	/*
	 * refrence to thread to the background thread that gets the wordlist
	 */
	private Thread wordsThread;
	/*
	 * constructer that innitilizes game size values
	 */

	public WordleModel() {
		/*
		 * default game values
		 */
		this.currentColumn = -1;
		this.currentRow = 0;
		this.columnCount = 5;
		this.maximumRows = 6;
		this.random = new Random();
		/*
		 * starts the readwordsrunnable thread
		 */
		createWordList();
		/*
		 * initilizes the grid and fills it with nuls
		 */
		this.wordleGrid = initializeWordleGrid();
		/*
		 * initilizes the size of the character array that stores the guess
		 */
		this.guess = new char[columnCount];
		/*
		 * initilzes a statistics object to store stats when the game is over
		 */
		this.statistics = new Statistics();
	}

	/*
	 * creates background thread for making wordlist, stores in wordsThread
	 */
	private void createWordList() {
		ReadWordsRunnable runnable = new ReadWordsRunnable(this);
		wordsThread = new Thread(runnable);
		wordsThread.start();
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

	private WordleResponse[][] initializeWordleGrid() {
		WordleResponse[][] wordleGrid = new WordleResponse[maximumRows][columnCount];

		for (int row = 0; row < wordleGrid.length; row++) {
			for (int column = 0; column < wordleGrid[row].length; column++) {
				wordleGrid[row][column] = null;
			}
		}

		return wordleGrid;
	}

	/*
	 * set method used to update the wordlist for availalbe guesses
	 * 
	 * @param a new list of viable words
	 */
	public void setWordList(List<String> wordList) {
		this.wordList = wordList;
	}

	/*
	 * chooses a random word from the wordlist and sets currentWord to it
	 */
	public void setCurrentWord() {
		int index = getRandomIndex();
		currentWord = wordList.get(index).toCharArray();
	}

	/*
	 * sets the character in the current column and update the grid
	 * 
	 * @param inputted character
	 */
	public void setCurrentColumn(char c) {
		/*
		 * moves the column foward unless it would go past the end
		 */
		currentColumn++;
		currentColumn = Math.min(currentColumn, (columnCount - 1));
		/*
		 * adjusts grid and the guess char[]
		 */
		guess[currentColumn] = c;
		wordleGrid[currentRow][currentColumn] = new WordleResponse(c, Color.WHITE, Color.BLACK);
	}

	/*
	 * sets the current col to null and walks currentColumn left
	 */
	public void backspace() {
		/*
		 * empties guess and grid
		 */
		wordleGrid[currentRow][currentColumn] = null;
		guess[currentColumn] = ' ';
		this.currentColumn--;
		this.currentColumn = Math.max(currentColumn, -1);
	}

	/**
	 * gets the current row in the wordleGrid
	 * 
	 * @return returns a row as an array
	 */
	public WordleResponse[] getCurrentRow() {
		return wordleGrid[getCurrentRowNumber()];
	}

	/*
	 * get method for currentRow
	 */
	public int getCurrentRowNumber() {
		return currentRow - 1;
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

	private boolean contains(char[] currentWord, char[] guess, int column) {
		/*
		 * checks the instances of a char in the word
		 */

		int instancesOfWord = 0;
		for (int index = 0; index < currentWord.length; index++) {
			if (guess[index] != currentWord[index] && guess[column] == currentWord[index]) {
				instancesOfWord += 1;
			}
		}
		/*
		 * checks the current number of highlighted instances of the char
		 */
		int currentInstancesOfWordHighligheted = 0;
		for (int index1 = 0; index1 < currentWord.length; index1++) {
			WordleResponse currentWordleResponse = wordleGrid[currentRow][index1];
			if (currentWordleResponse instanceof WordleResponse) {
				if (currentWordleResponse.getBackgroundColor() == AppColors.YELLOW
						&& currentWordleResponse.getChar() == guess[column]) {
					currentInstancesOfWordHighligheted += 1;
				}
			} else {
				break;
			}
		}
		for (int index = 0; index < currentWord.length; index++) {
			if (index != column && guess[column] == currentWord[index]
					&& currentInstancesOfWordHighligheted < instancesOfWord) {
				return true;
			}
		}

		return false;
	}

	/*
	 * get method to access the wordleGrid
	 * 
	 * @return returns the grid of characers for the game
	 */
	public WordleResponse[][] getWordleGrid() {
		return wordleGrid;
	}

	/*
	 * get method to access the the maxrow size
	 * 
	 * @return returns number of rows ingame
	 */
	public int getMaximumRows() {
		return maximumRows;
	}

	/*
	 * get method to access the number of columns
	 * 
	 * @return returns the columns in the game
	 */
	public int getColumnCount() {
		return columnCount;
	}

	/*
	 * get method to access the current column
	 * 
	 * @return current column position
	 */
	public int getCurrentColumn() {
		return currentColumn;
	}

	/*
	 * get method to access the amount of guessed words
	 * 
	 * @return gets the number of guessed words
	 */
	public int getTotalWordCount() {
		return wordList.size();
	}

	/*
	 * get method to access the statistics object
	 * 
	 * @return returns the statisitcs object
	 */
	public Statistics getStatistics() {
		return statistics;
	}

	/*
	 * checks if the current guess is a viable 5 letter word
	 */
	public boolean isWordViable() {
		String currentInput = "";
		/*
		 * grabs guess and makes it into a string
		 */
		for (char letter : this.guess) {
			currentInput += letter;
		}
		currentInput = currentInput.toLowerCase();
		if (this.wordList.contains(currentInput)) {
			return true;
		}
		/*
		 * warns that it is not a valid word with a popup for 1 second
		 */
		JOptionPane optionPane = new JOptionPane("The word '" + currentInput + "' is not a valid word",
				JOptionPane.WARNING_MESSAGE);
		JDialog dialog = optionPane.createDialog("Invalid Word");

		Timer timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		timer.setRepeats(false);
		timer.start();

		dialog.setVisible(true);

		return false;
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

	/*
	 * law of demeter violations in keyboardbuttonaction fix methods. All used to
	 * adjust the stats class from within model
	 */
	public void incrementTotalGamesPlayed() {
		statistics.incrementTotalGamesPlayed();
	}

	public void addWordsGuessed(int currentRowNumber) {
		statistics.addWordsGuessed(currentRowNumber);
	}

	public void setCurrentStreak(int currentStreak) {
		statistics.setCurrentStreak(currentStreak);
	}
	
	public int getCurrentStreak() {
		return statistics.getCurrentStreak();
	}
	
}

