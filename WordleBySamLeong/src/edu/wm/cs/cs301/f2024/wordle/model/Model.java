package edu.wm.cs.cs301.f2024.wordle.model;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import edu.wm.cs.cs301.f2024.wordle.controller.ReadWordsRunnable;
import edu.wm.cs.cs301.f2024.wordle.view.WordleFrame;

public abstract class Model {
	protected char[] guess;
	/*
	 * size of the game ints
	 */
	protected int columnCount;
	protected int maximumRows;
	/*
	 * ints for current position in game
	 */
	protected int currentColumn, currentRow;
	/*
	 * list of words that are possible guesses
	 */
	protected List<String> wordList;
	/*
	 * initilizes a statistics object to store previous game data
	 */
	protected Statistics statistics;
	/*
	 * the game grid
	 */
	protected WordleResponse[][] wordleGrid;

	/*
	 * refrence to thread to the background thread that gets the wordlist
	 */
	protected Thread wordsThread;
	/*
	 * constructer that innitilizes game size values
	 */
	List<AcceptanceRule> rules = new ArrayList<>();

	Thread statsThread; // background thread for reading stats
	
	protected int onceLeft;
	protected int twiceLeft;
	protected int thriceLeft;

	public Model() {
		/*
		 * default game values
		 */
		this.currentColumn = -1;
		this.currentRow = 0;
		this.columnCount = 5;
		this.maximumRows = 6;
		this.onceLeft = 1;
		this.twiceLeft = 2;
		this.thriceLeft = 3;
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
		 * initilzes a statistics object to store stats when the game is over in a
		 * thread
		 */
		createStatistics();
		// adds basic rules to the game
		RuleBasic defaultRules = new RuleBasic();
		rules.add(defaultRules);
	}

	/*
	 * creates background thread for making wordlist, stores in wordsThread
	 */
	protected void createWordList() {
		ReadWordsRunnable runnable = new ReadWordsRunnable(this);
		wordsThread = new Thread(runnable);
		wordsThread.start();
	}

	// creates background thread for statistics
	protected void createStatistics() {
		statistics = new Statistics();
		statsThread = new Thread(statistics);
		statsThread.start();
	}

	/*
	 * resets the game for next game
	 */
	public abstract void initialize();

	protected WordleResponse[][] initializeWordleGrid() {
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
		wordleGrid[currentRow][currentColumn] = new WordleResponse(c, AppColors.WHITE, AppColors.BLACK);
	}
	
	protected WordleResponse setCurrentColumnAsColor(char c, Color backgroundColor) {
		/*
		 * moves the column foward unless it would go past the end
		 */
		currentColumn++;
		currentColumn = Math.min(currentColumn, (columnCount - 1));
		/*
		 * adjusts grid and the guess char[]
		 */
		guess[currentColumn] = c;
		WordleResponse response = new WordleResponse(c, backgroundColor, AppColors.WHITE);
		wordleGrid[currentRow][currentColumn] = response;
		return response;
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
	public WordleResponse[] getNextRow() {
		return wordleGrid[currentRow];
	}
	
	public WordleResponse[] getCurrentRow() {
		return wordleGrid[getCurrentRowNumber()];
	}

	private Thread getStatsThread() {
		return statsThread;
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
	
	public abstract WordleResponse onceButton();
	
	public abstract WordleResponse twiceButton();
	
	public abstract char thriceButton(JButton[] buttons);
	
	public abstract boolean setCurrentRow();

	protected boolean contains(char[] currentWord, char[] guess, int column) {
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
	public abstract int getTotalWordCount();

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
	public void showInvalidWordDialogue() {
		String currentInput = AppStrings.EMPTY;
		for (char letter: guess) {
			currentInput += letter;
		}
		/*
		 * warns that it is not a valid word with a popup for 1 second
		 */
		JOptionPane optionPane = new JOptionPane(AppStrings.THE_WORD_ + currentInput + AppStrings._IS_NOT_A_VALID_WORD,
				JOptionPane.WARNING_MESSAGE);
		JDialog dialog = optionPane.createDialog(AppStrings.INVALID_WORD);

		Timer timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		timer.setRepeats(false);
		timer.start();

		dialog.setVisible(true);
		
	}

	/*
	 * law of demeter violations in keyboardbuttonaction fix methods. All used to
	 * adjust the stats class from within model
	 */
	public void incrementTotalGamesPlayed() {
		try {
			statsThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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

	public char[] getCurrentGuess() {
		return guess;
	}

	public List<String> getWordList() {
		return wordList;
	}

	public void addRule(AcceptanceRule rule) {
		rules.add(rule);
	}

	public boolean checkAcceptanceRules() {
		for (AcceptanceRule Rule : rules) {
			if (!(Rule.isAcceptableGuess(this))) { // case where a rule has failed
				showInvalidWordDialogue();
				int spotsToDelete = getCurrentColumn() + 1;
				for (int i = 0; i < spotsToDelete; i++) {
					backspace();
				}
				return false;
			}
		}
		return true;
	}

	public int getOnceLeft() {
		return onceLeft;
	}
	public int getTwiceLeft() {
		return twiceLeft;
	}
	public int getThriceLeft() {
		return thriceLeft;
	}
	
	public int getTotalGamesWon() {
	    return statistics.getTotalGamesWon(); 
	}
	
	public int getLastWin() {
	    return statistics.getLastWin(); 
	}
	
	public int[] calculateArrayOfWins(int maximumTries) {
	    return statistics.calculateArrayOfWins(maximumTries); 
	}

	public int getTotalGamesPlayed() {
		return statistics.getTotalGamesPlayed();
	}

	public int getLongestStreak() {
		return statistics.getLongestStreak();
	}

	public void saveDataToFile() {
		try {
			getStatsThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		statistics.writeStatistics();
	}
}
