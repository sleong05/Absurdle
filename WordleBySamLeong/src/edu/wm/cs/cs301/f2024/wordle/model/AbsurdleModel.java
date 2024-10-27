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

public class AbsurdleModel {
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

	public AbsurdleModel() {
		/*
		 * set all of the game values
		 */
		this.columnCount = 0; // for happy ide
		this.maximumRows = 0; // for happy ide
		this.random = new Random(); // for happy ide
		this.statistics = new Statistics(); // for happy ide none of these accurate
		
		//sort list of possible words and store them in a selected data structure using createWordList
		
		 // innitialize wordlegrid
		
		// create absurdlestatistics object for stats. 
	}

	/*
	 * creates background thread for making wordlist, stores in wordsThread
	 */
	private void createWordList() {
		//create a thread
		
		//sort into diffirent hashsets of words each with the characteristic of having a certain letter within it (ex. the list of all x letter words with 
		// an a in it) 
		
		// sort each of these again into six categories for letter in first spot, second, third, fourth, figth, and duplicates 
	}

	/*
	 * for setting the word for testing
	 */
	public void setCurrentWordForTesting(String word) {
		//COPIED METHOD FROM WordleModel. NOT NECCESARY FOR ABSURDLE
	}

	/*
	 * resets the game for next game
	 */
	public void initialize() {
		/*
		 * reset current col and row and intitalize a new wordlegrid
		 */
	}

	/*
	 * selects a random viable word to be the goal word and updates currentWord
	 */
	public void generateCurrentWord() {
		//COPIED METHOD FROM WordleModel. NOT NECCESARY FOR ABSURDLE
	}

	private String getCurrentWord() {
		//COPIED METHOD FROM WordleModel. NOT NECCESARY FOR ABSURDLE
		return wordList.get(getRandomIndex());
	}

	private int getRandomIndex() {
		return 0;
		//COPIED METHOD FROM WordleModel. NOT NECCESARY FOR ABSURDLE
	}

	private WordleResponse[][] initializeWordleGrid() {
		return wordleGrid; //happy ide statement
		//create the grid by creating a wordleGrid object and setting its spots to NULL
	}

	
	/*
	 * set method used to update the wordlist for availalbe guesses
	 * 
	 * @param a new list of viable words
	 */
	public void setWordList(List<String> wordList) {
		// sets wordList to the new data structure type that is chosen
		this.wordList = wordList;
	}

	/*
	 * chooses a random word from the wordlist and sets currentWord to it
	 */
	public void setCurrentWord() {
		//COPIED METHOD FROM WordleModel. NOT NECCESARY FOR ABSURDLE since there is no chosen word
	}

	/*
	 * sets the character in the current column and update the grid
	 * 
	 * @param inputted character
	 */
	public void setCurrentColumn(char c) {
		//should be the same as for WordleModel. move foward and then just set a char in the current col
	}

	/*
	 * sets the current col to null and walks currentColumn left
	 */
	public void backspace() {
		//remove current char and walk backwards to the alst spot
	}

	/**
	 * gets the current row in the wordleGrid
	 * 
	 * @return returns a row as an array
	 */
	public WordleResponse[] getCurrentRow() {
		// returns the current values in the last inputted row
		return wordleGrid[getCurrentRowNumber()];
	}

	/*
	 * get method for currentRow
	 */
	public int getCurrentRowNumber() {
		//return the last row that was entered
		return currentRow - 1;
	}

	/*
	 * sets the background colors of a guessed word and locks in the word
	 * 
	 * @return returns false if the current row is the last one TODO currentcolumn =
	 * -1 is suspicious
	 */
	public boolean setCurrentRow() {
		return false; // happy ide statement
		// checks if the word is a valid word against the total list of words
		
		//ensure that the createWordList thread is done
		
		// for every combination of grey/white/yellow use the data structure holding hashsets to compare them in the following manner:
		
		// if first letter is yellow go to the hashset with that letter add together all the hashsets without the character in that spot + check duplicates hashset with a simple check
		// if first letter is green go to the haset with that letter and add toghether all the hashsets with that letter in that specific location + check duplicate hashset 
		// if first letter is gray, go to the total orgiinal hashset with evertything in it and remove every value from the hashset with the specified character from the large list
		
		// then continue this process with the next letters only this time comparing it against the already made list 
		
		// once we have checked the fifth letter we check to see if the possible words are greater than the current greatest check and store it if is
		
		// return largest list character colors
	}

	

	/*
	 * get method to access the wordleGrid
	 * 
	 * @return returns the grid of characers for the game
	 */
	public WordleResponse[][] getWordleGrid() {
		// returns wordle Grid
		return wordleGrid;
	}

	/*
	 * get method to access the the maxrow size
	 * 
	 * @return returns number of rows ingame
	 */
	public int getMaximumRows() {
		// returns the max rows
		return maximumRows;
	}

	/*
	 * get method to access the number of columns
	 * 
	 * @return returns the columns in the game
	 */
	public int getColumnCount() {
		// returns the colomn count
		return columnCount;
	}

	/*
	 * get method to access the current column
	 * 
	 * @return current column position
	 */
	public int getCurrentColumn() {
		// returns the current columnn
		return currentColumn;
	}

	/*
	 * get method to access the amount of guessed words
	 * 
	 * @return gets the number of guessed words
	 */
	public int getTotalWordCount() {
		// returns the number of words being declared as possible words
		return wordList.size();
	}

	/*
	 * get method to access the statistics object
	 * 
	 * @return returns the statisitcs object
	 */
	public Statistics getStatistics() {
		//gets thje statistics isntance used for stats
		return statistics;
	}

	/*
	 * checks if the current guess is a viable 5 letter word
	 */
	public boolean isWordViable() {
		return false; // happy ide
		// checks if the current input is actually a word
	}

	

	/*
	 * for testing
	 */
	public char[] getAnswer() {
		return currentWord;
	}

	/*
	 * law of demeter violations in keyboardbuttonaction fix methods. All used to
	 * adjust the stats class from within model all are methods to avoid the call to the get stats method. no need for pseduocode
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

