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

public class AbsurdleModel extends Model {
	private alphabetTree tree;
	private Thread treeCreationThread;

	public AbsurdleModel() {
		super();
		// creates datat structure

		// waits for wordlist to eb finish
		try {
			this.wordsThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		buildTree();

	}

	private void buildTree() {
		// creates a thread to create the alphabetTree Datastructure and builds it
		treeCreationThread = new Thread(() -> {
			// Initialize alphabetTree with the word list
			this.tree = new alphabetTree(wordList.toArray(new String[0]));
		});
		treeCreationThread.start();
	}

	private void rebuildTree(HashSet<String> biggestSet) {
		// creates a thread to create the alphabetTree Datastructure and builds it
		Thread treeCreationThread = new Thread(() -> {
			// Initialize alphabetTree with the word list
			this.tree = new alphabetTree(biggestSet.toArray(new String[0]));
		});
		treeCreationThread.start();
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
		buildTree();
		this.guess = new char[columnCount];
	}

	/*
	 * sets the background colors of a guessed word and locks in the word
	 * 
	 * @return returns false if the current row is the last one TODO currentcolumn =
	 * -1 is suspicious
	 */
	public boolean setCurrentRow() {
		// checks if the word is a valid word against the total list of words
		System.out.println("started setcurrentrow");
		// ensure that the createTree thread is done
		try {
			this.treeCreationThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Thread Joined");
		if (isWordViable()) {
			System.out.println("wordviable");
			tree.printSubTree('a');
			HashSet<String> biggestList = tree.biggestList(); //finds the correct way to accept the answer
			System.out.println("Finished Tree Calculations");
			int[] colorsOfOutput = tree.getColors();
			Color foregroundColor = Color.WHITE;
			Color backgroundColor = AppColors.GRAY;
			System.out.println("biggestTree = " + biggestList);
			System.out.println("Colors = " + colorsOfOutput);
			
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
			
			for (int column = 0; column < guess.length; column++) {
				if (colorsOfOutput[column] == 1) { // Yellow case
					backgroundColor = AppColors.YELLOW;
				} else if (colorsOfOutput[column] == 2) { // green case
					backgroundColor = AppColors.GREEN;
				}
				wordleGrid[currentRow][column] = new WordleResponse(guess[column], backgroundColor, foregroundColor);
			}
			currentColumn = -1;
			currentRow++;
			guess = new char[columnCount];
			
			rebuildTree(biggestList);

			// checks if list is all green (win condition)
		}
		return currentRow < maximumRows; // happy ide statement
	}

	/*
	 * get method to access the wordleGrid
	 * 
	 * @return returns the grid of characers for the game
	 */
	/*
	 * overides the selected word list
	 */
	public void overideWordList(List<String> possibleWords) {
		// wait for thread to finish
		this.wordList = possibleWords;

	}

//data structure to store and retrieve aburdle info
	public class alphabetTree {
		// subNode that stores sets of words with a char in all 5 positions
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

		int[] colors;
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
				System.out.println("added word: " + word);
				possibleWords.add(word);
				int position = 0;
				for (char C : word.toCharArray()) {
					int index = C - 'a';
					listOfSubNodes[index].add(word, position);
					position++;
				}
			}
			System.out.println("created tree");
		}

		public HashSet<String> getPossibleWords() {
			return possibleWords;
		}

		public subTreeNode getSubTree(char letter) {
			return listOfSubNodes[letter - 'a'];

		}
		
		public void printSubTree(char letter) {
			subTreeNode subTree = getSubTree(letter);
			for (int i =0; i<5; i++) {
				System.out.println(subTree.getSet(i));
			}
		}

		public int[] getColors() {
			return colors;
		}

		public HashSet<String> getGray(char letter, int position) {
			HashSet<String> hasLetterin = getSubTree(letter).getTotal(); // set that contains that letter

			HashSet<String> copySetOfAll = new HashSet<>(possibleWords); // copy of all possible words

			copySetOfAll.removeAll(hasLetterin); // removes all of that letter from it
			return copySetOfAll;
		}

		public HashSet<String> getYellow(char letter, int position) {
			HashSet<String> hasLetterin = new HashSet<>(getSubTree(letter).getTotal()); // copy of set of words with
																						// letter in them
			HashSet<String> letterInPosition = getSubTree(letter).getSet(position); // set of words with letter in
																					// position

			hasLetterin.removeAll(letterInPosition); // set of words with letter not in position
			return hasLetterin;

		}

		public HashSet<String> getGreen(char letter, int position) {
			HashSet<String> letterInPosition = new HashSet<>(getSubTree(letter).getSet(position)); // copy of set with
																									// words with letter
																									// in position
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

		// make sure to update guess first
		public HashSet<String> biggestList() {
			int mostWords = 0;
			HashSet<String> biggestSet = null;
			HashSet<String> spot1, spot2, spot3, spot4, spot5;
			for (int i = 0; i < 3; i++) {
				spot1 = checkPosition(i, guess[0], 0);
				for (int j = 0; j < 3; j++) {
					spot2 = checkPosition(j, guess[1], 1);
					for (int k = 0; k < 3; k++) {
						spot3 = checkPosition(k, guess[2], 2);
						for (int l = 0; l < 3; l++) {
							spot4 = checkPosition(l, guess[3], 3);
							for (int m = 0; m < 3; m++) {
								spot5 = checkPosition(m, guess[4], 4);

								HashSet<String> spot1Copy = new HashSet<>(spot1);
								spot1Copy.retainAll(spot2);
								spot1Copy.retainAll(spot3);
								spot1Copy.retainAll(spot4);
								spot1Copy.retainAll(spot5);
								if (spot1Copy.size() > mostWords) {
									mostWords = spot1Copy.size();
									biggestSet = spot1Copy;
									colors = new int[] { i, j, k, l, m };
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
