package edu.wm.cs.cs301.f2024.wordle.model;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.JDialog;

import edu.wm.cs.cs301.f2024.wordle.controller.ReadWordsRunnable;

public class AbsurdleModel extends WordleModel {
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

		// sort list of possible words and store them in a selected data structure using
		// createWordList

		// innitialize wordlegrid

		// create absurdlestatistics object for stats.
	}

	/*
	 * creates background thread for making wordlist, stores in wordsThread
	 */
	private void createWordList() {
		// create a thread

		// sort into diffirent hashsets of words each with the characteristic of having
		// a certain letter within it (ex. the list of all x letter words with
		// an a in it)

		// sort each of these again into six categories for letter in first spot,
		// second, third, fourth, figth, and duplicates
	}

	/*
	 * for setting the word for testing
	 */
	public void setCurrentWordForTesting(String word) {
		// COPIED METHOD FROM WordleModel. NOT NECCESARY FOR ABSURDLE
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
		// COPIED METHOD FROM WordleModel. NOT NECCESARY FOR ABSURDLE
	}

	private String getCurrentWord() {
		// COPIED METHOD FROM WordleModel. NOT NECCESARY FOR ABSURDLE
		return wordList.get(getRandomIndex());
	}

	private int getRandomIndex() {
		return 0;
		// COPIED METHOD FROM WordleModel. NOT NECCESARY FOR ABSURDLE
	}

	private WordleResponse[][] initializeWordleGrid() {
		return wordleGrid; // happy ide statement
		// create the grid by creating a wordleGrid object and setting its spots to NULL
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
		// COPIED METHOD FROM WordleModel. NOT NECCESARY FOR ABSURDLE since there is no
		// chosen word
	}

	/*
	 * sets the character in the current column and update the grid
	 * 
	 * @param inputted character
	 */
	public void setCurrentColumn(char c) {
		// should be the same as for WordleModel. move foward and then just set a char
		// in the current col
	}

	/*
	 * sets the current col to null and walks currentColumn left
	 */
	public void backspace() {
		// remove current char and walk backwards to the alst spot
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
		// return the last row that was entered
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

		// ensure that the createWordList thread is done

		// for every combination of grey/white/yellow use the data structure holding
		// hashsets to compare them in the following manner:

		// if first letter is yellow go to the hashset with that letter add together all
		// the hashsets without the character in that spot + check duplicates hashset
		// with a simple check
		// if first letter is green go to the haset with that letter and add toghether
		// all the hashsets with that letter in that specific location + check duplicate
		// hashset
		// if first letter is gray, go to the total orgiinal hashset with evertything in
		// it and remove every value from the hashset with the specified character from
		// the large list

		// then continue this process with the next letters only this time comparing it
		// against the already made list

		// once we have checked the fifth letter we check to see if the possible words
		// are greater than the current greatest check and store it if is

		// return largest list character colors and put them in the grid

		// checks if list is all green (win condition)
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
		// gets thje statistics isntance used for stats
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
	 * adjust the stats class from within model all are methods to avoid the call to
	 * the get stats method. no need for pseduocode
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

	public boolean isCharacterInCurrent(char b) {
		// TODO Auto-generated method stub
		/*
		 * checks if a char is in the current input
		 */
		return false;
	}

	/*
	 * overides the selected word list
	 */
	public void overideWordList(List<String> possibleWords) {
		// wait for thread to finish
		this.wordList = possibleWords;

	}
//data structure to store and retrieve aburdle info
	public class alphabetTree {
		//subNode that stores sets of words with a char in all 5 positions
		public class subTreeNode {
			char letter;

			public HashSet<String>[] hashSetStorage;
			public HashSet<String> total = new HashSet<>();

			public subTreeNode(char letter) {
				this.hashSetStorage = new HashSet[5];
				this.letter = letter;

				for (int i = 0; i < 5; i++) {
					hashSetStorage[i] = new HashSet<>();
				}
			}

			public void add(String word, int letterPosition) {
				total.add(word);
				hashSetStorage[letterPosition].add(word);
			}

			public HashSet<String> getSet(int locationOfLetter) {
				return hashSetStorage[locationOfLetter];

			}

			public HashSet<String> getTotal() {
				return total;
			}
		}

		
		public HashSet<String> possibleWords = new HashSet<>();
		public subTreeNode[] listOfSubNodes = new subTreeNode[26];
		char[] alphabet = new char[26];

		public alphabetTree(String[] wordList) {
			for (int i = 0; i < 26; i++) {
				alphabet[i] = (char) ('a' + i); // Fill the array with the alphabet
				listOfSubNodes[i] = new subTreeNode((char) ('a' + i));
			}

			// iterates through the whole list of words and adds them to the various
			// subNodes in their correct spots
			for (String word : wordList) {
				possibleWords.add(word);
				int position = 0;
				for (char C : word.toCharArray()) {
					int index = C - 'a';
					listOfSubNodes[index].add(word, position);
					position++;
				}
			}
		}

		public HashSet<String> getPossibleWords() {
			return possibleWords;
		}

		public subTreeNode getSubTree(char letter) {
			return listOfSubNodes[letter - 'a'];

		}

		public HashSet<String> getGray(char letter, int position) {
			HashSet<String> hasLetterin = getSubTree(letter).getTotal(); //set that contains that letter

			HashSet<String> copySetOfAll = new HashSet<>(possibleWords); // copy of all possible words

			copySetOfAll.removeAll(hasLetterin); // removes all of that letter from it
			return copySetOfAll;
		}

		public HashSet<String> getYellow(char letter, int position) {
			HashSet<String> hasLetterin = new HashSet<>(getSubTree(letter).getTotal()); // copy of set of words with letter in them
			HashSet<String> letterInPosition = getSubTree(letter).getSet(position); // set of words with letter in
																					// position

			hasLetterin.removeAll(letterInPosition); // set of words with letter not in position
			return hasLetterin;

		}

		public HashSet<String> getGreen(char letter, int position) {
			HashSet<String> letterInPosition = new HashSet<>(getSubTree(letter).getSet(position)); //copy of set with words with letter in position
			return letterInPosition;
		}

		public HashSet<String> checkPosition(int color, char letter, int position) {
			if (color == 0) {
				return getGray(letter, position);
			} else if (color == 1) {
				return getYellow(letter, position);
			} else {
				return getGreen(letter, position);
			}
		}
		//make sure to update guess first
		public HashSet<String> biggestList() {
			int mostWords = 0;
			HashSet<String> biggestSet = null;
			HashSet<String> spot1,spot2,spot3,spot4,spot5;
			for (int i = 0; i<3; i++) {
				spot1 = checkPosition(i, guess[0], 0);
				for (int j = 0; j<3; j++) {
					spot2 = checkPosition(j, guess[1], 1);
					for (int k = 0; k<3; k++) {
						spot3 = checkPosition(k, guess[2], 2);
						for (int l = 0; l<3; l++) {
							spot4 = checkPosition(l, guess[3], 3);
							for (int m = 0; m<3; m++) {
								spot5 = checkPosition(m, guess[4], 4);
								
								HashSet<String> spot1Copy = new HashSet<>(spot1);
								spot1Copy.retainAll(spot2);
								spot1Copy.retainAll(spot3);
								spot1Copy.retainAll(spot4);
								spot1Copy.retainAll(spot5);
								if (spot1Copy.size() > mostWords) {
									mostWords = spot1Copy.size();
									biggestSet = spot1Copy;
								}
							}
						}
					}
				}
			}
			return biggestSet;
			
		}
	}

}
