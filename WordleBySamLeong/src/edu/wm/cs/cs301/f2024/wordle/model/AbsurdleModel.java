package edu.wm.cs.cs301.f2024.wordle.model;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
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
		
		 
		treeCreationThread = new Thread(() -> {
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
	
	public int getTotalWordCount() {
		try {
			wordsThread.join();
			treeCreationThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tree.getPossibleWords().size();
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
		if (isWordViable()) {
			HashSet<String> biggestList = tree.biggestList(); // finds the correct way to accept the answer
			System.out.println("Finished Tree Calculations");
			int[] colorsOfOutput = tree.getColors();
			Color foregroundColor = Color.WHITE;
			System.out.println("biggestTree = " + biggestList);
			System.out.println("Colors = " + colorsOfOutput[0] + colorsOfOutput[1] + colorsOfOutput[2]
					+ colorsOfOutput[3] + colorsOfOutput[4]);

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
				Color backgroundColor = AppColors.GRAY;
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
				this.hashSetStorage = new HashSet[6];
				this.letter = letter;

				for (int i = 0; i < 6; i++) {
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
		// edge case variables
		public HashSet<String> threeOfSameLetterWords = new HashSet<>();
		public HashSet<String> doubleDoubleLetterCase = new HashSet<>();

		public alphabetTree(String[] wordList) {
			for (int i = 0; i < 26; i++) {
				alphabet[i] = (char) ('A' + i); // Fill the array with the alphabet
				listOfSubNodes[i] = new subTreeNode((char) ('A' + i));
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

					// find duplicates
					int count = 0;
					for (char letter : word.toCharArray()) {
						if (C == letter) {
							count++;
						}
						if (count > 1) {
							int newindex = C - 'a';
							listOfSubNodes[newindex].add(word, 5);
						}
						if (count > 2) {
							threeOfSameLetterWords.add(word);
						}
					}
				}
			}
			System.out.println("created tree");
			System.out.println(possibleWords);
		}

		public HashSet<String> getPossibleWords() {
			return possibleWords;
		}

		public subTreeNode getSubTree(char letter) {
			return listOfSubNodes[letter - 'A'];

		}

		public void printSubTree(char letter) {
			subTreeNode subTree = getSubTree(letter);
			for (int i = 0; i < 5; i++) {
				System.out.println(subTree.getSet(i));
			}
		}

		public int[] getColors() {
			return colors;
		}

		// finds duplicat letters in a word
		public List<Character> findDuplicates(char[] word) {
			HashSet<Character> seen = new HashSet<>();

			List<Character> duplicates = new ArrayList<>();

			for (char letter : word) {
				if (seen.contains(letter)) {
					if (!duplicates.contains(letter)) {
						duplicates.add(letter);
					}
				} else {
					seen.add(letter);
				}
			}

			return duplicates;
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
			HashSet<String> spot1, spot2, spot3, spot4, spot5, duplicateSet;
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

								int[] colorArray = new int[] { i, j, k, l, m };

								// duplicate case checks
								List<Character> duplicateLetter = findDuplicates(guess);
								boolean secondLoop = false; // to rememeber to compart against first double letter case

								if (duplicateLetter.size() > 0) {
									// for changing back if spot values are adjusted
									HashSet<String> old1 = new HashSet<>(spot1);
									HashSet<String> old2 = new HashSet<>(spot2);
									HashSet<String> old3 = new HashSet<>(spot3);
									HashSet<String> old4 = new HashSet<>(spot4);
									HashSet<String> old5 = new HashSet<>(spot5);
									for (char duplicate : duplicateLetter) {
										int counter = 0; // to count the duplicates that arent gray
										int yellowCounter = 0;
										int greenCounter = 0;

										for (int p = 0; p < 5; p++) { // checks for duplicates that are yellow or green
											if (guess[p] == duplicate && colorArray[p] > 0) {
												if (colorArray[p] == 1) {
													yellowCounter++;
												} else if (colorArray[p] == 2) {
													greenCounter++;
												}
												counter++;
											}
										}
										// checks for duplicates that are grey. scenario where a letter is grey and
										// another instance of that letter as a yellow has the meaning of it being
										// yellow
										
										for (int q = 0; q < 5; q++) {
											if (guess[q] == duplicate && colorArray[q] == 0 && yellowCounter > 0) {

												ArrayList<HashSet<String>> listOfSpots = new ArrayList(); 
												// adjust gray meaing to be yellow if there is another non gray same letter in word
																											
												listOfSpots.add(spot1);
												listOfSpots.add(spot2);
												listOfSpots.add(spot3);
												listOfSpots.add(spot4);
												listOfSpots.add(spot5);
												

												HashSet<String> spot = listOfSpots.get(q);
												spot = checkPosition(1, guess[q], q);
												if (q==0) {
													spot1 = spot;
												} else if (q==1) {
													spot2 = spot;
												} else if (q==2) {
													spot3 = spot;
												} else if (q==3) {
													spot4 = spot;
												} else if (q==4) {
													spot5 = spot;
												} 

											} else if (guess[q] == duplicate && colorArray[q] == 0
													&& greenCounter > 0) {
												// case where there are no yellows but at least one of a duplicate is
												// green
												// if so make sure it does not have two letter unless there are two
												// greens which in that case make sure there are three letters
												ArrayList<HashSet<String>> listOfSpots = new ArrayList(); 
												// adjust gray spot meaning to be yellow if there is another non gray sae  letter in word
																											
												listOfSpots.add(spot1);
												listOfSpots.add(spot2);
												listOfSpots.add(spot3);
												listOfSpots.add(spot4);
												listOfSpots.add(spot5);

												HashSet<String> spot = listOfSpots.get(q);
												spot = getSubTree(duplicate).getTotal();

												HashSet<String> setOfDuplicates = getSubTree(duplicate).getSet(5);
												spot.removeAll(setOfDuplicates);
												if (q==0) {
													spot1 = spot;
												} else if (q==1) {
													spot2 = spot;
												} else if (q==2) {
													spot3 = spot;
												} else if (q==3) {
													spot4 = spot;
												} else if (q==4) {
													spot5 = spot;
												} 
	
												if (counter == 2) { 
													// its a 3 of same letter word  and one is gray others are not, therefore it must have two of that letter
													spot = (getSubTree(duplicate)).getSet(5);
													HashSet<String> notInSet = getSubTree(duplicate).getSet(q);
													spot.removeAll(notInSet);
													if (q==0) {
														spot1 = spot;
													} else if (q==1) {
														spot2 = spot;
													} else if (q==2) {
														spot3 = spot;
													} else if (q==3) {
														spot4 = spot;
													} else if (q==4) {
														spot5 = spot;
													} 

												}

											}
										}
										// rechecks with adjusted spots
										
										spot1Copy = new HashSet<>(spot1);
										spot1Copy.retainAll(spot2);
										spot1Copy.retainAll(spot3);
										spot1Copy.retainAll(spot4);
										spot1Copy.retainAll(spot5);
										
										if (counter > 1) { // intersects against set with two of that letter
											duplicateSet = getSubTree(duplicate).getSet(5);
											spot1Copy.retainAll(duplicateSet);
											
										}
										if (counter > 2) { // intersects against set with all three letter words
											spot1Copy.retainAll(threeOfSameLetterWords);
										}
										if (secondLoop) { // special case with two double letters. requires second check
															// to find shared elements with first check
											spot1Copy.retainAll(doubleDoubleLetterCase);
										}
									}
									if (duplicateLetter.size()>=1) {
										secondLoop = true;
										doubleDoubleLetterCase = spot1Copy;
									}
									// changes back adjusted values to originals
									spot1 = old1;
									spot2 = old2;
									spot3 = old3;
									spot4 = old4;
									spot5 = old5;
								}
								for (int color : colorArray) {
									System.out.print(color);
								}
								System.out.print(": ");
								System.out.print(spot1Copy + "    Length = " + spot1Copy.size() + "\n");
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
